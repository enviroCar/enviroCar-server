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

import java.net.URI;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.dao.PhenomenonDao;
import io.car.server.core.dao.SensorDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.MeasurementValue;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.User;
import io.car.server.core.util.GeoJSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.MeasurementsResource;
import io.car.server.rest.resources.TrackResource;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MeasurementCoder extends AbstractEntityCoder<Measurement> {
    private final EntityEncoder<Geometry> geometryEncoder;
    private final EntityDecoder<Geometry> geometryDecoder;
    private final EntityEncoder<User> userProvider;
    private final EntityEncoder<Sensor> sensorProvider;
    private final EntityEncoder<Phenomenon> phenomenonProvider;
    private final PhenomenonDao phenomenonDao;
    private final SensorDao sensorDao;

    @Inject
    public MeasurementCoder(EntityEncoder<Geometry> geometryEncoder,
                            EntityDecoder<Geometry> geometryDecoder,
                            EntityEncoder<User> userProvider,
                            EntityEncoder<Sensor> sensorProvider,
                            EntityEncoder<Phenomenon> phenomenonProvider,
                            PhenomenonDao phenomenonDao,
                            SensorDao sensorDao) {
        this.geometryDecoder = geometryDecoder;
        this.geometryEncoder = geometryEncoder;
        this.userProvider = userProvider;
        this.sensorProvider = sensorProvider;
        this.phenomenonProvider = phenomenonProvider;
        this.phenomenonDao = phenomenonDao;
        this.sensorDao = sensorDao;
    }

    @Override
    public Measurement decode(JsonNode j, MediaType mediaType) {
            Measurement measurement = getEntityFactory().createMeasurement();
            if (j.has(JSONConstants.GEOMETRY_KEY)) {
                measurement.setGeometry(geometryDecoder.decode(j.path(JSONConstants.GEOMETRY_KEY), mediaType));
            }
            if (j.has(GeoJSONConstants.PROPERTIES_KEY)) {
                JsonNode p = j.path(GeoJSONConstants.PROPERTIES_KEY);
                if (p.has(JSONConstants.SENSOR_KEY)) {
                    measurement.setSensor(sensorDao.getByName(p.path(JSONConstants.SENSOR_KEY)
                            .path(JSONConstants.NAME_KEY).textValue()));
                }
                if (p.has(JSONConstants.TIME_KEY)) {
                    measurement.setTime(getDateTimeFormat().parseDateTime(p.path(JSONConstants.TIME_KEY).textValue()));
                }

                if (p.has(JSONConstants.PHENOMENONS_KEY)) {
                    JsonNode phens = p.path(JSONConstants.PHENOMENONS_KEY);
                    Iterator<Entry<String, JsonNode>> fields = phens.fields();
                    while (fields.hasNext()) {
                        Entry<String, JsonNode> field = fields.next();
                        Phenomenon phenomenon = phenomenonDao.getByName(field.getKey());
                        JsonNode valueNode = field.getValue().get(JSONConstants.VALUE_KEY);
                        if (valueNode.isValueNode()) {
                            Object value = null;
                            if (valueNode.isNumber()) {
                                value = valueNode.asDouble();
                            } else if (valueNode.isBoolean()) {
                                value = valueNode.booleanValue();
                            } else if (valueNode.isTextual()) {
                                value = valueNode.textValue();
                            }
                            measurement.addValue(getEntityFactory().createMeasurementValue()
                                    .setValue(value).setPhenomenon(phenomenon));
                        }
                    }
                }
            }
            return measurement;
    }

    @Override
    public ObjectNode encode(Measurement t, MediaType mediaType) {
        ObjectNode measurement = getJsonFactory().objectNode();
        measurement.put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_TYPE);
        measurement.put(JSONConstants.GEOMETRY_KEY, geometryEncoder.encode(t.getGeometry(), mediaType));

        ObjectNode properties = measurement.putObject(GeoJSONConstants.PROPERTIES_KEY);
        properties.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
        properties.put(JSONConstants.TIME_KEY, getDateTimeFormat().print(t.getTime()));

        if (!mediaType.equals(MediaTypes.TRACK_TYPE)) {
            properties.put(JSONConstants.SENSOR_KEY, sensorProvider.encode(t.getSensor(), mediaType));
            properties.put(JSONConstants.USER_KEY, userProvider.encode(t.getUser(), mediaType));
            properties.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(t.getLastModificationDate()));
            properties.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(t.getCreationDate()));
        } else {
            URI href = getUriInfo().getRequestUriBuilder()
                    .path(TrackResource.MEASUREMENTS)
                    .path(MeasurementsResource.MEASUREMENT)
                    .build(t.getIdentifier());
            properties.put(JSONConstants.HREF_KEY, href.toString());
        }
        ObjectNode values = properties.putObject(JSONConstants.PHENOMENONS_KEY);
        for (MeasurementValue mv : t.getValues()) {
            ObjectNode phenomenon = phenomenonProvider.encode(mv.getPhenomenon(), mediaType);
            Object value = mv.getValue();
            if (value instanceof Number) {
                phenomenon.put(JSONConstants.VALUE_KEY, ((Number) value).doubleValue());
            } else if (value instanceof Boolean) {
                phenomenon.put(JSONConstants.VALUE_KEY, (Boolean) value);
            } else if (value != null) {
                phenomenon.put(JSONConstants.VALUE_KEY, value.toString());
            }
            values.put(mv.getPhenomenon().getName(), phenomenon);
        }
        return measurement;
    }
}
