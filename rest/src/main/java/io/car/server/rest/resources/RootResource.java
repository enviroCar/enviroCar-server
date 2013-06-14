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
        if (getAccessRights().canSeeUsers()) {
            root.put(JSONConstants.USERS_KEY,
                     getUriInfo().getAbsolutePathBuilder()
                    .path(USERS).build().toString());
        }
        if (getAccessRights().canSeeGroups()) {
            root.put(JSONConstants.GROUPS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(GROUPS).build().toString());
        }
        if (getAccessRights().canSeeTracks()) {
            root.put(JSONConstants.TRACKS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(TRACKS).build().toString());
        }
        if (getAccessRights().canSeeSensors()) {
            root.put(JSONConstants.SENSORS_KEY, getUriInfo()
                    .getAbsolutePathBuilder().path(SENSORS)
                    .build().toString());
        }
        if (getAccessRights().canSeePhenomenons()) {
            root.put(JSONConstants.PHENOMENONS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(PHENOMENONS).build().toString());
        }
        if (getAccessRights().canSeeMeasurements()) {
            root.put(JSONConstants.MEASUREMENTS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(MEASUREMENTS).build().toString());
        }
        if (getAccessRights().canSeeStatistics()) {
            root.put(JSONConstants.STATISTICS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(STATISTICS).build().toString());
        }
        return root;
    }

    @Path(USERS)
    public UsersResource users() {
        checkRights(getAccessRights().canSeeUsers());
        return getResourceFactory().createUsersResource();
    }

    @Path(GROUPS)
    public GroupsResource groups() {
        checkRights(getAccessRights().canSeeGroups());
        return getResourceFactory().createGroupsResource(null);
    }

    @Path(TRACKS)
    public TracksResource tracks() {
        checkRights(getAccessRights().canSeeTracks());
        return getResourceFactory().createTracksResource(null);
    }

    @Path(PHENOMENONS)
    public PhenomenonsResource phenomenons() {
        checkRights(getAccessRights().canSeePhenomenons());
        return getResourceFactory().createPhenomenonsResource();
    }

    @Path(SENSORS)
    public SensorsResource sensors() {
        checkRights(getAccessRights().canSeeSensors());
        return getResourceFactory().createSensorsResource();
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        checkRights(getAccessRights().canSeeMeasurements());
        return getResourceFactory().createMeasurementsResource(null, null);
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        checkRights(getAccessRights().canSeeStatistics());
        return getResourceFactory().createStatisticsResource(null, null);
    }
}
