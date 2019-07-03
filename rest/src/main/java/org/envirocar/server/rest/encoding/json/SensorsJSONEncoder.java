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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.envirocar.server.core.CarSimilarityService;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class SensorsJSONEncoder extends AbstractJSONEntityEncoder<Sensors> {
    private final JSONEntityEncoder<Sensor> sensorEncoder;
    private final CarSimilarityService carSimilarity;

    @Inject
    public SensorsJSONEncoder(JSONEntityEncoder<Sensor> sensorEncoder, CarSimilarityService carSimilarity) {
        super(Sensors.class);
        this.sensorEncoder = sensorEncoder;
        this.carSimilarity = carSimilarity;
    }

    @Override
    public ObjectNode encodeJSON(Sensors t, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode sensors = root.putArray(JSONConstants.SENSORS_KEY);

        Set<String> filteredDuplicates = this.carSimilarity.getMappedSensorIds();

        for (Sensor u : t) {
            if (filteredDuplicates == null || !filteredDuplicates.contains(u.getIdentifier())) {
                sensors.add(sensorEncoder.encodeJSON(u, mediaType));
            }
        }
        return root;
    }
}
