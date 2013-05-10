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

import io.car.server.rest.EntityEncoder;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.inject.Inject;

import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UsersCoder implements EntityEncoder<Users> {
    private UriInfo uriInfo;

    @Inject
    public UsersCoder(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public JSONObject encode(Users t, MediaType mediaType) throws JSONException {
        JSONArray a = new JSONArray();
        for (User u : t) {
            URI uri = uriInfo.getAbsolutePathBuilder().path(u.getName()).build();
            a.put(new JSONObject().put(JSONConstants.NAME_KEY, u.getName())
            		.put(JSONConstants.HREF_KEY, uri));
        }
        return new JSONObject().put(JSONConstants.USERS_KEY, a);
    }
}
