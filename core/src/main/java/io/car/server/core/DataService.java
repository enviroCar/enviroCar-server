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
package io.car.server.core;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.MeasurementNotFoundException;
import io.car.server.core.exception.PhenomenonNotFoundException;
import io.car.server.core.exception.SensorNotFoundException;
import io.car.server.core.exception.TrackNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.filter.MeasurementFilter;
import io.car.server.core.filter.SensorFilter;
import io.car.server.core.filter.TrackFilter;
import io.car.server.core.util.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface DataService {
    Measurement createMeasurement(Measurement measurement);

    Measurement createMeasurement(Track track, Measurement measurement);

    void deleteMeasurement(Measurement measurement);

    Measurement getMeasurement(String id) throws
            MeasurementNotFoundException;

    Measurements getMeasurements(MeasurementFilter request);

    Measurement modifyMeasurement(Measurement measurement, Measurement changes)
            throws ValidationException,
                   IllegalModificationException;

    Track createTrack(Track track) throws
            ValidationException;

    void deleteTrack(Track track);

    Track getTrack(String id) throws
            TrackNotFoundException;

    Track modifyTrack(Track track, Track changes) throws
            ValidationException,
            IllegalModificationException;

    Tracks getTracks(TrackFilter request);

    Phenomenon createPhenomenon(Phenomenon phenomenon);

    Phenomenon getPhenomenonByName(String name) throws
            PhenomenonNotFoundException;

    Phenomenons getPhenomenons(Pagination p);

    Sensor createSensor(Sensor sensor);

    Sensor getSensorByName(String id) throws
            SensorNotFoundException;

    Sensors getSensors(SensorFilter request);
}
