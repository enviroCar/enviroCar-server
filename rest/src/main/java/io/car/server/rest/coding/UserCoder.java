/*
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.User;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UserResource;
import io.car.server.rest.resources.UsersResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserCoder extends AbstractEntityCoder<User> {
    @Override
    public User decode(JsonNode j, MediaType mediaType) {
        return getEntityFactory().createUser()
                .setName(j.path(JSONConstants.NAME_KEY).textValue())
                .setMail(j.path(JSONConstants.MAIL_KEY).textValue())
                .setToken(j.path(JSONConstants.TOKEN_KEY).textValue());
    }

    @Override
    public ObjectNode encode(User t, MediaType mediaType) {
        ObjectNode j = getJsonFactory().objectNode()
                .put(JSONConstants.NAME_KEY, t.getName());
        if (mediaType.equals(MediaTypes.USER_TYPE)) {
            URI measurements = getUriInfo().getRequestUriBuilder()
                    .path(UserResource.MEASUREMENTS).build();
            URI groups = getUriInfo().getRequestUriBuilder()
                    .path(UserResource.GROUPS).build();
            URI friends = getUriInfo().getRequestUriBuilder()
                    .path(UserResource.FRIENDS).build();
            URI tracks = getUriInfo().getRequestUriBuilder()
                    .path(UserResource.TRACKS).build();
            URI statistics = getUriInfo().getRequestUriBuilder()
                    .path(UserResource.STATISTICS).build();
            j.put(JSONConstants.MAIL_KEY, t.getMail());
            j.put(JSONConstants.CREATED_KEY,
                  getDateTimeFormat().print(t.getCreationDate()));
            j.put(JSONConstants.MODIFIED_KEY,
                  getDateTimeFormat().print(t.getLastModificationDate()));
            j.put(JSONConstants.MEASUREMENTS_KEY, measurements.toString());
            j.put(JSONConstants.GROUPS_KEY, groups.toString());
            j.put(JSONConstants.FRIENDS_KEY, friends.toString());
            j.put(JSONConstants.TRACKS_KEY, tracks.toString());
            j.put(JSONConstants.STATISTICS_KEY, statistics.toString());
        } else {
            URI uri = getUriInfo().getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.USERS)
                    .path(UsersResource.USER).build(t.getName());
            return j.put(JSONConstants.HREF_KEY, uri.toString());

        }
        return j;
    }
}
