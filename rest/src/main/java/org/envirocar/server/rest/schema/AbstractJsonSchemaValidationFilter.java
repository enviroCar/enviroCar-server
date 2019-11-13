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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.StreamSupport;

public abstract class AbstractJsonSchemaValidationFilter implements ResourceFilter {
    private final JsonSchemaFactory schemaFactory;
    private final JsonNodeCreator nodeCreator;
    private final ObjectReader reader;

    AbstractJsonSchemaValidationFilter(JsonSchemaFactory schemaFactory,
                                       JsonNodeCreator nodeCreator,
                                       ObjectReader reader) {
        this.schemaFactory = schemaFactory;
        this.nodeCreator = nodeCreator;
        this.reader = reader;
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return null;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

    protected void validate(byte[] bytes, MediaType mediaType) throws ValidationException, IOException {
        Optional<String> schemaAttribute = MediaTypes.getSchemaAttribute(mediaType);
        if (schemaAttribute.isPresent()) {
            try {
                String entity = new String(bytes, ReaderWriter.getCharset(mediaType));
                validate(reader.readTree(entity), schemaFactory.getJsonSchema(schemaAttribute.get()));
            } catch (ProcessingException ex) {
                throw new ValidationException(ex);
            }
        }

    }

    private void validate(JsonNode instance, JsonSchema schema) throws ValidationException, ProcessingException {
        ProcessingReport report = schema.validate(instance);
        if (!report.isSuccess()) {
            ObjectNode error = nodeCreator.objectNode();
            StreamSupport.stream(report.spliterator(), false)
                    .map(ProcessingMessage::asJson)
                    .forEach(error.putArray(JSONConstants.ERRORS_KEY)::add);
            error.set(JSONConstants.INSTANCE_KEY, instance);
            throw new JsonValidationException(error);
        }
    }


}
