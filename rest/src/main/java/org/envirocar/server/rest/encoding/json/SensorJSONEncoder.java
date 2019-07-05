/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class SensorJSONEncoder extends AbstractJSONEntityEncoder<Sensor> {
    public SensorJSONEncoder() {
        super(Sensor.class);
    }

    @Override
    public ObjectNode encodeJSON(Sensor entity, AccessRights rights, MediaType mediaType) {
        ObjectNode sensor = getJsonFactory().objectNode();
        if (entity.hasType()) {
            sensor.put(JSONConstants.TYPE_KEY, entity.getType());
        }
        Map<String, Object> properties = new HashMap<>();

        if (entity.hasProperties()) {
            properties.putAll(entity.getProperties());
        }
        properties.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        if (mediaType.equals(MediaTypes.SENSOR_TYPE)) {
            if (entity.hasCreationTime()) {
                properties.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
            }
            if (entity.hasModificationTime()) {
                properties.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
            }
        }

        sensor.putPOJO(JSONConstants.PROPERTIES_KEY, properties);
        return sensor;
    }
}
