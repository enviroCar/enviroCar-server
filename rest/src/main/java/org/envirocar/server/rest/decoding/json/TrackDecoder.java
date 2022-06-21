/*
 * Copyright (C) 2013-2022 The enviroCar project
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
package org.envirocar.server.rest.decoding.json;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.TrackWithMeasurments;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class TrackDecoder extends AbstractJSONEntityDecoder<Track> {
    private final JSONEntityDecoder<Measurement> measurementDecoder;
    private final SensorDao sensorDao;
    private final ContextKnowledgeFactory contextKnowledgeFactory;

    @Inject
    public TrackDecoder(JSONEntityDecoder<Measurement> measurementDecoder,
                        SensorDao sensorDao,
                        MeasurementDao measurementDao, ContextKnowledgeFactory ckFac) {
        super(Track.class);
        this.measurementDecoder = measurementDecoder;
        this.sensorDao = sensorDao;
        this.contextKnowledgeFactory = ckFac;
    }

    @Override
    public Track decode(JsonNode node, MediaType mediaType) {
        Sensor trackSensor = null;
        Track track = getEntityFactory().createTrack();
        if (node.has(GeoJSONConstants.PROPERTIES_KEY)) {
            JsonNode p = node.path(GeoJSONConstants.PROPERTIES_KEY);
            if (p.has(JSONConstants.SENSOR_KEY)) {
                trackSensor = this.sensorDao.getByIdentifier(p.get(JSONConstants.SENSOR_KEY).asText());
                track.setSensor(trackSensor);
            }
            track.setName(p.path(JSONConstants.NAME_KEY).textValue());
            track.setDescription(p.path(JSONConstants.DESCRIPTION_KEY).textValue());
            track.setAppVersion(p.path(JSONConstants.APP_VERSION_KEY).textValue());
            track.setObdDevice(p.path(JSONConstants.OBD_DEVICE_KEY).textValue());
            track.setTouVersion(p.path(JSONConstants.TOU_VERSION_KEY).textValue());
            track.setLength(p.path(JSONConstants.LENGTH_KEY).asDouble());
            track.setStatus(p.path(JSONConstants.STATUS_KEY).textValue());
            track.setMeasurementProfile(p.path(JSONConstants.MEASUREMENT_PROFILE).textValue());
        }

        if (!node.path(GeoJSONConstants.FEATURES_KEY).isMissingNode()) {
            JsonNode ms = node.path(GeoJSONConstants.FEATURES_KEY);
            TrackWithMeasurments twm = new TrackWithMeasurments(track);
            
            ContextKnowledge knowledge = this.contextKnowledgeFactory.create();
            
            for (int i = 0; i < ms.size(); i++) {
                Measurement m = this.measurementDecoder.decode(ms.get(i), mediaType, knowledge);
                m.setTrack(track);
                if (m.getSensor() == null) {
                    m.setSensor(trackSensor);
                }
                twm.addMeasurement(m);
                
            }
            return twm;
        }
        return track;
    }
}
