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
package org.envirocar.server.core;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.envirocar.server.core.dao.AnnouncementsDao;
import org.envirocar.server.core.dao.BadgesDao;
import org.envirocar.server.core.dao.FuelingDao;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.dao.PrivacyStatementDao;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.dao.TrackDao;
import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.core.entities.Announcements;
import org.envirocar.server.core.entities.Badges;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Fuelings;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Phenomenons;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.PrivacyStatements;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.entities.TermsOfUse;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.event.ChangedTrackEvent;
import org.envirocar.server.core.event.ChangedTrackStatusEvent;
import org.envirocar.server.core.event.CreatedFuelingEvent;
import org.envirocar.server.core.event.CreatedMeasurementEvent;
import org.envirocar.server.core.event.CreatedPhenomenonEvent;
import org.envirocar.server.core.event.CreatedSensorEvent;
import org.envirocar.server.core.event.CreatedTrackEvent;
import org.envirocar.server.core.event.DeletedMeasurementEvent;
import org.envirocar.server.core.event.DeletedTrackEvent;
import org.envirocar.server.core.exception.FuelingNotFoundException;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.MeasurementNotFoundException;
import org.envirocar.server.core.exception.PhenomenonNotFoundException;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.exception.SensorNotFoundException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.FuelingFilter;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.filter.SensorFilter;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.core.update.EntityUpdater;
import org.envirocar.server.core.util.GeometryOperations;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.core.validation.EntityValidator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class DataServiceImpl implements DataService {
    private static final Logger log = LoggerFactory.getLogger(DataServiceImpl.class);
    private final TrackDao trackDao;
    private final MeasurementDao measurementDao;
    private final SensorDao sensorDao;
    private final PhenomenonDao phenomenonDao;
    private final TermsOfUseDao termsOfUseDao;
    private final FuelingDao fuelingDao;
    private final PrivacyStatementDao privacyStatementDao;
    private final EntityValidator<? super Track> trackValidator;
    private final EntityUpdater<? super Track> trackUpdater;
    private final EntityValidator<? super Measurement> measurementValidator;
    private final EntityValidator<? super Fueling> fuelingValidator;
    private final EventBus eventBus;
    private final AnnouncementsDao announcementsDao;
    private final BadgesDao badgesDao;
    private final GeometryOperations geomOps;
    private final CarSimilarityService carSimilarity;

    @Inject
    public DataServiceImpl(TrackDao trackDao,
                           MeasurementDao measurementDao,
                           SensorDao sensorDao,
                           PhenomenonDao phenomenonDao,
                           TermsOfUseDao termsOfUseDao,
                           PrivacyStatementDao privacyStatementDao,
                           AnnouncementsDao announcementsDao,
                           BadgesDao badgesDao,
                           FuelingDao fuelingDao,
                           EntityValidator<Track> trackValidator,
                           EntityUpdater<Track> trackUpdater,
                           EntityValidator<Measurement> measurementValidator,
                           EntityValidator<Fueling> fuelingValidator,
                           EventBus eventBus,
                           GeometryOperations geomOps,
                           CarSimilarityService carSimilarity) {
        this.trackDao = trackDao;
        this.measurementDao = measurementDao;
        this.sensorDao = sensorDao;
        this.phenomenonDao = phenomenonDao;
        this.privacyStatementDao = privacyStatementDao;
        this.trackValidator = trackValidator;
        this.trackUpdater = trackUpdater;
        this.measurementValidator = measurementValidator;
        this.eventBus = eventBus;
        this.termsOfUseDao = termsOfUseDao;
        this.announcementsDao = announcementsDao;
        this.badgesDao = badgesDao;
        this.fuelingValidator = fuelingValidator;
        this.fuelingDao = fuelingDao;
        this.geomOps = geomOps;
        this.carSimilarity = carSimilarity;
    }

    @Override
    public Track modifyTrack(Track track, Track changes)
            throws ValidationException, IllegalModificationException {
        return modifyTrack(track, changes, Collections.emptyList());
    }

    @Override
    public Track modifyTrack(Track track, Track changes, List<Measurement> measurements)
            throws ValidationException, IllegalModificationException {
        this.trackValidator.validateUpdate(changes);

        TrackStatus statusBefore = track.getStatus();
        this.trackUpdater.update(changes, track);
        TrackStatus statusAfter = track.getStatus();

        updateTrackProperties(track, measurements);
        this.trackDao.save(track);
        for (Measurement m : measurements) {
            this.measurementDao.create(m);
            this.eventBus.post(new CreatedMeasurementEvent(m.getUser(), m));
        }
        if (statusAfter == statusBefore) {
            this.eventBus.post(new ChangedTrackEvent(track.getUser(), track));
        } else {
            this.eventBus.post(new ChangedTrackStatusEvent(track.getUser(), track, statusBefore));
        }
        return track;
    }

    private void updateTrackProperties(Track track, List<Measurement> measurements) {
        DateTime begin = track.getBegin();
        DateTime end = track.getEnd();
        for (Measurement m : measurements) {
            m.setUser(track.getUser());
            m.setSensor(track.getSensor());
            m.setTrack(track);
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

        if (!track.hasLength()) {
            track.setLength(this.geomOps.calculateLength(measurements));
        }
    }

    @Override
    public Track getTrack(String id) throws TrackNotFoundException {
        Track track = this.trackDao.getById(id);
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
    public Track createTrack(Track track, List<Measurement> measurements)
            throws ValidationException {
        this.trackValidator.validateCreate(track);
        updateTrackProperties(track, measurements);

        this.trackDao.create(track);
        for (Measurement m : measurements) {
            this.measurementDao.create(m);
        }
        this.eventBus.post(new CreatedTrackEvent(track.getUser(), track));
        return track;
    }

    @Override
    public void deleteTrack(Track track) {
        Measurements measurements = getMeasurements(new MeasurementFilter(track));
        this.eventBus.post(new DeletedTrackEvent(track, track.getUser(), measurements));
        this.trackDao.delete(track);
    }

    @Override
    public Measurement createMeasurement(Measurement m) {
        this.measurementValidator.validateCreate(m);
        this.measurementDao.create(m);
        this.eventBus.post(new CreatedMeasurementEvent(m.getUser(), m));
        return m;
    }

    @Override
    public Measurement createMeasurement(Track track, Measurement measurement) {
        this.measurementValidator.validateCreate(measurement);
        measurement.setTrack(track);
        Measurement m = this.measurementDao.create(measurement);
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
        Phenomenon p = this.phenomenonDao.create(phenomenon);
        this.eventBus.post(new CreatedPhenomenonEvent(p));
        return p;
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
        try {
            return this.carSimilarity.resolveEquivalent(sensor);
        } catch (ResourceNotFoundException ex) {
            log.info("No equivalent sensor found, creating new");
        }

        Sensor s = this.sensorDao.create(sensor);
        this.eventBus.post(new CreatedSensorEvent(s));
        return s;
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
        return this.termsOfUseDao.getById(id)
                                 .orElseThrow(() -> {
                                     String message = String.format("TermsOfUse with id '%s' not found.", id);
                                     return new ResourceNotFoundException(message);
                                 });
    }

    @Override
    public PrivacyStatements getPrivacyStatements(Pagination pagination) {
        return this.privacyStatementDao.get(pagination);
    }

    @Override
    public PrivacyStatement getPrivacyStatement(String id) throws ResourceNotFoundException {
        return this.privacyStatementDao.getById(id)
                                       .orElseThrow(() -> {
                                           String message = String.format("PrivacyStatement with id '%s' not found.", id);
                                           return new ResourceNotFoundException(message);
                                       });
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

    @Override
    public Badges getBadges(Pagination pagination) {
        return this.badgesDao.get(pagination);
    }

    @Override
    public Fueling createFueling(Fueling fueling) {
        this.fuelingValidator.validateCreate(fueling);
        Fueling f = this.fuelingDao.create(fueling);
        this.eventBus.post(new CreatedFuelingEvent(f.getUser(), f));
        return f;
    }

    @Override
    public Fuelings getFuelings(FuelingFilter filter) {
        return this.fuelingDao.get(filter);
    }

    @Override
    public Fueling getFueling(User user, String id)
            throws FuelingNotFoundException {
        Fueling m = this.fuelingDao.getById(id);
        if (m == null || !m.getUser().getName().equals(user.getName())) {
            throw new FuelingNotFoundException(id);
        }
        return m;
    }

    @Override
    public void deleteFueling(Fueling fueling) {
        this.fuelingDao.delete(fueling);
    }
}
