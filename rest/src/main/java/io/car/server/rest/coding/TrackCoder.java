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

import io.car.server.rest.EntityDecoder;
import io.car.server.rest.EntityEncoder;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;

import io.car.server.core.EntityFactory;
import io.car.server.core.db.SensorDao;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

public class TrackCoder implements EntityEncoder<Track>, EntityDecoder<Track> {
    private DateTimeFormatter formatter;
    private EntityFactory factory;
    private EntityEncoder<Sensor> sensorProvider;
    private EntityEncoder<User> userProvider;
    private SensorDao sensorDao;

    @Inject
    public TrackCoder(DateTimeFormatter formatter, EntityFactory factory,
                         EntityEncoder<Sensor> sensorProvider,
                         EntityEncoder<User> userProvider, SensorDao sensorDao) {
        this.formatter = formatter;
        this.factory = factory;
        this.sensorProvider = sensorProvider;
        this.userProvider = userProvider;
        this.sensorDao = sensorDao;
    }

    @Override
    public Track decode(JSONObject j, MediaType mediaType) throws JSONException {
        return factory.createTrack()
                .setSensor(sensorDao.getByName(j.getString(JSONConstants.SENSOR_KEY)));
    }

    @Override
    public JSONObject encode(Track t, MediaType mediaType) throws JSONException {
        return new JSONObject()
                .put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier())
                .put(JSONConstants.CREATED_KEY, formatter.print(t.getCreationDate()))
                .put(JSONConstants.MODIFIED_KEY, formatter.print(t.getLastModificationDate()))
                .put(JSONConstants.SENSOR_KEY, sensorProvider.encode(t.getSensor(), mediaType))
                .put(JSONConstants.USER_KEY, userProvider.encode(t.getUser(), mediaType));
    }
}