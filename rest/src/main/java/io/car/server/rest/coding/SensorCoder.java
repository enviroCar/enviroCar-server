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
package io.car.server.rest.coding;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.Sensor;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.SensorsResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class SensorCoder extends AbstractEntityCoder<Sensor> {
    @Override
    public Sensor decode(JsonNode j, MediaType mediaType) {
        return getEntityFactory().createSensor().setName(j
                .path(JSONConstants.NAME_KEY).textValue());
    }

    @Override
    public ObjectNode encode(Sensor t, MediaType mediaType) {
        ObjectNode user = getJsonFactory().objectNode()
                .put(JSONConstants.NAME_KEY, t.getName());
        if (mediaType.equals(MediaTypes.PHENOMENON_TYPE)) {
            user.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(t
                    .getCreationDate()));
            user.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(t
                    .getLastModificationDate()));
        } else {
            URI href = getUriInfo().getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.SENSORS)
                    .path(SensorsResource.SENSOR)
                    .build(t.getName());
            user.put(JSONConstants.HREF_KEY, href.toString());
        }
        return user;
    }
}
