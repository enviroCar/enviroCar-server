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
package io.car.server.rest.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;

import io.car.server.core.EntityFactory;
import io.car.server.core.MeasurementValue;
import io.car.server.core.db.PhenomenonDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.exception.GeometryConverterException;
import io.car.server.rest.MediaTypes;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementProvider extends AbstractJsonEntityProvider<Measurement> {
    @Inject
    private DateTimeFormatter formatter;
    @Inject
    private EntityFactory factory;
    @Inject
    private GeoJSON geoJSON;
    @Inject
    private UserProvider userProvider;
    @Inject
    private SensorProvider sensorProvider;
    @Inject
    private PhenomenonProvider phenomenonProvider;
    @Inject
    private PhenomenonDao phenomenonDao;

    public MeasurementProvider() {
        super(Measurement.class, MediaTypes.MEASUREMENT_TYPE,
              MediaTypes.MEASUREMENT_CREATE_TYPE,
              MediaTypes.MEASUREMENT_MODIFY_TYPE);
    }

    @Override
    public Measurement read(JSONObject j, MediaType mediaType)
            throws JSONException {
        try {
            Measurement measurement = factory.createMeasurement();
            if (j.has(JSONConstants.TIME_KEY)) {
                measurement.setTime(formatter.parseDateTime(j.getString(JSONConstants.TIME_KEY)));
            }
            if (j.has(JSONConstants.GEOMETRY_KEY)) {
                measurement.setGeometry(geoJSON.decode(j.getJSONObject(JSONConstants.GEOMETRY_KEY)));
            }
            if (j.has(JSONConstants.PHENOMENONS_KEY)) {
                JSONArray array = j.getJSONArray(JSONConstants.PHENOMENONS_KEY);
                for (int i = 0; i < array.length(); ++i) {
                    measurement.addValue(
                            factory.createMeasurementValue().setValue(j.get(JSONConstants.VALUE_KEY))
                            .setPhenomenon(phenomenonDao.getByName(j.getJSONObject(JSONConstants.PHENOMENON_KEY)
                            .getString(JSONConstants.NAME_KEY))));
                }
            }
            return null;
        } catch (GeometryConverterException ex) {
            throw new JSONException(ex);
        }
    }

    @Override
    public JSONObject write(Measurement t, MediaType mediaType)
            throws JSONException {
        try {
            JSONObject j = new JSONObject()
                    .put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier())
                    .put(JSONConstants.TIME_KEY, formatter.print(t.getTime()))
                    .put(JSONConstants.SENSOR_KEY, sensorProvider.write(t.getSensor(), mediaType))
                    .put(JSONConstants.USER_KEY, userProvider.write(t.getUser(), mediaType))
                    .put(JSONConstants.GEOMETRY_KEY, geoJSON.encode(t.getGeometry()))
                    .put(JSONConstants.MODIFIED_KEY, formatter.print(t.getLastModificationDate()))
                    .put(JSONConstants.CREATED_KEY, formatter.print(t.getCreationDate()));
            JSONArray values = new JSONArray();
            for (MeasurementValue mv : t.getValues()) {
                values.put(new JSONObject()
                        .put(JSONConstants.PHENOMENON_KEY, phenomenonProvider.write(mv.getPhenomenon(), mediaType))
                        .put(JSONConstants.VALUE_KEY, mv.getValue()));
            }
            return j.put(JSONConstants.PHENOMENONS_KEY, values);
        } catch (GeometryConverterException ex) {
            throw new JSONException(ex);
        }
    }
}
