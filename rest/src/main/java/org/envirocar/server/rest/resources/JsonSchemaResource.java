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
package org.envirocar.server.rest.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.guice.JsonSchemaModule;
import org.envirocar.server.rest.schema.JsonSchemaUriLoader;
import org.envirocar.server.rest.schema.JsonSchemaUriReplacer;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JsonSchemaResource extends AbstractResource {
    public static final String SCHEMA = "{schema}";
    private final Set<String> schemas;
    private final JsonNodeCreator nodeCreator;
    private final JsonSchemaUriLoader loader;
    private final JsonSchemaUriReplacer replacer;

    @Inject
    public JsonSchemaResource(
            @Named(JsonSchemaModule.SCHEMAS) Set<String> schemaIds,
            JsonNodeFactory nodeCreator,
            JsonSchemaUriLoader loader,
            JsonSchemaUriReplacer replacer) {
        this.schemas = Objects.requireNonNull(schemaIds);
        this.nodeCreator = Objects.requireNonNull(nodeCreator);
        this.loader = Objects.requireNonNull(loader);
        this.replacer = Objects.requireNonNull(replacer);
    }

    @GET
    @Produces(MediaTypes.JSON)
    public JsonNode get() {
        ObjectNode root = nodeCreator.objectNode();
        ArrayNode schemas = root.putArray(JSONConstants.SCHEMA);
        UriBuilder builder = getUriInfo().getRequestUriBuilder().path(SCHEMA);
        this.schemas.stream().map(builder::build).map(URI::toString).forEach(schemas::add);
        return root;
    }

    @GET
    @Path(SCHEMA)
    @Schema(response = "http://json-schema.org/draft-04/schema#")
    @Produces(MediaTypes.JSON)
    public JsonNode get(@PathParam("schema") String schema) throws ResourceNotFoundException {
        String schemaName = schema.endsWith(".json") ? schema : String.format("%s.json", schema);
        try {
            return replacer.replaceSchemaLinks(loader.load(new URI(schemaName)));
        } catch (URISyntaxException ex) {
            throw new BadRequestException(ex);
        } catch (IOException ex) {
            throw new ResourceNotFoundException(String.format("schema %s not found", schemaName), ex);
        }
    }


}
