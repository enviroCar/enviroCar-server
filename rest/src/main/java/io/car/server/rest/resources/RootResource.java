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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.Schemas;
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
    public static final String STATISTICS = "statistics";
    @Inject
    private JsonNodeFactory factory;

    @GET
    @Schema(response = Schemas.ROOT)
    @Produces(MediaTypes.ROOT)
    public JsonNode get() {
        ObjectNode root = factory.objectNode();
        URI users = getUriInfo().getAbsolutePathBuilder().path(USERS).build();
        URI groups = getUriInfo().getAbsolutePathBuilder().path(GROUPS).build();
        URI tracks = getUriInfo().getAbsolutePathBuilder().path(TRACKS).build();
        URI sensors = getUriInfo().getAbsolutePathBuilder().path(SENSORS)
                .build();
        URI phenomenons = getUriInfo().getAbsolutePathBuilder()
                .path(PHENOMENONS).build();
        URI measurements = getUriInfo().getAbsolutePathBuilder()
                .path(MEASUREMENTS).build();
        URI statistics = getUriInfo().getAbsolutePathBuilder()
                .path(STATISTICS).build();
        root.put(JSONConstants.USERS_KEY, users.toString());
        root.put(JSONConstants.GROUPS_KEY, groups.toString());
        root.put(JSONConstants.TRACKS_KEY, tracks.toString());
        root.put(JSONConstants.SENSORS_KEY, sensors.toString());
        root.put(JSONConstants.PHENOMENONS_KEY, phenomenons.toString());
        root.put(JSONConstants.MEASUREMENTS_KEY, measurements.toString());
        root.put(JSONConstants.STATISTICS_KEY, statistics.toString());
        return root;
    }

    @Path(USERS)
    public UsersResource users() {
        return getResourceFactory().createUsersResource();
    }

    @Path(GROUPS)
    public GroupsResource groups() {
        return getResourceFactory().createGroupsResource(null);
    }

    @Path(TRACKS)
    public TracksResource tracks() {
        return getResourceFactory().createTracksResource(null);
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
        return getResourceFactory().createMeasurementsResource(null, null);
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        return getResourceFactory().createStatisticsResource(null, null);
    }
}
