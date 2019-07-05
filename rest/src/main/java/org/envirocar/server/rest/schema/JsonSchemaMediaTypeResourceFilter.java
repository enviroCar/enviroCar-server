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

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.spi.container.*;
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
public class JsonSchemaMediaTypeResourceFilter implements ResourceFilter, ContainerRequestFilter, ContainerResponseFilter {
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
        return requestSchema != null ? this : null;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return responseSchema != null ? this : null;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        URI schema = this.schemaUriConfiguration.get().toExternalURI(requestSchema);
        MediaType mediaType = adjustMediaType(request.getMediaType(), schema);
        request.getRequestHeaders().putSingle(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        return request;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        URI schema = this.schemaUriConfiguration.get().toExternalURI(responseSchema);
        MediaType mediaType = adjustMediaType(response.getMediaType(), schema);
        response.getHttpHeaders().putSingle(HttpHeaders.CONTENT_TYPE, mediaType);
        return response;
    }

    private MediaType adjustMediaType(MediaType mediaType, URI schema) {
        if (mediaType == null) {
            return createMediaType(schema);
        } else if (!mediaType.getParameters().containsKey(MediaTypes.SCHEMA_ATTRIBUTE)) {
            return createMediaType(mediaType, schema);
        }
        return mediaType;
    }

    private MediaType createMediaType(URI schema) {
        return createMediaType(Collections.singletonMap(MediaTypes.SCHEMA_ATTRIBUTE, schema.toString()));
    }

    private MediaType createMediaType(MediaType mediaType, URI schema) {
        Map<String, String> parameters = ImmutableMap.<String, String>builder()
                .putAll(mediaType.getParameters())
                .put(MediaTypes.SCHEMA_ATTRIBUTE, schema.toString())
                .build();
        return createMediaType(parameters);
    }

    private MediaType createMediaType(Map<String, String> parameters) {
        return new MediaType("application", "json", parameters);
    }
}
