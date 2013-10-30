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
package org.envirocar.server.rest;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface JSONConstants {
    String BADGES = "badges";
    String INSTANCE_KEY = "instance";
    String FRIEND_ACTIVITIES = "friendActivities";
    String GROUP_KEY = "group";
    String OTHER_KEY = "other";
    String TRACK_KEY = "track";
    String MEASUREMENT_KEY = "measurement";
    String TYPE_KEY = "type";
    String ACTIVITIES_KEY = "activities";
    String UNIT_KEY = "unit";
    String MIN_KEY = "min";
    String AVG_KEY = "avg";
    String MAX_KEY = "max";
    String STATISTICS_KEY = "statistics";
    String STATISTIC_KEY = "statistic";
    String ERROR_KEY = "error";
    String BBOX_KEY = "bbox";
    String CREATED_KEY = "created";
    String DESCRIPTION_KEY = "description";
    String ERRORS_KEY = "errors";
    String FRIENDS_KEY = "friends";
    String GEOMETRY_KEY = "geometry";
    String GROUPS_KEY = "groups";
    String HREF_KEY = "href";
    String IDENTIFIER_KEY = "id";
    String MAIL_KEY = "mail";
    String MEASUREMENTS_KEY = "measurements";
    String MEMBERS_KEY = "members";
    String MODIFIED_KEY = "modified";
    String NAME_KEY = "name";
    String OWNER_KEY = "owner";
    String PHENOMENON_KEY = "phenomenon";
    String PHENOMENONS_KEY = "phenomenons";
    String SENSOR_KEY = "sensor";
    String SENSORS_KEY = "sensors";
    String TIME_KEY = "time";
    String TOKEN_KEY = "token";
    String TRACKS_KEY = "tracks";
    String USER_KEY = "user";
    String USERS_KEY = "users";
    String VALUE_KEY = "value";
    String AVATAR_KEY = "avatar";
    String CAR_KEY = "car";
    String PROPERTIES_KEY = "properties";
    String LAST_NAME_KEY = "lastName";
    String FIRST_NAME_KEY = "firstName";
    String COUNTRY_KEY = "country";
    String LOCATION_KEY = "location";
    String ABOUT_ME_KEY = "aboutMe";
    String URL_KEY = "url";
    String DAY_OF_BIRTH_KEY = "dayOfBirth";
    String GENDER_KEY = "gender";
    String LANGUAGE_KEY = "language";
    String MALE = "m";
    String FEMALE = "f";
    String TERMS_OF_USE_KEY = "termsOfUse";

    /**
     * @deprecated use {@link #TOU_VERSION_KEY} instead. kept for backwards
     * compatibility
     */
    @Deprecated
    String ACCEPTED_TERMS_OF_USE_VERSION_KEY = "acceptedTermsOfUseVersion";

    String APP_VERSION_KEY = "appVersion";
    String OBD_DEVICE_KEY = "obdDevice";
    String TOU_VERSION_KEY = "touVersion";
    String SCHEMA = "schema";
    String ANNOUNCEMENTS_KEY = "announcements";
    String ISSUED_DATE = "issuedDate";
    String CONTENTS = "contents";
    String VERSIONS = "versions";
    String CONTENT = "content";
    String PRIORITY  = "priority";
    String CATEGORY = "category";
}
