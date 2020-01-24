/*
 * Copyright (C) 2013-2020 The enviroCar project
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
import com.github.fge.jackson.JsonLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

@Singleton
public class JsonSchemaUriLoaderImpl implements JsonSchemaUriLoader {
    private final LoadingCache<URI, JsonNode> jsonCache = jsonCache();
    private final JsonSchemaUriConfiguration jsonSchemaUriConfiguration;

    @Inject
    public JsonSchemaUriLoaderImpl(JsonSchemaUriConfiguration jsonSchemaUriConfiguration) {
        this.jsonSchemaUriConfiguration = jsonSchemaUriConfiguration;
    }

    @Override
    public JsonNode load(URI uri) throws IOException {
        try {
            return jsonCache.get(uri);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
    }

    private LoadingCache<URI, JsonNode> jsonCache() {
        return CacheBuilder.newBuilder().build(new CacheLoader<URI, JsonNode>() {
            @Override
            public JsonNode load(URI key) throws IOException {
                return JsonLoader.fromURL(jsonSchemaUriConfiguration.toInternalURI(key).toURL());
            }
        });
    }
}
