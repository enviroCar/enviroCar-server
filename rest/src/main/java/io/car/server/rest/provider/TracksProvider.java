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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.rest.MediaTypes;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TracksProvider extends AbstractJsonEntityProvider<Tracks> {
	@Context
    private UriInfo uriInfo;
	
	public TracksProvider() {
		super(Tracks.class, MediaTypes.TRACKS_TYPE);
	}

	@Override
	public Tracks read(JSONObject j, MediaType mediaType) throws JSONException {
		throw new UnsupportedOperationException();
	}

	@Override
	public JSONObject write(Tracks t, MediaType mediaType) throws JSONException {
		JSONArray array = new JSONArray();
        for (Track track : t) {

            array.put(new JSONObject()
                    .put(JSONConstants.IDENTIFIER_KEY, track.getIdentifier())
                    .put(JSONConstants.HREF_KEY, uriInfo.getRequestUriBuilder().path(track.getIdentifier()).build()));
		}
        return new JSONObject().put(JSONConstants.TRACKS_KEY, array);
	}

}
