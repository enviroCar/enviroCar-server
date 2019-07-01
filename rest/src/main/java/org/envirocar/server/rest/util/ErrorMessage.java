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
package org.envirocar.server.rest.util;

import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.core.Response;

public class ErrorMessage {
    private final Response.StatusType code;
    private final String message;
    private final JsonNode details;
    private Throwable exception;

    public ErrorMessage(Response.StatusType code, String message, JsonNode details, Throwable exception) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.exception = exception;
    }

    public Response.StatusType getStatus() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public JsonNode getDetails() {
        return details;
    }

    public Throwable getThrowable() {
        return exception;
    }
}
