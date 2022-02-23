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
package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpStatus;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.guice.YAML;
import org.envirocar.server.rest.schema.JsonSchemaUriConfiguration;
import org.envirocar.server.rest.schema.JsonValidationException;
import org.envirocar.server.rest.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Produces({MediaTypes.JSON, MediaTypes.YAML, MediaType.APPLICATION_OCTET_STREAM})
public abstract class AbstractJSONMessageBodyWriter<T> implements MessageBodyWriter<T>, JSONEntityEncoder<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJSONMessageBodyWriter.class);
    @Inject
    private JsonSchemaFactory schemaFactory;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private JsonNodeCreator nodeCreator;
    @Inject
    private Provider<JsonSchemaUriConfiguration> jsonSchemaUriConfiguration;
    @Inject
    private ObjectWriter jsonWriter;
    @Inject
    @YAML
    private ObjectWriter yamlWriter;
    private final Class<T> classType;

    protected AbstractJSONMessageBodyWriter(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return this.classType.isAssignableFrom(type)
               && (mediaType.isCompatible(MediaTypes.JSON_TYPE)
                   || mediaType.isCompatible(MediaTypes.YAML_TYPE)
                   || mediaType.isCompatible(MediaType.APPLICATION_OCTET_STREAM_TYPE));
    }

    @Override
    public void writeTo(T entity, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream out)
            throws IOException, WebApplicationException {

        MediaType mt = mediaType.isCompatible(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                       ? MediaType.APPLICATION_JSON_TYPE : mediaType;
        Optional<URI> schema = getSchema(annotations, mediaType);

        if (schema.isPresent()) {
            URI uri = this.jsonSchemaUriConfiguration.get().toExternalURI(schema.get());
            mt = createMediaType(mt, uri);
            httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, mt);
        }

        JsonNode value = encodeJSON(entity, mt);

        schema.ifPresent(uri -> validateResponse(uri, value));

        if (mt.isCompatible(MediaTypes.JSON_TYPE)) {
            this.jsonWriter.writeValue(out, value);
        } else if (mt.isCompatible(MediaTypes.YAML_TYPE)) {
            this.yamlWriter.writeValue(out, value);
        } else {
            throw new WebApplicationException(HttpStatus.SC_NOT_ACCEPTABLE);
        }
        out.flush();
    }

    private Optional<URI> getSchema(Annotation[] annotations, MediaType mediaType) {
        Optional<URI> schema = MediaTypes.getSchemaAttribute(mediaType).map(URI::create);
        if (!schema.isPresent()) {
            return Arrays.stream(annotations)
                         .filter(Schema.class::isInstance)
                         .map(Schema.class::cast)
                         .findFirst()
                         .map(Schema::response)
                         .filter(x -> !x.isEmpty()).map(URI::create);
        }
        return schema;
    }

    private void validateResponse(URI id, JsonNode value) {
        URI uri = this.jsonSchemaUriConfiguration.get().toInternalURI(id);
        try {
            try {
                validate(value, this.schemaFactory.getJsonSchema(uri.toString()));
            } catch (ProcessingException e) {
                throw new ValidationException(e);
            }
        } catch (JsonValidationException e) {
            try {
                String error = this.jsonWriter.writeValueAsString(e.getError());
                LOG.error(String.format("Created invalid response: Error:\n%s", error), e);
            } catch (JsonProcessingException ex) {
                e.addSuppressed(ex);
                LOG.error(String.format("Created invalid response: Error:\n%s", e.getMessage()), e);
            }
        } catch (ValidationException e) {
            LOG.error(String.format("Created invalid response: Error: %s", e.getMessage()), e);
        }
    }

    private MediaType createMediaType(MediaType mediaType, URI schema) {
        ImmutableMap.Builder<String, String> parameters = ImmutableMap.builder();
        parameters.put(MediaTypes.SCHEMA_ATTRIBUTE, schema.toString());
        mediaType.getParameters().entrySet().stream()
                 .filter(e -> !e.getKey().equalsIgnoreCase(MediaTypes.SCHEMA_ATTRIBUTE))
                 .forEach(parameters::put);
        return new MediaType(mediaType.getType(), mediaType.getSubtype(), parameters.build());
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

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    protected JsonNodeCreator getJsonFactory() {
        return this.nodeCreator;
    }

    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    public abstract JsonNode encodeJSON(T entity, MediaType mediaType);

}
