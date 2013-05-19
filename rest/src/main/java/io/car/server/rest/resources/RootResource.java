/*
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

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.rest.Schemas;
import io.car.server.rest.coding.JSONConstants;
import io.car.server.rest.validation.Schema;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
@Path("/")
public class RootResource extends AbstractResource {
    public static final String USERS = "users";
    public static final String GROUPS = "groups";
    public static final String TRACKS = "tracks";
    public static final String PHENOMENONS = "phenomenons";
    public static final String SENSORS = "sensors";
    public static final String MEASUREMENTS = "measurements";
    @Inject
    private JsonNodeFactory factory;

    @GET
    @Schema(response = Schemas.ROOT)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonNode get() {
        ObjectNode root = factory.objectNode();
        URI users = getUriInfo().getRequestUriBuilder().path(USERS).build();
        URI groups = getUriInfo().getRequestUriBuilder().path(GROUPS).build();
        URI tracks = getUriInfo().getRequestUriBuilder().path(TRACKS).build();
        URI sensors = getUriInfo().getRequestUriBuilder().path(SENSORS).build();
        URI phenomenons = getUriInfo().getRequestUriBuilder()
                .path(PHENOMENONS).build();
        URI measurements = getUriInfo().getRequestUriBuilder()
                .path(MEASUREMENTS).build();
        root.put(JSONConstants.USERS_KEY, users.toString());
        root.put(JSONConstants.GROUPS_KEY, groups.toString());
        root.put(JSONConstants.TRACKS_KEY, tracks.toString());
        root.put(JSONConstants.SENSORS_KEY, sensors.toString());
        root.put(JSONConstants.PHENOMENONS_KEY, phenomenons.toString());
        root.put(JSONConstants.MEASUREMENTS_KEY, measurements.toString());
        return root;
    }

    @Path(USERS)
    public UsersResource users() {
        return getResourceFactory().createUsersResource();
    }

    @Path(GROUPS)
    public GroupsResource groups() {
        return getResourceFactory().createGroupsResource();
    }

    @Path(TRACKS)
    public TracksResource tracks() {
        return getResourceFactory().createTracksResource();
    }

    @Path(PHENOMENONS)
    public PhenomenonsResource phenomenons() {
        return getResourceFactory().createPhenomenonsResource();
    }

    @Path(SENSORS)
    public SensorsResource sensors() {
        return getResourceFactory().createSensorsResource();
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        return getResourceFactory().createMeasurementsResource();
    }
}
