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
import com.google.inject.Inject;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.rest.resources.GroupResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupCoder extends AbstractEntityCoder<Group> {
    private EntityEncoder<User> userEncoder;

    @Inject
    public GroupCoder(EntityEncoder<User> userProvider) {
        this.userEncoder = userProvider;
    }

    @Override
    public Group decode(JsonNode j, MediaType mediaType) {
        Group group = getEntityFactory().createGroup();
        group.setName(j.path(JSONConstants.NAME_KEY).textValue());
        group.setDescription(j.path(JSONConstants.DESCRIPTION_KEY).textValue());
        return group;
    }

    @Override
    public ObjectNode encode(Group t, MediaType mediaType) {
        ObjectNode group = getJsonFactory().objectNode();
        group.put(JSONConstants.NAME_KEY, t.getName());
        group.put(JSONConstants.DESCRIPTION_KEY, t.getDescription());
        group.put(JSONConstants.CREATED_KEY,
                  getDateTimeFormat().print(t.getCreationDate()));
        group.put(JSONConstants.MODIFIED_KEY,
                  getDateTimeFormat().print(t.getLastModificationDate()));
        group.put(JSONConstants.OWNER_KEY,
                  userEncoder.encode(t.getOwner(), mediaType));
        URI uri = getUriInfo().getRequestUriBuilder()
                .path(GroupResource.MEMBERS).build();
        group.put(JSONConstants.MEMBERS_KEY, uri.toString());
        return group;
    }
}
