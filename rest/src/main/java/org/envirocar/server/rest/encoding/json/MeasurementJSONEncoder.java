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
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class MeasurementJSONEncoder extends AbstractJSONEntityEncoder<Measurement> {
    private final JSONEntityEncoder<Geometry> geometryEncoder;
    private final JSONEntityEncoder<Sensor> sensorProvider;

    @Inject
    public MeasurementJSONEncoder(JSONEntityEncoder<Geometry> geometryEncoder,
                                  JSONEntityEncoder<Sensor> sensorProvider) {
        super(Measurement.class);
        this.geometryEncoder = geometryEncoder;
        this.sensorProvider = sensorProvider;
    }

    @Override
    public ObjectNode encodeJSON(Measurement entity, MediaType mediaType) {
        ObjectNode measurement = getJsonFactory().objectNode();
        measurement.put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_TYPE);
        if (entity.hasGeometry()) {
            measurement.set(JSONConstants.GEOMETRY_KEY, geometryEncoder.encodeJSON(entity.getGeometry(), mediaType));
        }

        ObjectNode properties = measurement.putObject(GeoJSONConstants.PROPERTIES_KEY);
        if (entity.hasIdentifier()) {
            properties.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        }
        if (entity.hasTime()) {
            properties.put(JSONConstants.TIME_KEY, getDateTimeFormat().print(entity.getTime()));
        }

        if (!mediaType.equals(MediaTypes.TRACK_TYPE)) {
            if (entity.hasSensor()) {
                properties.set(JSONConstants.SENSOR_KEY, sensorProvider.encodeJSON(entity.getSensor(), mediaType));
            }
            if (entity.hasTrack()) {
                properties.put(JSONConstants.TRACK_KEY, entity.getTrack().getIdentifier());
            }
        }
        if (mediaType.equals(MediaTypes.MEASUREMENT_TYPE) || mediaType.equals(MediaTypes.MEASUREMENTS_TYPE)
                || mediaType.equals(MediaTypes.TRACK_TYPE)) {
            ObjectNode values = properties.putObject(JSONConstants.PHENOMENONS_KEY);

            for (MeasurementValue mv : entity.getValues()) {
                if (mv.hasPhenomenon() && mv.hasValue()) {
                    ObjectNode phenomenon = values.objectNode();
                    phenomenon.putPOJO(JSONConstants.VALUE_KEY, mv.getValue());
                    values.set(mv.getPhenomenon().getName(), phenomenon);
                    if (mv.getPhenomenon().hasUnit()) {
                        phenomenon.put(JSONConstants.UNIT_KEY, mv.getPhenomenon().getUnit());
                    }
                }
            }
        }
        return measurement;
    }
}
