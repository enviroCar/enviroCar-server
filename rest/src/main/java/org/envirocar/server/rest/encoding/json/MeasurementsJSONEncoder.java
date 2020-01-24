/*
 * Copyright (C) 2013-2020 The enviroCar project
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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 */
@Provider
@Singleton
public class MeasurementsJSONEncoder extends AbstractJSONEntityEncoder<Measurements> {
    private final JSONEntityEncoder<Measurement> measurementEncoder;
    private final JsonNodeFactory factory;

    @Inject
    public MeasurementsJSONEncoder(JsonNodeFactory factory, JSONEntityEncoder<Measurement> measurementEncoder) {
        super(Measurements.class);
        this.measurementEncoder = measurementEncoder;
        this.factory = factory;
    }

    @Override
    public ObjectNode encodeJSON(Measurements entity, AccessRights rights, MediaType mediaType) {
        ObjectNode root = factory.objectNode();
        root.put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_COLLECTION_TYPE);

        ArrayNode features = root.putArray(GeoJSONConstants.FEATURES_KEY);
        for (Measurement measurement : entity) {
            features.add(measurementEncoder.encodeJSON(measurement, rights, mediaType));
        }
        return root;
    }
}
