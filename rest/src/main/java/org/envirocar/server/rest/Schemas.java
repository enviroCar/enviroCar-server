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
package org.envirocar.server.rest;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Schemas {
    public static final String ACTIVITIES = "activities";
    public static final String ACTIVITY = "activity";
    public static final String ANNOUNCEMENT = "announcement";
    public static final String ANNOUNCEMENTS = "announcements";
    public static final String BADGE = "badge";
    public static final String BADGES = "badges";
    public static final String EXCEPTION = "exception";
    public static final String FUELING_CREATE = "fueling-create";
    public static final String FUELING = "fueling";
    public static final String FUELINGS = "fuelings";
    public static final String GEOMETRY_COLLECTION = "geometrycollection";
    public static final String GEOMETRY = "geometry";
    public static final String GROUP_CREATE = "group-create";
    public static final String GROUP = "group";
    public static final String GROUP_MODIFY = "group-modify";
    public static final String GROUPS = "groups";
    public static final String LINESTRING = "linestring";
    public static final String MAIL_ADDRESS = "mail-address";
    public static final String MEASUREMENT_CREATE = "measurement-create";
    public static final String MEASUREMENT = "measurement";
    public static final String MEASUREMENTS = "measurements";
    public static final String MULTILINESTRING = "multilinestring";
    public static final String MULTIPOINT = "multipoint";
    public static final String MULTIPOLYGON = "multipolygon";
    public static final String OPTIONAL_USER_PROPERTIES = "optional-user-properties";
    public static final String PASSWORD_RESET_REQUEST = "passwordResetRequest";
    public static final String PASSWORD_RESET_VERIFICATION = "passwordResetVerification";
    public static final String PHENOMENON_CREATE = "phenomenon-create";
    public static final String PHENOMENON = "phenomenon";
    public static final String PHENOMENON_MODIFY = "phenomenon-modify";
    public static final String PHENOMENONS = "phenomenons";
    public static final String POINT = "point";
    public static final String POLYGON = "polygon";
    public static final String PRIVACY_STATEMENT = "privacy-statement";
    public static final String PRIVACY_STATEMENTS = "privacy-statements";
    public static final String ROOT = "root";
    public static final String SCHEMAS = "schemas";
    public static final String SENSOR_CREATE = "sensor-create";
    public static final String SENSOR = "sensor";
    public static final String SENSORS = "sensors";
    public static final String STATISTIC = "statistic";
    public static final String STATISTICS = "statistics";
    public static final String TERMS_OF_USE_INSTANCE = "terms-of-use-instance";
    public static final String TERMS_OF_USE = "terms-of-use";
    public static final String TRACK_CREATE = "track-create";
    public static final String TRACK = "track";
    public static final String TRACK_MODIFY = "track-modify";
    public static final String TRACK_REFERENCE = "track-reference";
    public static final String TRACKS = "tracks";
    public static final String TRACK_STATUS = "track-status";
    public static final String USER_CREATE = "user-create";
    public static final String USER = "user";
    public static final String USER_MODIFY = "user-modify";
    public static final String USERNAME = "username";
    public static final String USER_REF = "user-ref";
    public static final String USERS = "users";
    public static final String USER_STATISTIC = "userStatistic";

    public static final Set<String> ALL_SCHEMAS = ImmutableSet.of(
            ACTIVITIES,
            ACTIVITY,
            ANNOUNCEMENT,
            ANNOUNCEMENTS,
            BADGE,
            BADGES,
            EXCEPTION,
            FUELING_CREATE,
            FUELING,
            FUELINGS,
            GEOMETRY_COLLECTION,
            GEOMETRY,
            GROUP_CREATE,
            GROUP,
            GROUP_MODIFY,
            GROUPS,
            LINESTRING,
            MAIL_ADDRESS,
            MEASUREMENT_CREATE,
            MEASUREMENT,
            MEASUREMENTS,
            MULTILINESTRING,
            MULTIPOINT,
            MULTIPOLYGON,
            OPTIONAL_USER_PROPERTIES,
            PASSWORD_RESET_REQUEST,
            PASSWORD_RESET_VERIFICATION,
            PHENOMENON_CREATE,
            PHENOMENON,
            PHENOMENON_MODIFY,
            PHENOMENONS,
            POINT,
            POLYGON,
            PRIVACY_STATEMENT,
            PRIVACY_STATEMENTS,
            ROOT,
            SCHEMAS,
            SENSOR_CREATE,
            SENSOR,
            SENSORS,
            STATISTIC,
            STATISTICS,
            TERMS_OF_USE_INSTANCE,
            TERMS_OF_USE,
            TRACK_CREATE,
            TRACK,
            TRACK_MODIFY,
            TRACK_REFERENCE,
            TRACKS,
            TRACK_STATUS,
            USER_CREATE,
            USER,
            USER_MODIFY,
            USERNAME,
            USER_REF,
            USERS,
            USER_STATISTIC);

    private Schemas() {
    }
}
