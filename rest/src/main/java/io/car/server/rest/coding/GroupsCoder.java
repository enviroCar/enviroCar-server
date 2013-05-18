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

import org.codehaus.jettison.json.JSONArray;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupsCoder extends AbstractEntityEncoder<Groups> {


    @Override
    public ObjectNode encode(Groups t, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode groups = root.putArray(JSONConstants.GROUPS_KEY);
        JSONArray a = new JSONArray();
        for (Group u : t) {
            URI uri = getUriInfo().getAbsolutePathBuilder().path(u.getName()).build();
            groups.addObject()
                    .put(JSONConstants.NAME_KEY, u.getName())
                    .put(JSONConstants.HREF_KEY, uri.toString());
        }
        return root;
    }
}
