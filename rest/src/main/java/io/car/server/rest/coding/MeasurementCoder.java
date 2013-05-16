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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;

import io.car.server.core.EntityFactory;
import io.car.server.core.MeasurementValue;
import io.car.server.core.db.PhenomenonDao;
import io.car.server.core.db.SensorDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.User;
import io.car.server.core.exception.GeometryConverterException;
import io.car.server.core.util.GeoJSONConstants;
import io.car.server.rest.EntityDecoder;
import io.car.server.rest.EntityEncoder;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MeasurementCoder implements EntityEncoder<Measurement>, EntityDecoder<Measurement> {
    private DateTimeFormatter formatter;
    private EntityFactory factory;
    private GeoJSON geoJSON;
    private EntityEncoder<User> userProvider;
    private EntityEncoder<Sensor> sensorProvider;
    private EntityEncoder<Phenomenon> phenomenonProvider;
    private PhenomenonDao phenomenonDao;
    private SensorDao sensorDao;

    @Inject
    public MeasurementCoder(DateTimeFormatter formatter,
                            EntityFactory factory,
                            GeoJSON geoJSON,
                            EntityEncoder<User> userProvider,
                            EntityEncoder<Sensor> sensorProvider,
                            EntityEncoder<Phenomenon> phenomenonProvider,
                            PhenomenonDao phenomenonDao,
                            SensorDao sensorDao) {
        this.formatter = formatter;
        this.factory = factory;
        this.geoJSON = geoJSON;
        this.userProvider = userProvider;
        this.sensorProvider = sensorProvider;
        this.phenomenonProvider = phenomenonProvider;
        this.phenomenonDao = phenomenonDao;
        this.sensorDao = sensorDao;
    }

    @Override
    public Measurement decode(JSONObject j, MediaType mediaType)
            throws JSONException {
        try {
            Measurement measurement = factory.createMeasurement();
            if (j.has(JSONConstants.GEOMETRY_KEY)) {
                measurement.setGeometry(geoJSON.decode(j.getJSONObject(JSONConstants.GEOMETRY_KEY)));
            }
            if (j.has(GeoJSONConstants.PROPERTIES_KEY)) {
                JSONObject p = j.getJSONObject(GeoJSONConstants.PROPERTIES_KEY);
                if (p.has(JSONConstants.SENSOR_KEY)) {
                    measurement.setSensor(sensorDao.getByName(p.getJSONObject(JSONConstants.SENSOR_KEY)
                            .getString(JSONConstants.NAME_KEY)));
                }
                if (p.has(JSONConstants.TIME_KEY)) {
                    measurement.setTime(formatter.parseDateTime(p.getString(JSONConstants.TIME_KEY)));
                }

                if (p.has(JSONConstants.PHENOMENONS_KEY)) {
                    JSONArray array = p.getJSONArray(JSONConstants.PHENOMENONS_KEY);
                    for (int i = 0; i < array.length(); ++i) {
                        measurement.addValue(decodeMeasurementValue(array.getJSONObject(i)));
                    }
                }
            }
            
            return measurement;
        } catch (GeometryConverterException ex) {
            throw new JSONException(ex);
        }
    }

    @Override
    public JSONObject encode(Measurement t, MediaType mediaType) throws JSONException {
        try {
            //FIXME just encode references to user/sensor
            JSONObject properties = new JSONObject()
                    .put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier())
                    .put(JSONConstants.TIME_KEY, formatter.print(t.getTime()))
                    .put(JSONConstants.SENSOR_KEY, sensorProvider.encode(t.getSensor(), mediaType))
                    .put(JSONConstants.USER_KEY, userProvider.encode(t.getUser(), mediaType))
                    .put(JSONConstants.MODIFIED_KEY, formatter.print(t.getLastModificationDate()))
                    .put(JSONConstants.CREATED_KEY, formatter.print(t.getCreationDate()));
            JSONArray values = new JSONArray();
            for (MeasurementValue mv : t.getValues()) {
                values.put(new JSONObject()
                        .put(JSONConstants.PHENOMENON_KEY, phenomenonProvider.encode(mv.getPhenomenon(), mediaType))
                        .put(JSONConstants.VALUE_KEY, mv.getValue()));
            }
            properties.put(JSONConstants.PHENOMENONS_KEY, values);
            return new JSONObject()
                    .put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_TYPE)
                    .put(JSONConstants.GEOMETRY_KEY, geoJSON.encode(t.getGeometry()))
                    .put(GeoJSONConstants.PROPERTIES_KEY, properties);
            
        } catch (GeometryConverterException ex) {
            throw new JSONException(ex);
        }
    }

    protected MeasurementValue decodeMeasurementValue(JSONObject j) throws JSONException {
        String phenomenonName = j
                .getJSONObject(JSONConstants.PHENOMENON_KEY)
                .getString(JSONConstants.NAME_KEY);
        Phenomenon phenomenon = phenomenonDao.getByName(phenomenonName);
        Object value = j.get(JSONConstants.VALUE_KEY);
        return factory.createMeasurementValue()
                .setValue(value)
                .setPhenomenon(phenomenon);
    }
}
