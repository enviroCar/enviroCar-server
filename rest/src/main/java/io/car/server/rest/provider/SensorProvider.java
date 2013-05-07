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
package io.car.server.rest.provider;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.inject.Inject;

import io.car.server.core.EntityFactory;
import io.car.server.core.entities.Sensor;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class SensorProvider extends AbstractJsonEntityProvider<Sensor> {
    private EntityFactory factory;

    @Inject
    public SensorProvider(EntityFactory factory) {
        //TODO mediatypes
        super(Sensor.class, MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_JSON_TYPE);
        this.factory = factory;
    }

    @Override
    public Sensor read(JSONObject j, MediaType mediaType) throws JSONException {
        return factory.createSensor().setName(j.getString(JSONConstants.NAME_KEY));
    }

    @Override
    public JSONObject write(Sensor t, MediaType mediaType) throws JSONException {
        return new JSONObject().put(JSONConstants.NAME_KEY, t.getName());
    }
}
