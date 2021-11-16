/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.envirocar.server.core.CountService;
import org.envirocar.server.core.Counts;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;

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
    public static final String BADGES = "badges";
    public static final String RESET_PASSWORD = "resetPassword";
    public static final String CONFIRM = "confirm";
    public static final String PRIVACY_STATEMENTS = "privacyStatements";
    private final JsonNodeFactory factory;
    private final CountService countService;

    @Inject
    public RootResource(JsonNodeFactory factory, CountService countService) {
        this.factory = factory;
        this.countService = countService;
    }

    @GET
    @Schema(response = Schemas.ROOT)
    @Produces({MediaTypes.JSON})
    public JsonNode get() {
        ObjectNode root = factory.objectNode();
        if (getRights().canSeeUsers()) {
            root.put(JSONConstants.USERS_KEY, getUriBuilder().path(USERS).build().toString());
        }
        if (getRights().canSeeGroups()) {
            root.put(JSONConstants.GROUPS_KEY, getUriBuilder().path(GROUPS).build().toString());
        }
        if (getRights().canSeeTracks()) {
            root.put(JSONConstants.TRACKS_KEY, getUriBuilder().path(TRACKS).build().toString());
        }
        if (getRights().canSeeSensors()) {
            root.put(JSONConstants.SENSORS_KEY, getUriBuilder().path(SENSORS).build().toString());
        }
        if (getRights().canSeePhenomenons()) {
            root.put(JSONConstants.PHENOMENONS_KEY, getUriBuilder().path(PHENOMENONS).build().toString());
        }
        if (getRights().canSeeMeasurements()) {
            root.put(JSONConstants.MEASUREMENTS_KEY, getUriBuilder().path(MEASUREMENTS).build().toString());
        }
        if (getRights().canSeeStatistics()) {
            root.put(JSONConstants.STATISTICS_KEY, getUriBuilder().path(STATISTICS).build().toString());
        }
        if (getRights().canSeeAnnouncements()) {
            root.put(JSONConstants.ANNOUNCEMENTS_KEY, getUriBuilder().path(ANNOUNCEMENTS).build().toString());
        }
        if (getRights().canSeeBadges()) {
            root.put(JSONConstants.BADGES_KEY, getUriBuilder().path(BADGES).build().toString());
        }

        root.put(JSONConstants.SCHEMA, getUriBuilder().path(SCHEMA).build().toString());
        root.put(JSONConstants.PRIVACY_STATEMENTS, getUriBuilder().path(PRIVACY_STATEMENTS).build().toString());
        root.put(JSONConstants.TERMS_OF_USE_KEY, getUriBuilder().path(TERMS_OF_USE).build().toString());

        Counts counts = countService.getCounts();

        root.putObject(JSONConstants.COUNTS_KEY)
            .put(JSONConstants.USERS_KEY, counts.getUsers())
            .put(JSONConstants.GROUPS_KEY, counts.getGroups())
            .put(JSONConstants.TRACKS_KEY, counts.getTracks())
            .put(JSONConstants.SENSORS_KEY, counts.getSensors())
            .put(JSONConstants.PHENOMENONS_KEY, counts.getPhenomenons())
            .put(JSONConstants.MEASUREMENTS_KEY, counts.getMeasurements())
            .put(JSONConstants.STATISTICS_KEY, counts.getStatistics())
            .put(JSONConstants.ANNOUNCEMENTS_KEY, counts.getAnnouncements())
            .put(JSONConstants.BADGES_KEY, counts.getBadges())
            .put(JSONConstants.PRIVACY_STATEMENTS, counts.getPrivacyStatements())
            .put(JSONConstants.TERMS_OF_USE_KEY, counts.getTermsOfUses())
            .put(JSONConstants.ACTIVITIES_KEY, counts.getActivities())
            .put(JSONConstants.USERSTATISTIC_KEY, counts.getUserStatistics())
            .put(JSONConstants.FUELINGS, counts.getFuelings());

        return root;
    }

    public UriBuilder getUriBuilder() {
        return getUriInfo().getAbsolutePathBuilder();
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
        return getResourceFactory().createTermsOfUseResource();
    }

    @Path(SCHEMA)
    public JsonSchemaResource schemas() {
        return getResourceFactory().createSchemaResource();
    }

    @Path(ANNOUNCEMENTS)
    public AnnouncementsResource announcements() {
        checkRights(getRights().canSeeAnnouncements());
        return getResourceFactory().createAnnouncementsResource();
    }

    @Path(BADGES)
    public BadgesResource badges() {
        checkRights(getRights().canSeeBadges());
        return getResourceFactory().createBadgesResource();
    }

    @Path(PRIVACY_STATEMENTS)
    public PrivacyStatementsResource privacyStatements() {
        return getResourceFactory().createPrivacyStatementsResource();
    }

    @Path(RESET_PASSWORD)
    public ResetPasswordResource resetPassword() {
        return getResourceFactory().createResetPasswordResource();
    }

    @Path(CONFIRM)
    public ConfirmResource confirm() {
        return getResourceFactory().createConfirmResource();
    }

    @Path("rest")
    public RootResource redirect() {
        return this;
    }
}
