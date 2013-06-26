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
package org.envirocar.server.rest.encoding.json;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class SensorJSONEncoder extends AbstractJSONEntityEncoder<Sensor> {
    public SensorJSONEncoder() {
        super(Sensor.class);
    }

    @Override
    public ObjectNode encodeJSON(Sensor t, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode sensor = getJsonFactory().objectNode();
        if (t.hasType()) {
            sensor.put(JSONConstants.TYPE_KEY, t.getType());
        }
        Map<String, Object> properties = Maps.newHashMap();

        if (t.hasProperties()) {
            properties.putAll(t.getProperties());
        }
        properties.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
        if (mediaType.equals(MediaTypes.SENSOR_TYPE)) {
            if (t.hasCreationTime()) {
                properties.put(JSONConstants.CREATED_KEY,
                               getDateTimeFormat().print(t.getCreationTime()));
            }
            if (t.hasModificationTime()) {
                properties.put(JSONConstants.MODIFIED_KEY,
                               getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
        }

        sensor.putPOJO(JSONConstants.PROPERTIES_KEY, properties);
        return sensor;
    }
}
