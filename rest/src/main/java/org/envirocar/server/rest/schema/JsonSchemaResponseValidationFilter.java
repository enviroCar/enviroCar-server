/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ContainerResponseWriter;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

public class JsonSchemaResponseValidationFilter extends AbstractJsonSchemaValidationFilter
        implements ContainerResponseFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JsonSchemaResponseValidationFilter.class);
    private final ObjectWriter writer;

    @Inject
    public JsonSchemaResponseValidationFilter(JsonSchemaFactory schemaFactory,
                                              JsonNodeCreator nodeCreator,
                                              ObjectReader reader,
                                              ObjectWriter writer) {
        super(schemaFactory, nodeCreator, reader);
        this.writer = writer;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return this;
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
                try {
                    validate(bytes, mediaType);
                } catch (JsonValidationException v) {
                    LOG.error(String.format("Created invalid response: Error:\n%s", writer.writeValueAsString(v.getError())), v);
                } catch (ValidationException v) {
                    LOG.error(String.format("Created invalid response: Error: %s", v.getMessage()), v);
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
