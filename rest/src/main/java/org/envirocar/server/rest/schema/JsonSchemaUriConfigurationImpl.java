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

import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.resources.JsonSchemaResource;
import org.envirocar.server.rest.resources.RootResource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class JsonSchemaUriConfigurationImpl implements JsonSchemaUriConfiguration {
    private final Provider<UriInfo> uriInfo;

    @Inject
    public JsonSchemaUriConfigurationImpl(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public URI toExternalURI(URI uri) {
        if (uri.isAbsolute() || uri.getScheme() != null || uri.getPath().isEmpty()) {
            return uri;
        }
        return uriInfo.get()
                .getBaseUriBuilder()
                .path(RootResource.SCHEMA)
                .path(JsonSchemaResource.SCHEMA)
                .fragment(uri.getFragment())
                .build(uri.getPath());
    }

    @Override
    public URI toInternalURI(URI uri) {
        if (uri.isAbsolute() || uri.getScheme() != null || uri.getPath().isEmpty()) {
            return uri;
        }
        String resourceName = String.format("/schema/%s", uri.getPath());
        URL resource = JsonSchemaUriConfigurationImpl.class.getResource(resourceName);
        if (resource != null) {
            try {
                return resource.toURI();
            } catch (URISyntaxException ignored) {
            }
        }
        return uri;
    }

    @Override
    public boolean isSchema(MediaType mediaType, String schema) {
        return MediaTypes.getSchemaAttribute(mediaType)
                .map(URI::create)
                .map(this::toExternalURI)
                .map(s -> s.equals(toExternalURI(URI.create(schema))))
                .orElse(false);
    }

}
