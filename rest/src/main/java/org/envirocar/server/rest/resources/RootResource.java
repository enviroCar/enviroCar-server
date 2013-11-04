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
package org.envirocar.server.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.validation.Schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
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
	public static final String TERMS_OF_USE = "termsOfUse";
    public static final String SCHEMA = "schema";
    public static final String ANNOUNCEMENTS = "announcements";
    
    @Inject
    private JsonNodeFactory factory;

    @GET
    @Schema(response = Schemas.ROOT)
    @Produces({ MediaTypes.ROOT })
    public JsonNode get() {
        ObjectNode root = factory.objectNode();
        if (getRights().canSeeUsers()) {
            root.put(JSONConstants.USERS_KEY,
                     getUriInfo().getAbsolutePathBuilder()
                    .path(USERS).build().toString());
        }
        if (getRights().canSeeGroups()) {
            root.put(JSONConstants.GROUPS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(GROUPS).build().toString());
        }
        if (getRights().canSeeTracks()) {
            root.put(JSONConstants.TRACKS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(TRACKS).build().toString());
        }
        if (getRights().canSeeSensors()) {
            root.put(JSONConstants.SENSORS_KEY, getUriInfo()
                    .getAbsolutePathBuilder().path(SENSORS)
                    .build().toString());
        }
        if (getRights().canSeePhenomenons()) {
            root.put(JSONConstants.PHENOMENONS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(PHENOMENONS).build().toString());
        }
        if (getRights().canSeeMeasurements()) {
            root.put(JSONConstants.MEASUREMENTS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(MEASUREMENTS).build().toString());
        }
        if (getRights().canSeeStatistics()) {
            root.put(JSONConstants.STATISTICS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(STATISTICS).build().toString());
        }
        if (getRights().canSeeTermsOfUse()) {
            root.put(JSONConstants.TERMS_OF_USE_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(TERMS_OF_USE).build().toString());
        }
        if (getRights().canSeeSchema()) {
            root.put(JSONConstants.SCHEMA, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(SCHEMA).build().toString());
        }
        if (getRights().canSeeAnnouncements()) {
            root.put(JSONConstants.ANNOUNCEMENTS_KEY, getUriInfo()
                    .getAbsolutePathBuilder()
                    .path(ANNOUNCEMENTS).build().toString());
        }
        return root;
    }

    @Path(USERS)
    public UsersResource users() {
        return getResourceFactory().createUsersResource();
    }

    @Path(GROUPS)
    public GroupsResource groups() {
        checkRights(getRights().canSeeGroups());
        return getResourceFactory().createGroupsResource(null);
    }

    @Path(TRACKS)
    public TracksResource tracks() {
        checkRights(getRights().canSeeTracks());
        return getResourceFactory().createTracksResource(null);
    }

    @Path(PHENOMENONS)
    public PhenomenonsResource phenomenons() {
        checkRights(getRights().canSeePhenomenons());
        return getResourceFactory().createPhenomenonsResource();
    }

    @Path(SENSORS)
    public SensorsResource sensors() {
        checkRights(getRights().canSeeSensors());
        return getResourceFactory().createSensorsResource();
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        checkRights(getRights().canSeeMeasurements());
        return getResourceFactory().createMeasurementsResource(null, null);
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        checkRights(getRights().canSeeStatistics());
        return getResourceFactory().createStatisticsResource();
    }
    
    @Path(TERMS_OF_USE)
    public TermsOfUseResource termsOfUse() {
        checkRights(getRights().canSeeTermsOfUse());
        return getResourceFactory().createTermsOfUseResource();
    }

    @Path(SCHEMA)
    public JSONSchemaResource schemas() {
        checkRights(getRights().canSeeSchema());
        return getResourceFactory().createSchemaResource();
    }
    
    @Path(ANNOUNCEMENTS)
    public AnnouncementsResource announcements() {
    	checkRights(getRights().canSeeAnnouncements());
    	return getResourceFactory().createAnnouncementsResource();
    }

    @Path("rest")
    public RootResource redirect() {
        return this;
    }
}
