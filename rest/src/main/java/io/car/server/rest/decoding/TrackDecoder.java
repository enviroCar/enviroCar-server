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
package io.car.server.rest.decoding;

import io.car.server.rest.JSONConstants;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.SensorDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Track;
import io.car.server.core.util.GeoJSONConstants;

public class TrackDecoder extends AbstractEntityDecoder<Track> {
    private EntityDecoder<Measurement> measurementDecoder;
    private SensorDao sensorDao;
    private MeasurementDao measurementDao;

    @Inject
    public TrackDecoder(EntityDecoder<Measurement> measurementDecoder,
                        SensorDao sensorDao,
                        MeasurementDao measurementDao) {
        this.measurementDecoder = measurementDecoder;
        this.sensorDao = sensorDao;
        this.measurementDao = measurementDao;
    }

    @Override
    public Track decode(JsonNode j, MediaType mediaType) {
        Track track = getEntityFactory().createTrack();
        if (j.has(GeoJSONConstants.PROPERTIES_KEY)) {
            JsonNode p = j.path(GeoJSONConstants.PROPERTIES_KEY);
            if (p.has(JSONConstants.SENSOR_KEY)) {
                track.setSensor(sensorDao.getByName(p.get(
                        JSONConstants.SENSOR_KEY).asText()));
            }
            track.setName(p.path(JSONConstants.NAME_KEY).textValue());
            track.setDescription(p.path(JSONConstants.DESCRIPTION_KEY)
                    .textValue());
            if (j.has(JSONConstants.MEASUREMENTS_KEY)) {
//                trackDao.create(track);
                JsonNode m = j.path(JSONConstants.MEASUREMENTS_KEY);
                for (int i = 0; i < m.size(); i++) {
                    measurementDao.create(measurementDecoder.decode(
                            m.get(i), mediaType).setTrack(track));
                }
            }
        }
        return track;
    }
}