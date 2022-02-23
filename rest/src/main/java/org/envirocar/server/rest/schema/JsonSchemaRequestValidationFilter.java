/*
 * Copyright (C) 2013-2022 The enviroCar project
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
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class JsonSchemaRequestValidationFilter implements ResourceFilter, ContainerRequestFilter {
    private final JsonSchemaFactory schemaFactory;
    private final JsonNodeCreator nodeCreator;
    private final ObjectReader reader;
    private final JsonSchemaUriConfiguration jsonSchemaUriConfiguration;

    @Inject
    public JsonSchemaRequestValidationFilter(JsonSchemaFactory schemaFactory,
                                             JsonNodeCreator factory,
                                             ObjectReader reader,
                                             JsonSchemaUriConfiguration jsonSchemaUriConfiguration) {
        this.schemaFactory = schemaFactory;
        this.nodeCreator = factory;
        this.reader = reader;
        this.jsonSchemaUriConfiguration = jsonSchemaUriConfiguration;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        if (!MediaTypes.hasSchemaAttribute(request.getMediaType())) {
            return request;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = request.getEntityInputStream();
        try {
            ReaderWriter.writeTo(in, out);
            byte[] requestEntity = out.toByteArray();
            request.setEntityInputStream(new ByteArrayInputStream(requestEntity));
            try {
                validate(requestEntity, request.getMediaType());
                return request;
            } catch (JsonParseException e) {
                throw new BadRequestException(e);
            }
        } catch (IOException ex) {
            throw new ContainerException(ex);
        }
    }

    private void validate(byte[] bytes, MediaType mediaType) throws ValidationException, IOException {
        Optional<String> schemaAttribute = MediaTypes.getSchemaAttribute(mediaType);
        if (schemaAttribute.isPresent()) {
            try {
                String entity = new String(bytes, ReaderWriter.getCharset(mediaType));
                URI uri = this.jsonSchemaUriConfiguration.toInternalURI(URI.create(schemaAttribute.get()));
                validate(this.reader.readTree(entity), this.schemaFactory.getJsonSchema(uri.toString()));
            } catch (ProcessingException ex) {
                throw new ValidationException(ex);
            }
        }
    }

    private void validate(JsonNode instance, JsonSchema schema) throws ValidationException, ProcessingException {
        ProcessingReport report = schema.validate(instance);
        if (!report.isSuccess()) {
            ObjectNode error = this.nodeCreator.objectNode();
            StreamSupport.stream(report.spliterator(), false)
                         .map(ProcessingMessage::asJson)
                         .forEach(error.putArray(JSONConstants.ERRORS_KEY)::add);
            error.set(JSONConstants.INSTANCE_KEY, instance);
            throw new JsonValidationException(error);
        }
    }
}
