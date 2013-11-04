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
package org.envirocar.server.core;

import java.util.List;

import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.core.entities.Announcements;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Phenomenons;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.entities.TermsOfUse;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.MeasurementNotFoundException;
import org.envirocar.server.core.exception.PhenomenonNotFoundException;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.exception.SensorNotFoundException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.filter.SensorFilter;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.core.util.Pagination;

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

    Track createTrack(Track track) throws
            ValidationException;

    Track createTrack(Track track, List<Measurement> measurements) throws
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

	TermsOfUse getTermsOfUse(Pagination pagination);

	TermsOfUseInstance getTermsOfUseInstance(String id) throws ResourceNotFoundException;
	
	Announcements getAnnouncements(Pagination pagination);

	Announcement getAnnouncement(String id) throws ResourceNotFoundException;
}
