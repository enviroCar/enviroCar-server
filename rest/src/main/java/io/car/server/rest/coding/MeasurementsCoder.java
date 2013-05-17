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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.util.GeoJSONConstants;

/**
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MeasurementsCoder implements EntityEncoder<Measurements> {
    private final EntityEncoder<Measurement> measurementEncoder;
    private final JsonNodeFactory factory;

    @Inject
    public MeasurementsCoder(JsonNodeFactory factory, EntityEncoder<Measurement> measurementEncoder) {
        this.measurementEncoder = measurementEncoder;
        this.factory = factory;
    }

    @Override
    public JsonNode encode(Measurements t, MediaType mediaType) {
        ObjectNode on = factory.objectNode();
        ArrayNode an = on.putArray(GeoJSONConstants.FEATURES_KEY);
        for (Measurement measurement : t) {
            an.add(measurementEncoder.encode(measurement, mediaType));
        }
        on.put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_COLLECTION_TYPE);
        return on;
    }
}
