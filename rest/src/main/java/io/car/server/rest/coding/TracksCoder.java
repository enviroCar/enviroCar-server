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
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.inject.Inject;

import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;

public class TracksCoder implements EntityEncoder<Tracks> {
    private UriInfo uriInfo;

    @Inject
    public TracksCoder(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public JSONObject encode(Tracks t, MediaType mediaType) throws JSONException {
        JSONArray array = new JSONArray();
        for (Track track : t) {

            array.put(new JSONObject()
                    .put(JSONConstants.IDENTIFIER_KEY, track.getIdentifier())
                    .put(JSONConstants.HREF_KEY, uriInfo.getRequestUriBuilder().path(track.getIdentifier()).build()));
        }
        return new JSONObject().put(JSONConstants.TRACKS_KEY, array);
    }
}
