/*
 * Copyright (C) 2013-2018 The enviroCar project
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
import org.envirocar.server.core.dao.*;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.core.event.CreatedMeasurementEvent;
import org.envirocar.server.core.event.CreatedPhenomenonEvent;
import org.envirocar.server.core.event.CreatedSensorEvent;
import org.envirocar.server.core.event.CreatedTrackEvent;
import org.envirocar.server.core.exception.*;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.filter.SensorFilter;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.core.util.GeometryOperations;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.core.validation.EntityValidator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final PrivacyStatementDao privacyStatementDao;
    private final EntityValidator<Track> trackValidator;
    private final EntityValidator<Measurement> measurementValidator;
    private final EventBus eventBus;
    private final GeometryOperations geomOps;
    private final CarSimilarityService carSimilarity;

    @Inject
    public DataServiceImpl(TrackDao trackDao, MeasurementDao measurementDao,
                           SensorDao sensorDao, PhenomenonDao phenomenonDao,
                           TermsOfUseDao termsOfUseDao,
                           PrivacyStatementDao privacyStatementDao,
                           EntityValidator<Track> trackValidator,
                           EntityValidator<Measurement> measurementValidator,
                           EventBus eventBus,
                           GeometryOperations geomOps,
                           CarSimilarityService carSimilarity) {
        this.trackDao = trackDao;
        this.measurementDao = measurementDao;
        this.sensorDao = sensorDao;
        this.phenomenonDao = phenomenonDao;
        this.privacyStatementDao = privacyStatementDao;
        this.trackValidator = trackValidator;
        this.measurementValidator = measurementValidator;
        this.eventBus = eventBus;
        this.termsOfUseDao = termsOfUseDao;
        this.geomOps = geomOps;
        this.carSimilarity = carSimilarity;
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

        if (!track.hasTouVersion()) {
            TermsOfUseInstance latest = termsOfUseDao.getLatest();
            if (latest != null) {
                track.setTouVersion(latest.getIssuedDate());
            }
        }

        this.trackDao.create(track);
        this.eventBus.post(new CreatedTrackEvent(track));
        return track;
    }

    @Override
    public Track createTrack(Track track, List<Measurement> measurements) throws
            ValidationException {
        this.trackValidator.validateCreate(track);
        DateTime begin = null, end = null;
        for (Measurement m : measurements) {
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

        if (!track.hasTouVersion()) {
            TermsOfUseInstance latest = termsOfUseDao.getLatest();
            if (latest != null) {
                track.setTouVersion(latest.getIssuedDate());
            }
        }

        if (!track.hasLength()) {
            track.setLength(geomOps.calculateLength(measurements));
        }

        this.trackDao.create(track);
        for (Measurement m : measurements) {
            this.measurementDao.create(m);
        }
        this.eventBus.post(new CreatedTrackEvent(track));
        return track;
    }

    @Override
    public Measurement createMeasurement(Measurement m) {
        this.measurementValidator.validateCreate(m);
        this.measurementDao.create(m);
        this.eventBus.post(new CreatedMeasurementEvent(m));
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
        this.eventBus.post(new CreatedMeasurementEvent(m));
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
        TermsOfUseInstance result = this.termsOfUseDao.getById(id);
        if (result == null) {
            throw new ResourceNotFoundException(String.format("TermsOfUse with id '%s' not found.", id));
        }
        return result;
    }

    @Override
    public PrivacyStatements getPrivacyStatements(Pagination pagination) {
        return this.privacyStatementDao.get(pagination);
    }

    @Override
    public PrivacyStatement getPrivacyStatement(String id) throws ResourceNotFoundException {
        PrivacyStatement result = this.privacyStatementDao.getById(id);
        if (result == null) {
            throw new ResourceNotFoundException(String.format("PrivacyStatement with id '%s' not found.", id));
        }
        return result;
    }
}
