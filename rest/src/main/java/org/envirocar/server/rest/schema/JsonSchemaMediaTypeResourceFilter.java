/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import org.envirocar.server.rest.MediaTypes;

import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Filter to add schema attributes to media types.
 */
public class JsonSchemaMediaTypeResourceFilter
        implements ResourceFilter, ContainerRequestFilter, ContainerResponseFilter {
    private final URI requestSchema;
    private final URI responseSchema;
    private final Provider<JsonSchemaUriConfiguration> schemaUriConfiguration;

    public JsonSchemaMediaTypeResourceFilter(
            @Nullable URI requestSchema, @Nullable URI responseSchema,
            Provider<JsonSchemaUriConfiguration> schemaUriConfiguration) {
        this.requestSchema = requestSchema;
        this.responseSchema = responseSchema;
        this.schemaUriConfiguration = Objects.requireNonNull(schemaUriConfiguration);
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this.requestSchema == null ? null : this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return this;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        URI schema = this.schemaUriConfiguration.get().toExternalURI(requestSchema);
        MediaType mediaType = adjustMediaType(request.getMediaType(), schema);
        request.getRequestHeaders().remove(HttpHeaders.CONTENT_TYPE);
        request.getRequestHeaders().putSingle(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        return request;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        if (MediaTypes.JSON_TYPE.isCompatible(response.getMediaType())) {
            URI schema = MediaTypes.getSchemaAttribute(response.getMediaType())
                                   .map(URI::create).orElse(this.responseSchema);
            if (schema != null) {
                schema = this.schemaUriConfiguration.get().toExternalURI(schema);
                MediaType mediaType = adjustMediaType(response.getMediaType(), schema);
                response.getHttpHeaders().putSingle(HttpHeaders.CONTENT_TYPE, mediaType);
            }
        }
        return response;
    }

    private MediaType adjustMediaType(MediaType mediaType, URI schema) {
        if (mediaType == null) {
            return createMediaType(schema);
        } else {
            return createMediaType(mediaType, schema);
        }
    }

    private MediaType createMediaType(URI schema) {
        return createMediaType(Collections.singletonMap(MediaTypes.SCHEMA_ATTRIBUTE, schema.toString()));
    }

    private MediaType createMediaType(MediaType mediaType, URI schema) {
        ImmutableMap.Builder<String, String> parameters = ImmutableMap.builder();
        parameters.put(MediaTypes.SCHEMA_ATTRIBUTE, schema.toString());
        mediaType.getParameters().entrySet().stream()
                 .filter(e -> !e.getKey().equalsIgnoreCase(MediaTypes.SCHEMA_ATTRIBUTE))
                 .forEach(parameters::put);
        return createMediaType(parameters.build());
    }

    private MediaType createMediaType(Map<String, String> parameters) {
        return new MediaType("application", "json", parameters);
    }
}
