/*
 * Copyright (C) 2013-2018 The enviroCar project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.envirocar.server.rest.schema;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.*;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.stream.StreamSupport;
import java.util.zip.GZIPInputStream;

public class JsonSchemaValidationFilter implements ResourceFilter, ContainerRequestFilter, ContainerResponseFilter {
    public static final String VALIDATE_REQUESTS = "validate_requests";
    public static final String VALIDATE_RESPONSES = "validate_responses";
    private static final Logger log = LoggerFactory.getLogger(JsonSchemaValidationFilter.class);

    private final JsonSchemaFactory schemaFactory;
    private final JsonNodeFactory factory;
    private final ObjectReader reader;
    private final ObjectWriter writer;
    private boolean validateResponses = true;
    private boolean validateRequests = true;

    @Inject
    public JsonSchemaValidationFilter(JsonSchemaFactory schemaFactory,
                                      JsonNodeFactory factory,
                                      ObjectReader reader,
                                      ObjectWriter writer) {
        this.schemaFactory = schemaFactory;
        this.factory = factory;
        this.reader = reader;
        this.writer = writer;
    }

    @Inject
    public void setValidateResponses(@Named(VALIDATE_RESPONSES) boolean validateResponses) {
        this.validateResponses = validateResponses;
    }

    @Inject
    public void setValidateRequests(@Named(VALIDATE_REQUESTS) boolean validateRequests) {
        this.validateRequests = validateRequests;
    }


    @Override
    public ContainerRequestFilter getRequestFilter() {
        return validateRequests ? this : null;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return validateResponses ? this : null;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {

        MediaType mediaType = request.getMediaType();
        if (!MediaTypes.hasSchemaAttribute(mediaType)) {
            return request;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = request.getEntityInputStream();
        try {
            ReaderWriter.writeTo(in, out);
            byte[] requestEntity = out.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestEntity);
            request.setEntityInputStream(byteArrayInputStream);
            String entity = new String(requestEntity, ReaderWriter.getCharset(mediaType));
            if (entity.isEmpty()) {
                throw new BadRequestException("empty entity");
            }
            JsonNode tree;
            try {
                tree = reader.readTree(entity);
            } catch (JsonParseException e) {
                throw new BadRequestException(e);
            }
            validate(tree, MediaTypes.getSchemaAttribute(mediaType));
        } catch (IOException ex) {
            throw new ContainerException(ex);
        }
        return request;
    }


    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        MediaType mediaType = response.getMediaType();
        if (!MediaTypes.hasSchemaAttribute(mediaType) || request.getMethod().equals(HttpMethod.HEAD)) {
            return response;
        }
        response.setContainerResponseWriter(new ValidatingWriter(response.getContainerResponseWriter()));
        return response;
    }

    private void validate(JsonNode entity, String schema) throws ValidationException {
        try {
            validate(entity, schemaFactory.getJsonSchema(schema));
        } catch (ProcessingException ex) {
            throw new ValidationException(ex);
        }
    }

    private void validate(JsonNode instance, JsonSchema schema) throws ValidationException, ProcessingException {
        ProcessingReport report = schema.validate(instance);
        if (!report.isSuccess()) {
            ObjectNode error = factory.objectNode();
            StreamSupport.stream(report.spliterator(), false)
                    .map(ProcessingMessage::asJson)
                    .forEach(error.putArray(JSONConstants.ERRORS_KEY)::add);
            error.set(JSONConstants.INSTANCE_KEY, instance);
            throw new JsonValidationException(error);
        }
    }

    private class ValidatingWriter implements ContainerResponseWriter {
        private final ContainerResponseWriter delegate;
        private ByteArrayOutputStream byteArrayOutputStream;
        private OutputStream delegateOutputStream;
        private MediaType mediaType;
        private String contentEncoding;

        ValidatingWriter(ContainerResponseWriter delegate) {
            this.delegate = delegate;
        }

        @Override
        public OutputStream writeStatusAndHeaders(long contentLength, ContainerResponse response)
                throws IOException {
            this.mediaType = response.getMediaType();
            this.contentEncoding = (String) response.getHttpHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);
            this.delegateOutputStream = delegate.writeStatusAndHeaders(contentLength, response);
            this.byteArrayOutputStream = new ByteArrayOutputStream();
            return this.byteArrayOutputStream;
        }

        @Override
        public void finish() throws IOException {
            byte[] bytes = this.byteArrayOutputStream.toByteArray();
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                ReaderWriter.writeTo(byteArrayInputStream, this.delegateOutputStream);
            }
            this.delegate.finish();
            if (bytes.length > 0) {
                if (contentEncoding != null && contentEncoding.equals("gzip")) {
                    bytes = gunzip(bytes);
                }
                String entity = new String(bytes, ReaderWriter.getCharset(mediaType));
                try {
                    validate(reader.readTree(entity), MediaTypes.getSchemaAttribute(mediaType));
                } catch (JsonValidationException v) {
                    log.error("Created invalid response: Error:\n" + writer.writeValueAsString(v.getError()), v);
                } catch (ValidationException v) {
                    log.error(String.format("Created invalid response: Error:\n%s\nGenerated Response:\n%s\n", v.getMessage(), entity), v);
                }
            }
        }

        private byte[] gunzip(byte[] bytes) throws IOException {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
                ReaderWriter.writeTo(gzipInputStream, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

}
