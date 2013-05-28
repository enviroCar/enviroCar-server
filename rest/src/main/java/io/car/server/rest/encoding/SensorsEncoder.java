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
package io.car.server.rest.encoding;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.rest.JSONConstants;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class SensorsEncoder extends AbstractEntityEncoder<Sensors> {
    private final EntityEncoder<Sensor> sensorEncoder;

    @Inject
    public SensorsEncoder(EntityEncoder<Sensor> sensorEncoder) {
        this.sensorEncoder = sensorEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Sensors t, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode sensors = root.putArray(JSONConstants.SENSORS_KEY);
        for (Sensor u : t) {
            sensors.add(sensorEncoder.encodeJSON(u, mediaType));
        }
        return root;
    }

    @Override
    public Model encodeRDF(Sensors t, MediaType mt) {
        /* TODO implement io.car.server.rest.encoding.SensorsEncoder.encodeRDF() */
        throw new UnsupportedOperationException("io.car.server.rest.encoding.SensorsEncoder.encodeRDF() not yet implemented");
    }
}
