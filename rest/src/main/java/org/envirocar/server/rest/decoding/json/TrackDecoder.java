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
package org.envirocar.server.rest.decoding.json;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.SensorDao;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.TrackWithMeasurments;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class TrackDecoder extends AbstractJSONEntityDecoder<Track> {
    private JSONEntityDecoder<Measurement> measurementDecoder;
    private SensorDao sensorDao;

    @Inject
    public TrackDecoder(JSONEntityDecoder<Measurement> measurementDecoder,
                        SensorDao sensorDao,
                        MeasurementDao measurementDao) {
        super(Track.class);
        this.measurementDecoder = measurementDecoder;
        this.sensorDao = sensorDao;
    }

    @Override
    public Track decode(JsonNode j, MediaType mediaType) {
        Sensor trackSensor = null;
        Track track = getEntityFactory().createTrack();
        if (j.has(GeoJSONConstants.PROPERTIES_KEY)) {
            JsonNode p = j.path(GeoJSONConstants.PROPERTIES_KEY);
            if (p.has(JSONConstants.SENSOR_KEY)) {
                trackSensor = sensorDao.getByIdentifier(p
                        .get(JSONConstants.SENSOR_KEY).asText());
                track.setSensor(trackSensor);
            }
            track.setName(p.path(JSONConstants.NAME_KEY).textValue());
            track.setDescription(p.path(JSONConstants.DESCRIPTION_KEY)
                    .textValue());
            track.setAppVersion(p.path(JSONConstants.APP_VERSION_KEY)
                    .textValue());
            track.setObdDevice(p.path(JSONConstants.OBD_DEVICE_KEY)
                    .textValue());
            track.setTouVersion(p.path(JSONConstants.TOU_VERSION_KEY)
                    .textValue());
        }

        if (!j.path(GeoJSONConstants.FEATURES_KEY).isMissingNode()) {
            JsonNode ms = j.path(GeoJSONConstants.FEATURES_KEY);
            TrackWithMeasurments twm = new TrackWithMeasurments(track);
            for (int i = 0; i < ms.size(); i++) {
                Measurement m = measurementDecoder.decode(ms.get(i), mediaType);
                m.setTrack(track);
                if (m.getSensor() == null) {
                    m.setSensor(trackSensor);
                }
                twm.addMeasurement(m);
            }
            track = twm;
        }
        return track;
    }
}
