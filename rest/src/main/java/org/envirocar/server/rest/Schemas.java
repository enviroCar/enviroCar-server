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
public class Schemas {
    private static final String PREFIX = "http://schema.envirocar.org/";
    public static final String ROOT = PREFIX + "root.json#";
    public static final String USERS = PREFIX + "users.json#";
    public static final String USER = PREFIX + "user.json#";
    public static final String USER_MODIFY = PREFIX + "user.modify.json#";
    public static final String USER_CREATE = PREFIX + "user.create.json#";
    public static final String USER_REF = PREFIX + "user.ref.json#";
    public static final String GROUPS = PREFIX + "groups.json#";
    public static final String GROUP = PREFIX + "group.json#";
    public static final String GROUP_MODIFY = PREFIX + "group.modify.json#";
    public static final String GROUP_CREATE = PREFIX + "group.create.json#";
    public static final String GROUP_REF = PREFIX + "group.ref.json#";
    public static final String TRACK = PREFIX + "track.json#";
    public static final String TRACKS = PREFIX + "tracks.json#";
    public static final String TRACK_CREATE = PREFIX + "track.create.json#";
    public static final String TRACK_MODIFY = PREFIX + "track.modify.json#";
    public static final String MEASUREMENT = PREFIX + "measurement.json#";
    public static final String MEASUREMENTS = PREFIX + "measurements.json#";
    public static final String MEASUREMENT_CREATE = PREFIX +
                                                    "measurement.create.json#";
    public static final String MEASUREMENT_MODIFY = PREFIX +
                                                    "measurement.modify.json#";
    public static final String SENSOR = PREFIX + "sensor.json#";
    public static final String SENSORS = PREFIX + "sensors.json#";
    public static final String SENSOR_CREATE = PREFIX + "sensor.create.json#";
    public static final String SENSOR_MODIFY = PREFIX + "sensor.modify.json#";
    public static final String PHENOMENON = PREFIX + "phenomenon.json#";
    public static final String PHENOMENONS = PREFIX + "phenomenons.json#";
    public static final String PHENOMENON_CREATE = PREFIX +
                                                   "phenomenon.create.json#";
    public static final String PHENOMENON_MODIFY = PREFIX +
                                                   "phenomenon.modify.json#";
    public static final String STATISTICS = PREFIX + "statistics.json#";
    public static final String STATISTIC = PREFIX + "statistic.json#";
    public static final String ACTIVITY = PREFIX + "activitiy.json#";
    public static final String ACTIVITIES = PREFIX + "activities.json#";
    public static final String TERMS_OF_USE = PREFIX + "terms-of-use.json#";
    public static final String TERMS_OF_USE_INSTANCE = PREFIX + "terms-of-use-instance.json#";
    public static final String ANNOUNCEMENTS = PREFIX + "announcements.json#";
    public static final String ANNOUNCEMENT = PREFIX + "announcement.json#";

    private Schemas() {
    }
}
