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
package io.car.server.rest.coding;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.statistics.Statistic;
import io.car.server.core.statistics.Statistics;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface CodingFactory {
    EntityEncoder<Group> createGroupEncoder();

    EntityDecoder<Group> createGroupDecoder();

    EntityEncoder<Groups> createGroupsEncoder();

    EntityEncoder<User> createUserEncoder();

    EntityDecoder<User> createUserDecoder();

    EntityEncoder<Users> createUsersEncoder();

    EntityEncoder<Phenomenon> createPhenomenonEncoder();

    EntityDecoder<Phenomenon> createPhenomenonDecoder();

    EntityEncoder<Phenomenons> createPhenomenonsEncoder();

    EntityEncoder<Sensor> createSensorEncoder();

    EntityDecoder<Sensor> createSensorDecoder();

    EntityEncoder<Sensors> createSensorsEncoder();

    EntityEncoder<Track> createTrackEncoder();

    EntityDecoder<Track> createTrackDecoder();

    EntityEncoder<Tracks> createTracksEncoder();

    EntityEncoder<Measurement> createMeasurementEncoder();

    EntityDecoder<Measurement> createMeasurementDecoder();

    EntityEncoder<Measurements> createMeasurementsEncoder();

    EntityEncoder<Statistics> createStatisticsEncoder();

    EntityEncoder<Statistic> createStatisticEncoder();
}
