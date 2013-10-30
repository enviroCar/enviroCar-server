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

import org.envirocar.server.core.dao.AnnouncementsDao;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.dao.TrackDao;
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
import org.envirocar.server.core.event.ChangedTrackEvent;
import org.envirocar.server.core.event.CreatedMeasurementEvent;
import org.envirocar.server.core.event.CreatedPhenomenonEvent;
import org.envirocar.server.core.event.CreatedSensorEvent;
import org.envirocar.server.core.event.CreatedTrackEvent;
import org.envirocar.server.core.event.DeletedMeasurementEvent;
import org.envirocar.server.core.event.DeletedTrackEvent;
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
import org.envirocar.server.core.update.EntityUpdater;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.core.validation.EntityValidator;
import org.joda.time.DateTime;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class DataServiceImpl implements DataService {
    private final TrackDao trackDao;
    private final MeasurementDao measurementDao;
    private final SensorDao sensorDao;
    private final PhenomenonDao phenomenonDao;
    private final TermsOfUseDao termsOfUseDao;
    private final EntityValidator<Track> trackValidator;
    private final EntityUpdater<Track> trackUpdater;
    private final EntityUpdater<Measurement> measurementUpdater;
    private final EntityValidator<Measurement> measurementValidator;
    private final EventBus eventBus;
	private final AnnouncementsDao announcementsDao;

    @Inject
    public DataServiceImpl(TrackDao trackDao, MeasurementDao measurementDao,
                           SensorDao sensorDao, PhenomenonDao phenomenonDao,
                           TermsOfUseDao termsOfUseDao,
                           AnnouncementsDao announcementsDao,
                           EntityValidator<Track> trackValidator,
                           EntityUpdater<Track> trackUpdater,
                           EntityUpdater<Measurement> measurementUpdater,
                           EntityValidator<Measurement> measurementValidator,
                           EventBus eventBus) {
        this.trackDao = trackDao;
        this.measurementDao = measurementDao;
        this.sensorDao = sensorDao;
        this.phenomenonDao = phenomenonDao;
        this.trackValidator = trackValidator;
        this.trackUpdater = trackUpdater;
        this.measurementUpdater = measurementUpdater;
        this.measurementValidator = measurementValidator;
        this.eventBus = eventBus;
        this.termsOfUseDao = termsOfUseDao;
        this.announcementsDao = announcementsDao;
    }

    @Override
    public Track modifyTrack(Track track, Track changes)
            throws ValidationException, IllegalModificationException {
        this.trackValidator.validateCreate(track);
        this.trackUpdater.update(changes, track);
        this.trackDao.save(track);
        this.eventBus.post(new ChangedTrackEvent(track.getUser(), track));
        return track;
    }

    @Override
    public Track getTrack(String id) throws TrackNotFoundException {
        Track track = trackDao.getById(id);
        if (track == null) {
            throw new TrackNotFoundException(id);
        }
        return track;
    }

    @Override
    public Track createTrack(Track track) throws ValidationException {
        this.trackValidator.validateCreate(track);
        this.trackDao.create(track);
        this.eventBus.post(new CreatedTrackEvent(track.getUser(), track));
        return track;
    }

    @Override
    public Track createTrack(Track track, List<Measurement> measurements) throws
            ValidationException {
        this.trackValidator.validateCreate(track);
        DateTime begin = null, end = null;
        for (Measurement m : measurements) {
            m.setUser(track.getUser());
            this.measurementValidator.validateCreate(m);
            if (begin == null || m.getTime().isBefore(begin)) {
                begin = m.getTime();
            }
            if (end == null || m.getTime().isAfter(end)) {
                end = m.getTime();
            }
        }
        track.setBegin(begin);
        track.setEnd(end);
        this.trackDao.create(track);
        for (Measurement m : measurements) {
            this.measurementDao.create(m);
        }
        this.eventBus.post(new CreatedTrackEvent(track.getUser(), track));
        return track;
    }

    @Override
    public void deleteTrack(Track track) {
        this.trackDao.delete(track);
        this.eventBus.post(new DeletedTrackEvent(track, track.getUser()));
    }

    @Override
    public Measurement createMeasurement(Measurement m) {
        this.measurementValidator.validateCreate(m);
        this.measurementDao.create(m);
        this.eventBus.post(new CreatedMeasurementEvent(m.getUser(), m));
        return m;
    }

    @Override
    public Measurement createMeasurement(Track track, Measurement m) {
        this.measurementValidator.validateCreate(m);
        m.setTrack(track);
        this.measurementDao.create(m);
        if (!track.hasBegin() || m.getTime().isBefore(track.getBegin())) {
            track.setBegin(m.getTime());
        }
        if (!track.hasEnd() || m.getTime().isAfter(track.getEnd())) {
            track.setEnd(m.getTime());
        }
        this.trackDao.save(track);
        this.eventBus.post(new CreatedMeasurementEvent(m.getUser(), m));
        return m;
    }

    @Override
    public Measurement getMeasurement(String id) throws
            MeasurementNotFoundException {
        Measurement m = this.measurementDao.getById(id);
        if (m == null) {
            throw new MeasurementNotFoundException(id);
        }
        return m;
    }

    @Override
    public void deleteMeasurement(Measurement m) {
        this.measurementDao.delete(m);
        this.eventBus.post(new DeletedMeasurementEvent(m, m.getUser()));
    }

    @Override
    public Phenomenon getPhenomenonByName(String name)
            throws PhenomenonNotFoundException {
        Phenomenon p = this.phenomenonDao.getByName(name);
        if (p == null) {
            throw new PhenomenonNotFoundException(name);
        }
        return p;
    }

    @Override
    public Phenomenon createPhenomenon(Phenomenon phenomenon) {
        this.phenomenonDao.create(phenomenon);
        this.eventBus.post(new CreatedPhenomenonEvent(phenomenon));
        return phenomenon;
    }

    @Override
    public Phenomenons getPhenomenons(Pagination p) {
        return this.phenomenonDao.get(p);
    }

    @Override
    public Sensor getSensorByName(String id) throws SensorNotFoundException {
        Sensor s = this.sensorDao.getByIdentifier(id);
        if (s == null) {
            throw new SensorNotFoundException(id);
        }
        return s;
    }

    @Override
    public Sensor createSensor(Sensor sensor) {
        this.sensorDao.create(sensor);
        this.eventBus.post(new CreatedSensorEvent(sensor));
        return sensor;
    }

    @Override
    public Measurements getMeasurements(MeasurementFilter request) {
        return this.measurementDao.get(request);
    }

    @Override
    public Tracks getTracks(TrackFilter request) {
        return this.trackDao.get(request);
    }

    @Override
    public Sensors getSensors(SensorFilter request) {
        return this.sensorDao.get(request);
    }

	@Override
	public TermsOfUse getTermsOfUse(Pagination p) {
		return this.termsOfUseDao.get(p);
	}

	@Override
	public TermsOfUseInstance getTermsOfUseInstance(String id)
			throws ResourceNotFoundException {
		TermsOfUseInstance result = this.termsOfUseDao.getById(id);
		if (result == null) {
			throw new ResourceNotFoundException(String.format("TermsOfUse with id '%s' not found.", id));
		}
		return result;
	}

	@Override
	public Announcements getAnnouncements(Pagination pagination) {
		return this.announcementsDao.get(pagination);
	}

	@Override
	public Announcement getAnnouncement(String id)
			throws ResourceNotFoundException {
		Announcement result = this.announcementsDao.getById(id);
		if (result == null) {
			throw new ResourceNotFoundException(String.format("Announcement with id '%s' not found.", id));
		}
		return result;
	}
}
