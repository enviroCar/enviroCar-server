/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import io.car.server.rest.provider.JSONConstants;

@Path("/")
public class RootResource extends AbstractResource {
    private final UserResource userResource;

    @Inject
    public RootResource(UserResource userResource) {
        this.userResource = userResource;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject get() {
        try {
            return new JSONObject().put(JSONConstants.USERS_KEY,
                                        getUriInfo().getRequestUriBuilder().path("users").build());
        } catch (JSONException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Path("users")
    public UserResource users() {
        return userResource;
    }
}
