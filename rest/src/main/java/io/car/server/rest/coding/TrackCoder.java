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
package io.car.server.rest.coding;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.core.dao.SensorDao;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.util.GeoJSONConstants;

public class TrackCoder extends AbstractEntityCoder<Track> {
    private EntityEncoder<Sensor> sensorEncoder;
    private EntityEncoder<Measurements> measurementsEncoder;
    private EntityEncoder<User> userEncoder;
    private SensorDao sensorDao;

    @Inject
    public TrackCoder(EntityEncoder<Sensor> sensorProvider,
                      EntityEncoder<Measurements> measurementsProvider,
                      EntityEncoder<User> userProvider,
                      SensorDao sensorDao) {
        this.sensorEncoder = sensorProvider;
        this.userEncoder = userProvider;
        this.measurementsEncoder = measurementsProvider;
        this.sensorDao = sensorDao;
    }

    @Override
    public Track decode(JsonNode j, MediaType mediaType) {
        Track track = getEntityFactory().createTrack();
        if (j.has(GeoJSONConstants.PROPERTIES_KEY)) {
            JsonNode p = j.path(GeoJSONConstants.PROPERTIES_KEY);
            if (p.has(JSONConstants.SENSOR_KEY)) {
                track.setSensor(sensorDao.getByName(p.get(JSONConstants.SENSOR_KEY).asText()));
            }
            track.setName(p.path(JSONConstants.NAME_KEY).textValue());
            track.setDescription(p.path(JSONConstants.DESCRIPTION_KEY).textValue());
        }
        return track;
    }

    @Override
    public ObjectNode encode(Track t, MediaType mediaType) {
        ObjectNode track = getJsonFactory().objectNode();
        track.put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_COLLECTION_TYPE);
        JsonNode features = measurementsEncoder.encode(t.getMeasurements(), mediaType)
                .path(GeoJSONConstants.FEATURES_KEY);
        track.put(GeoJSONConstants.FEATURES_KEY, features);
        ObjectNode properties = track.putObject(GeoJSONConstants.PROPERTIES_KEY);
        properties.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
        properties.put(JSONConstants.NAME_KEY, t.getName());
        properties.put(JSONConstants.DESCRIPTION_KEY, t.getDescription());
        properties.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(t.getCreationDate()));
        properties.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(t.getLastModificationDate()));
        properties.put(JSONConstants.SENSOR_KEY, sensorEncoder.encode(t.getSensor(), mediaType));
        properties.put(JSONConstants.USER_KEY, userEncoder.encode(t.getUser(), mediaType));
        return track;
    }
}