package org.envirocar.server.rest.mapper;

import javax.ws.rs.core.Response;
import java.util.Optional;

public class CustomStatus implements Response.StatusType {
    private final int statusCode;
    private final String reasonPhrase;

    public CustomStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = Optional.ofNullable(reasonPhrase)
                .filter(x -> !x.isEmpty()).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public Response.Status.Family getFamily() {
        switch (this.statusCode / 100) {
            case 1:
                return Response.Status.Family.INFORMATIONAL;
            case 2:
                return Response.Status.Family.SUCCESSFUL;
            case 3:
                return Response.Status.Family.REDIRECTION;
            case 4:
                return Response.Status.Family.CLIENT_ERROR;
            case 5:
                return Response.Status.Family.SERVER_ERROR;
            default:
                return Response.Status.Family.OTHER;
        }
    }

    @Override
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
