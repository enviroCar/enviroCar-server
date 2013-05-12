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

import com.google.inject.Inject;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.rest.EntityEncoder;

/**
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MeasurementsCoder implements EntityEncoder<Measurements> {
    private EntityEncoder<Measurement> measurementEncoder;

    @Inject
    public MeasurementsCoder(EntityEncoder<Measurement> measurementEncoder) {
        this.measurementEncoder = measurementEncoder;
    }

	@Override
    public JSONObject encode(Measurements t, MediaType mediaType) throws JSONException {
        JSONArray measurements = new JSONArray();
        for (Measurement m : t) {
            measurements.put(measurementEncoder.encode(m, mediaType));
		}
        return new JSONObject().put(JSONConstants.MEASUREMENTS_KEY, measurements);
	}
}
