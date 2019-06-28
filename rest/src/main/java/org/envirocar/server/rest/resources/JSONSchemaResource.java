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
package org.envirocar.server.rest.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.guice.JSONSchemaFactoryProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JSONSchemaResource extends AbstractResource {
    public static final String SCHEMA = "{schema}";
    private final Set<String> schemaPaths;
    private final JsonNodeFactory nodeFactory;
    private final LoadingCache<String, JsonNode> cache;

    @Inject
    public JSONSchemaResource(
            @Named(JSONSchemaFactoryProvider.SCHEMAS) Set<String> schemaIds,
            JsonNodeFactory nodeFactory,
            LoadingCache<String, JsonNode> cache) {
        this.schemaPaths = Preconditions.checkNotNull(schemaIds);
        this.nodeFactory = Preconditions.checkNotNull(nodeFactory);
        this.cache = Preconditions.checkNotNull(cache);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode get() {
        ObjectNode root = nodeFactory.objectNode();
        ArrayNode schemas = root.putArray(JSONConstants.SCHEMA);
        UriBuilder builder = getUriInfo().getRequestUriBuilder().path("{schema}");
        this.schemaPaths.stream()
                .map(path -> path.replaceFirst("/schema/", ""))
                .map(path -> builder.build(path))
                .map(URI::toString)
                .forEach(schemas::add);
        return root;
    }

    @GET
    @Path(SCHEMA)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode get(@PathParam("schema") String schema) throws ResourceNotFoundException {
        String schemaPath = "/schema/" + (schema.endsWith(".json") ? schema : schema + ".json");
        if (!schemaPaths.contains(schemaPath)) {
            throw new ResourceNotFoundException("schema not found");
        }
        try {
            return cache.get(schemaPath);
        } catch (ExecutionException ex) {
            throw new WebApplicationException(ex);
        }
    }

}
