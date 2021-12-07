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
package org.envirocar.server.rest.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.jersey.api.NotFoundException;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.guice.YAML;
import org.envirocar.server.rest.schema.JsonSchemaUriReplacer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class ApiDocsResource extends AbstractResource {
    private final JsonSchemaUriReplacer replacer;
    private final ObjectMapper objectMapper;

    @Inject
    public ApiDocsResource(JsonSchemaUriReplacer replacer,
                           @YAML ObjectMapper objectMapper) {
        this.replacer = replacer;
        this.objectMapper = objectMapper;
    }

    private JsonNode loadYaml() throws IOException {
        try (InputStream resourceAsStream = ApiDocsResource.class.getResourceAsStream("/openapi.yaml")) {
            return this.objectMapper.readTree(resourceAsStream);
        }
    }

    private JsonNode adjustEndpoints(JsonNode apiDocs) {
        return this.replacer.replaceSchemaLinks(addServer(apiDocs));
    }

    private JsonNode addServer(JsonNode apiDocs) {
        ArrayNode servers = apiDocs.withArray(JSONConstants.SERVERS);
        servers.removeAll();
        String root = getUriInfo().getBaseUriBuilder().build().toString();
        servers.addObject().put(JSONConstants.URL_KEY, root);
        return apiDocs;
    }

    @GET
    public JsonNode get() throws IOException {
        return adjustEndpoints(loadYaml());
    }

    @GET
    @Produces("text/html")
    public Response ui() throws IOException {
        return ui("index.html");
    }

    @GET
    @Produces({"text/html", "text/css", "application/javascript"})
    @Path("{file}")
    public Response ui(@PathParam("file") String file) {
        if (file == null || file.isEmpty()) {
            return Response.seeOther(getUriInfo().getAbsolutePathBuilder().path("index.html").build()).build();
        }
        String name = Paths.get(file).getFileName().toString();
        InputStream resourceAsStream = ApiDocsResource.class.getResourceAsStream("/swagger-ui/" + name);
        if (resourceAsStream == null) {
            throw new NotFoundException();
        }
        return Response.ok(resourceAsStream).build();

    }
}
