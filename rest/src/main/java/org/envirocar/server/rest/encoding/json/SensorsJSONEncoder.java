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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;

import org.envirocar.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class SensorsJSONEncoder extends AbstractJSONEntityEncoder<Sensors> {
    private final JSONEntityEncoder<Sensor> sensorEncoder;

    @Inject
    public SensorsJSONEncoder(JSONEntityEncoder<Sensor> sensorEncoder) {
        super(Sensors.class);
        this.sensorEncoder = sensorEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Sensors t, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode sensors = root.putArray(JSONConstants.SENSORS_KEY);
        for (Sensor u : t) {
            sensors.add(sensorEncoder.encodeJSON(u, rights, mediaType));
        }
        return root;
    }
}
