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

import io.car.server.core.entities.Sensor;
import io.car.server.rest.JSONConstants;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class SensorDecoder extends AbstractEntityDecoder<Sensor> {
    @Override
    public Sensor decode(JsonNode j, MediaType mediaType) {
        Sensor s = getEntityFactory().createSensor();
        s.setName(j.path(JSONConstants.NAME_KEY).textValue());
        Iterator<Entry<String, JsonNode>> iter = j.fields();
        while (iter.hasNext()) {
            Entry<String, JsonNode> attrib = iter.next();
            s.addAttribute(attrib.getKey(), attrib.getValue().textValue());
        }
        return s;
    }
}
