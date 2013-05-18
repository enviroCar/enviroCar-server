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

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;

public class TracksCoder extends AbstractEntityEncoder<Tracks> {
    @Override
    public ObjectNode encode(Tracks t, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode tracks = root.putArray(JSONConstants.TRACKS_KEY);
        for (Track u : t) {
            URI uri = getUriInfo().getRequestUriBuilder().path(u.getIdentifier()).build();
            ObjectNode track = tracks.addObject();
            track.put(JSONConstants.IDENTIFIER_KEY, u.getIdentifier());
            track.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(u.getLastModificationDate()));
            if (u.getName() != null) {
                track.put(JSONConstants.NAME_KEY, u.getName());
            }
            track.put(JSONConstants.HREF_KEY, uri.toString());
        }
        return root;
    }
}
