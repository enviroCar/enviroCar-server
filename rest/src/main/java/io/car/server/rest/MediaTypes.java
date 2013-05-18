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

import com.google.common.collect.ImmutableMap;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class MediaTypes {
    public static final String SCHEMA_ATTRIBUTE = "schema";
    public static final MediaType ROOT_TYPE = withSchema(Schemas.ROOT);
    public static final MediaType USERS_TYPE = withSchema(Schemas.USERS);
    public static final MediaType USER_TYPE = withSchema(Schemas.USER);
    public static final MediaType USER_MODIFY_TYPE = withSchema(Schemas.USER_MODIFY);
    public static final MediaType USER_CREATE_TYPE = withSchema(Schemas.USER_CREATE);
    public static final MediaType USER_REF_TYPE = withSchema(Schemas.USER_REF);
    public static final MediaType GROUPS_TYPE = withSchema(Schemas.GROUPS);
    public static final MediaType GROUP_TYPE = withSchema(Schemas.GROUP);
    public static final MediaType GROUP_MODIFY_TYPE = withSchema(Schemas.GROUP_MODIFY);
    public static final MediaType GROUP_CREATE_TYPE = withSchema(Schemas.GROUP_CREATE);
    public static final MediaType GROUP_REF_TYPE = withSchema(Schemas.GROUP_REF);
    public static final MediaType TRACK_TYPE = withSchema(Schemas.TRACK);
    public static final MediaType TRACKS_TYPE = withSchema(Schemas.TRACKS);
    public static final MediaType TRACK_CREATE_TYPE = withSchema(Schemas.TRACK_CREATE);
    public static final MediaType TRACK_MODIFY_TYPE = withSchema(Schemas.TRACK_MODIFY);
    public static final MediaType MEASUREMENT_TYPE = withSchema(Schemas.MEASUREMENT);
    public static final MediaType MEASUREMENTS_TYPE = withSchema(Schemas.MEASUREMENTS);
    public static final MediaType MEASUREMENT_CREATE_TYPE = withSchema(Schemas.MEASUREMENT_CREATE);
    public static final MediaType MEASUREMENT_MODIFY_TYPE = withSchema(Schemas.MEASUREMENT_MODIFY);
    public static final MediaType SENSOR_TYPE = withSchema(Schemas.SENSOR);
    public static final MediaType SENSORS_TYPE = withSchema(Schemas.SENSORS);
    public static final MediaType SENSOR_CREATE_TYPE = withSchema(Schemas.SENSOR_CREATE);
    public static final MediaType SENSOR_MODIFY_TYPE = withSchema(Schemas.SENSOR_MODIFY);
    public static final MediaType PHENOMENON_TYPE = withSchema(Schemas.PHENOMENON);
    public static final MediaType PHENOMENONS_TYPE = withSchema(Schemas.PHENOMENONS);
    public static final MediaType PHENOMENON_CREATE_TYPE = withSchema(Schemas.PHENOMENON_CREATE);
    public static final MediaType PHENOMENON_MODIFY_TYPE = withSchema(Schemas.PHENOMENON_MODIFY);

    public static MediaType withSchema(String schema) {
        return new MediaType("application", "json", ImmutableMap.of(SCHEMA_ATTRIBUTE, schema));
    }

    private MediaTypes() {
    }
}
