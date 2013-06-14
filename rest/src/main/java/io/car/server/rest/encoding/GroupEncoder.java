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
package io.car.server.rest.encoding;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.GroupResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupEncoder extends AbstractEntityEncoder<Group> {
    private EntityEncoder<User> userEncoder;

    @Inject
    public GroupEncoder(EntityEncoder<User> userProvider) {
        this.userEncoder = userProvider;
    }

    @Override
    public ObjectNode encode(Group t, MediaType mediaType) {
        ObjectNode group = getJsonFactory().objectNode();
        if (t.hasName() && getRights().canSeeNameOf(t)) {
            group.put(JSONConstants.NAME_KEY, t.getName());
        }
        if (t.hasDescription() && getRights().canSeeDescriptionOf(t)) {
            group.put(JSONConstants.DESCRIPTION_KEY, t.getDescription());
        }
        if (t.hasOwner() && getRights().canSeeOwnerOf(t)) {
            group.put(JSONConstants.OWNER_KEY,
                      userEncoder.encode(t.getOwner(), mediaType));
        }
        if (mediaType.equals(MediaTypes.GROUP_TYPE)) {
            if (t.hasCreationTime() && getRights().canSeeCreationTimeOf(t)) {
                group.put(JSONConstants.CREATED_KEY,
                          getDateTimeFormat().print(t.getCreationTime()));
            }
            if (t.hasModificationTime() && getRights()
                    .canSeeModificationTimeOf(t)) {
                group.put(JSONConstants.MODIFIED_KEY,
                          getDateTimeFormat().print(t.getModificationTime()));
            }
            if (getRights().canSeeMembersOf(t)) {
                group.put(JSONConstants.MEMBERS_KEY, getUriInfo()
                        .getAbsolutePathBuilder()
                        .path(GroupResource.MEMBERS).build().toString());
            }
            if (getRights().canSeeActivitiesOf(t)) {
                group.put(JSONConstants.ACTIVITIES_KEY, getUriInfo()
                        .getAbsolutePathBuilder()
                        .path(GroupResource.ACTIVITIES).build().toString());
            }
        }
        return group;
    }
}
