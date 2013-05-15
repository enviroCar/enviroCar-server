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
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;

import io.car.server.core.EntityFactory;
import io.car.server.core.entities.User;
import io.car.server.rest.EntityDecoder;
import io.car.server.rest.EntityEncoder;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UsersResource;


/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UserCoder implements EntityEncoder<User>, EntityDecoder<User> {
    private DateTimeFormatter formatter;
    private EntityFactory factory;
    private UriInfo uriInfo;

    @Inject
    public UserCoder(DateTimeFormatter formatter, EntityFactory factory, UriInfo uriInfo) {
        this.formatter = formatter;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    public User decode(JSONObject j, MediaType mediaType) throws JSONException {
        return factory.createUser()
                .setName(j.optString(JSONConstants.NAME_KEY, null))
                .setMail(j.optString(JSONConstants.MAIL_KEY, null))
                .setToken(j.optString(JSONConstants.TOKEN_KEY, null));
    }

    @Override
    public JSONObject encode(User t, MediaType mediaType) throws JSONException {
        if (mediaType.equals(MediaTypes.GROUP_TYPE)) {
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.USERS_PATH)
                    .path(UsersResource.USER_PATH).build(t.getName());
            return new JSONObject()
                    .put(JSONConstants.NAME_KEY, t.getName())
                    .put(JSONConstants.HREF_KEY, uri);
        }
        return new JSONObject()
                .put(JSONConstants.NAME_KEY, t.getName())
                .put(JSONConstants.MAIL_KEY, t.getMail())
                .put(JSONConstants.CREATED_KEY, formatter.print(t.getCreationDate()))
                .put(JSONConstants.MODIFIED_KEY, formatter.print(t.getLastModificationDate()));
    }
}
