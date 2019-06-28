package org.envirocar.server.rest.mapper;

import javax.ws.rs.core.Response;

public class ErrorMessage {
    private final Response.StatusType code;
    private final String message;
    private final String details;

    public ErrorMessage(Response.StatusType code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public Response.StatusType getStatus() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
