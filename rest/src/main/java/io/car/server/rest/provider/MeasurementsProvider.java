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

import java.net.URI;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.rest.MediaTypes;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 * 
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementsProvider extends
		AbstractJsonEntityProvider<Measurements> {

	@Context
	private UriInfo uriInfo;

	public MeasurementsProvider(Class<Measurements> classType, MediaType get) {
		super(Measurements.class, MediaTypes.MEASUREMENTS_TYPE);
	}

	@Override
	public Measurements read(JSONObject j, MediaType mediaType)
			throws JSONException {
        throw new UnsupportedOperationException();
	}

	@Override
	public JSONObject write(Measurements t, MediaType mediaType)
			throws JSONException {
		JSONArray a = new JSONArray();
		for(Measurement m : t){
//			URI uri = uriInfo.getAbsolutePathBuilder()
		}
		return null;
	}
}
