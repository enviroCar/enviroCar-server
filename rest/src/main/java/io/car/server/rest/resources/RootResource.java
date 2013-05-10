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
package io.car.server.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import io.car.server.rest.AbstractResource;
import io.car.server.rest.provider.JSONConstants;

/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
@Path("/")
public class RootResource extends AbstractResource {
    public static final String USERS_PATH = "users";
    public static final String GROUPS_PATH = "groups";
    public static final String TRACKS_PATH = "tracks";
    public static final String PHENOMENONS_PATH = "phenomenons";
    public static final String SENSORS_PATH = "sensors";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject get() {
        try {
            return new JSONObject()
                    .put(JSONConstants.USERS_KEY,
                         getUriInfo().getRequestUriBuilder().path(USERS_PATH).build())
                    .put(JSONConstants.GROUPS_KEY,
                         getUriInfo().getRequestUriBuilder().path(GROUPS_PATH).build())
                    .put(JSONConstants.TRACKS_KEY,
                         getUriInfo().getRequestUriBuilder().path(TRACKS_PATH).build())
                    .put(JSONConstants.SENSORS_KEY,
                         getUriInfo().getRequestUriBuilder().path(SENSORS_PATH).build())
                    .put(JSONConstants.PHENOMENONS_KEY,
                         getUriInfo().getRequestUriBuilder().path(PHENOMENONS_PATH).build());
        } catch (JSONException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Path(USERS_PATH)
    public UsersResource users() {
        return getResourceFactory().createUsersResource();
    }

    @Path(GROUPS_PATH)
    public GroupsResource groups() {
        return getResourceFactory().createGroupsResource();
    }
    
    @Path(TRACKS_PATH)
    public TracksResource tracks() {
    	return getResourceFactory().createTracksResource();
    }

    @Path(PHENOMENONS_PATH)
    public PhenomenonsResource phenomenons() {
        return getResourceFactory().createPhenomenonsResource();
    }

    @Path(SENSORS_PATH)
    public SensorsResource sensors() {
        return getResourceFactory().createSensorsResource();
    }
}
