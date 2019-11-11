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

import com.google.inject.ImplementedBy;
import org.envirocar.server.rest.MediaTypes;

import javax.ws.rs.core.MediaType;
import java.net.URI;

@ImplementedBy(JsonSchemaUriConfigurationImpl.class)
public interface JsonSchemaUriConfiguration {
    /**
     * Convert the schema {@link URI} to it's external form.
     *
     * @param uri The internal URI.
     * @return The external URI.
     */
    URI toExternalURI(URI uri);

    /**
     * Convert the schema {@link URI} to it's internal URI.
     *
     * @param uri The internal URI.
     * @return The resource URI.
     */
    URI toInternalURI(URI uri);

    default boolean isSchema(MediaType mediaType, String schema) {
        return schema != null && !schema.isEmpty() &&
                MediaTypes.getSchemaAttribute(mediaType)
                        .map(URI::create)
                        .map(URI::getPath)
                        .map(path -> path.split("/"))
                        .filter(path -> path.length > 0)
                        .map(path -> path[path.length - 1])
                        .map(schema::equals)
                        .orElse(false);
    }
}
