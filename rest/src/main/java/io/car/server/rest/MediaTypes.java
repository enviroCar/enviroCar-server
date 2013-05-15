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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.rest;

import javax.ws.rs.core.MediaType;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Arne de Wall
 */
public interface MediaTypes {
    String USERS = "application/json; schema=\"http://schema.envirocar.org/users.json\"";
    MediaType USERS_TYPE = MediaType.valueOf(USERS);
    String USER = "application/json; schema=\"http://schema.envirocar.org/user.json\"";
    MediaType USER_TYPE = MediaType.valueOf(USER);
    String USER_MODIFY = "application/json; schema=\"http://schema.envirocar.org/user.modify.json\"";
    MediaType USER_MODIFY_TYPE = MediaType.valueOf(USER_MODIFY);
    String USER_CREATE = "application/json; schema=\"http://schema.envirocar.org/user.create.json\"";
    MediaType USER_CREATE_TYPE = MediaType.valueOf(USER_CREATE);
    String USER_REF = "application/json; schema=\"http://schema.envirocar.org/user.ref.json\"";
    MediaType USER_REF_TYPE = MediaType.valueOf(USER_REF);
    
    String GROUPS = "application/json; schema=\"http://schema.envirocar.org/groups.json\"";
    MediaType GROUPS_TYPE = MediaType.valueOf(GROUPS);
    String GROUP = "application/json; schema=\"http://schema.envirocar.org/group.json\"";
    MediaType GROUP_TYPE = MediaType.valueOf(GROUP);
    String GROUP_MODIFY = "application/json; schema=\"http://schema.envirocar.org/group.modify.json\"";
    MediaType GROUP_MODIFY_TYPE = MediaType.valueOf(GROUP_MODIFY);
    String GROUP_CREATE = "application/json; schema=\"http://schema.envirocar.org/group.create.json\"";
    MediaType GROUP_CREATE_TYPE = MediaType.valueOf(GROUP_CREATE);
    String GROUP_REF = "application/json; schema=\"http://schema.envirocar.org/group.ref.json\"";
    MediaType GROUP_REF_TYPE = MediaType.valueOf(GROUP_REF);
    
    String TRACK = "application/json; schema=\"http://schema.envirocar.org/track.json\"";
    MediaType TRACK_TYPE = MediaType.valueOf(TRACK);
    String TRACKS = "application/json; schema=\"http://schema.envirocar.org/tracks.json\"";
    MediaType TRACKS_TYPE = MediaType.valueOf(TRACKS);
    String TRACK_CREATE = "application/json; schema=\"http://schema.envirocar.org/track.create.json\"";
    MediaType TRACK_CREATE_TYPE = MediaType.valueOf(TRACK_CREATE);
    String TRACK_MODIFY = "application/json; schema=\"http://schema.envirocar.org/track.modify.json\"";
    MediaType TRACK_MODIFY_TYPE = MediaType.valueOf(TRACK_MODIFY);

    String MEASUREMENT = "application/json; schema=\"http://schema.envirocar.org/measurement.json\"";
    MediaType MEASUREMENT_TYPE = MediaType.valueOf(MEASUREMENT);
    String MEASUREMENTS = "application/json; schema=\"http://schema.envirocar.org/measurements.json\"";
	MediaType MEASUREMENTS_TYPE = MediaType.valueOf(MEASUREMENTS);
    String MEASUREMENT_CREATE = "application/json; schema=\"http://schema.envirocar.org/measurement.create.json\"";
	MediaType MEASUREMENT_CREATE_TYPE = MediaType.valueOf(MEASUREMENT_CREATE);
    String MEASUREMENT_MODIFY = "application/json; schema=\"http://schema.envirocar.org/measurement.modify.json\"";
	MediaType MEASUREMENT_MODIFY_TYPE = MediaType.valueOf(MEASUREMENT_MODIFY);
	
    String SENSOR = "application/json; schema=\"http://schema.envirocar.org/sensor.json\"";
    MediaType SENSOR_TYPE = MediaType.valueOf(SENSOR);
    String SENSORS = "application/json; schema=\"http://schema.envirocar.org/sensors.json\"";
    MediaType SENSORS_TYPE = MediaType.valueOf(SENSORS);
    String SENSOR_CREATE = "application/json; schema=\"http://schema.envirocar.org/sensor.create.json\"";
    MediaType SENSOR_CREATE_TYPE = MediaType.valueOf(SENSOR_CREATE);
    String SENSOR_MODIFY = "application/json; schema=\"http://schema.envirocar.org/sensor.modify.json\"";
    MediaType SENSOR_MODIFY_TYPE = MediaType.valueOf(SENSOR_MODIFY);
    String PHENOMENON = "application/json; schema=\"http://schema.envirocar.org/phenomenon.json\"";
    MediaType PHENOMENON_TYPE = MediaType.valueOf(PHENOMENON);
    String PHENOMENONS = "application/json; schema=\"http://schema.envirocar.org/phenomenons.json\"";
    MediaType PHENOMENONS_TYPE = MediaType.valueOf(PHENOMENONS);
    String PHENOMENON_CREATE = "application/json; schema=\"http://schema.envirocar.org/phenomenon.create.json\"";
    MediaType PHENOMENON_CREATE_TYPE = MediaType.valueOf(PHENOMENON_CREATE);
    String PHENOMENON_MODIFY = "application/json; schema=\"http://schema.envirocar.org/phenomenon.modify.json\"";
    MediaType PHENOMENON_MODIFY_TYPE = MediaType.valueOf(PHENOMENON_MODIFY);

}
