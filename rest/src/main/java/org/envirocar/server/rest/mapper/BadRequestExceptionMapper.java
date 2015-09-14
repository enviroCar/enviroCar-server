/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.rest.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.rest.JSONConstants;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    private static JsonNodeFactory factory = new JsonNodeFactory(false);

    @Override
    public Response toResponse(BadRequestException exception) {
        return Response.status(Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(createJson(exception.getMessage())).build();
    }

    private JsonNode createJson(String message) {
        ObjectNode error = factory.objectNode();
        ArrayNode errors = error.putArray(JSONConstants.ERRORS_KEY);
        errors.add(factory.textNode(message));
        return error;
    }
}
