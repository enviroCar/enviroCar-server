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
package io.car.server.rest;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public interface Schemas {
    String ROOT = "http://schema.envirocar.org/root.json#";
    String USERS = "http://schema.envirocar.org/users.json#";
    String USER = "http://schema.envirocar.org/user.json#";
    String USER_MODIFY = "http://schema.envirocar.org/user.modify.json#";
    String USER_CREATE = "http://schema.envirocar.org/user.create.json#";
    String USER_REF = "http://schema.envirocar.org/user.ref.json#";
    String GROUPS = "http://schema.envirocar.org/groups.json#";
    String GROUP = "http://schema.envirocar.org/group.json#";
    String GROUP_MODIFY = "http://schema.envirocar.org/group.modify.json#";
    String GROUP_CREATE = "http://schema.envirocar.org/group.create.json#";
    String GROUP_REF = "http://schema.envirocar.org/group.ref.json#";
    String TRACK = "http://schema.envirocar.org/track.json#";
    String TRACKS = "http://schema.envirocar.org/tracks.json#";
    String TRACK_CREATE = "http://schema.envirocar.org/track.create.json#";
    String TRACK_MODIFY = "http://schema.envirocar.org/track.modify.json#";
    String MEASUREMENT = "http://schema.envirocar.org/measurement.json#";
    String MEASUREMENTS = "http://schema.envirocar.org/measurements.json#";
    String MEASUREMENT_CREATE = "http://schema.envirocar.org/measurement.create.json#";
    String MEASUREMENT_MODIFY = "http://schema.envirocar.org/measurement.modify.json#";
    String SENSOR = "http://schema.envirocar.org/sensor.json#";
    String SENSORS = "http://schema.envirocar.org/sensors.json#";
    String SENSOR_CREATE = "http://schema.envirocar.org/sensor.create.json#";
    String SENSOR_MODIFY = "http://schema.envirocar.org/sensor.modify.json#";
    String PHENOMENON = "http://schema.envirocar.org/phenomenon.json#";
    String PHENOMENONS = "http://schema.envirocar.org/phenomenons.json#";
    String PHENOMENON_CREATE = "http://schema.envirocar.org/phenomenon.create.json#";
    String PHENOMENON_MODIFY = "http://schema.envirocar.org/phenomenon.modify.json#";
}
