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
