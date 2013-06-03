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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.MeasurementValue;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.User;
import io.car.server.core.util.GeoJSONConstants;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MeasurementEncoder extends AbstractEntityEncoder<Measurement> {
    private final EntityEncoder<Geometry> geometryEncoder;
    private final EntityEncoder<User> userProvider;
    private final EntityEncoder<Sensor> sensorProvider;

    @Inject
    public MeasurementEncoder(EntityEncoder<Geometry> geometryEncoder,
                              EntityEncoder<User> userProvider,
                              EntityEncoder<Sensor> sensorProvider) {
        this.geometryEncoder = geometryEncoder;
        this.userProvider = userProvider;
        this.sensorProvider = sensorProvider;
    }

    @Override
    public ObjectNode encode(Measurement t, MediaType mediaType) {
        ObjectNode measurement = getJsonFactory().objectNode();
        measurement.put(GeoJSONConstants.TYPE_KEY,
                        GeoJSONConstants.FEATURE_TYPE);
        if (t.hasGeometry()) {
            measurement.put(JSONConstants.GEOMETRY_KEY,
                            geometryEncoder.encode(t.getGeometry(), mediaType));
        }

        ObjectNode properties = measurement
                .putObject(GeoJSONConstants.PROPERTIES_KEY);
        if (t.hasIdentifier()) {
            properties.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
        }
        if (t.hasTime()) {
            properties.put(JSONConstants.TIME_KEY,
                           getDateTimeFormat().print(t.getTime()));
        }

        if (!mediaType.equals(MediaTypes.TRACK_TYPE)) {
            if (t.hasSensor()) {
                properties.put(JSONConstants.SENSOR_KEY,
                               sensorProvider.encode(t.getSensor(), mediaType));
            }
            if (t.hasUser()) {
                properties.put(JSONConstants.USER_KEY,
                               userProvider.encode(t.getUser(), mediaType));
            }
            if (t.hasModificationTime()) {
                properties.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
            if (t.hasCreationTime()) {
                properties.put(JSONConstants.CREATED_KEY,
                               getDateTimeFormat().print(t.getCreationTime()));
            }
        }
        ObjectNode values = properties.putObject(JSONConstants.PHENOMENONS_KEY);
        for (MeasurementValue mv : t.getValues()) {
            if (mv.hasPhenomenon() && mv.hasValue()) {
                ObjectNode phenomenon = values.objectNode();
                Object value = mv.getValue();
                if (value instanceof Number) {
                    phenomenon.put(JSONConstants.VALUE_KEY, ((Number) value)
                            .doubleValue());
                } else if (value instanceof Boolean) {
                    phenomenon.put(JSONConstants.VALUE_KEY, (Boolean) value);
                } else if (value != null) {
                    phenomenon.put(JSONConstants.VALUE_KEY, value.toString());
                }
                values.put(mv.getPhenomenon().getName(), phenomenon);
                if (mv.getPhenomenon().hasUnit()) {
                    phenomenon.put(JSONConstants.UNIT_KEY, mv.getPhenomenon()
                            .getUnit());
                }
            }
        }
        return measurement;
    }
}
