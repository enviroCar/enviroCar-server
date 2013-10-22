/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.envirocar.server.rest.resources;


import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.guice.JSONSchemaFactoryProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;

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
    public JsonNode get() throws TrackNotFoundException {
        ObjectNode root = nodeFactory.objectNode();
        ArrayNode schemas = root.putArray(JSONConstants.SCHEMA);
        UriBuilder builder
                = getUriInfo().getRequestUriBuilder().path("{schema}");
        for (String path : this.schemaPaths) {
            schemas.add(builder.build(path.replaceFirst("/schema/", "")).toString());
        }
        return root;
    }

    @GET
    @Path(SCHEMA)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode get(@PathParam("schema") String schema) throws ResourceNotFoundException {
        if (!schema.endsWith(".json")) {
            String path = getUriInfo() .getPath() + ".json";
            URI uri = getUriInfo().getBaseUriBuilder().path(path).build();
            throw new WebApplicationException(Response.temporaryRedirect(uri).build());
        }
        String schemaPath = "/schema/" + schema;
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
