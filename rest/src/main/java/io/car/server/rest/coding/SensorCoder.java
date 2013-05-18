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

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;

import io.car.server.core.entities.EntityFactory;
import io.car.server.core.entities.Sensor;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.SensorsResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class SensorCoder implements EntityEncoder<Sensor>, EntityDecoder<Sensor> {
    private final EntityFactory factory;
    private final DateTimeFormatter format;
    private final UriInfo uriInfo;


    @Inject
    public SensorCoder(EntityFactory factory, DateTimeFormatter format, UriInfo uriInfo) {
        this.factory = factory;
        this.format = format;
        this.uriInfo = uriInfo;
    }

    @Override
    public Sensor decode(JSONObject j, MediaType mediaType) throws JSONException {
        return factory.createSensor().setName(j.getString(JSONConstants.NAME_KEY));
    }

    @Override
    public JSONObject encode(Sensor t, MediaType mediaType) throws JSONException {
        JSONObject j = new JSONObject().put(JSONConstants.NAME_KEY, t.getName());
        if (mediaType.equals(MediaTypes.SENSOR_TYPE)) {
            j.put(JSONConstants.CREATED_KEY, format.print(t.getCreationDate()));
            j.put(JSONConstants.MODIFIED_KEY, format.print(t.getLastModificationDate()));
        } else {
            URI href = uriInfo.getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.SENSORS)
                    .path(SensorsResource.SENSOR)
                    .build(t.getName());
            j.put(JSONConstants.HREF_KEY, href);
        }
        return j;
    }
}
