/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class GroupJSONEncoder extends AbstractJSONEntityEncoder<Group> {
    private final JSONEntityEncoder<User> userEncoder;

    @Inject
    public GroupJSONEncoder(JSONEntityEncoder<User> userProvider) {
        super(Group.class);
        this.userEncoder = userProvider;
    }

    @Override
    public ObjectNode encodeJSON(Group entity, AccessRights rights, MediaType mediaType) {
        ObjectNode group = getJsonFactory().objectNode();
        if (entity.hasName() && rights.canSeeNameOf(entity)) {
            group.put(JSONConstants.NAME_KEY, entity.getName());
        }
        if (entity.hasDescription() && rights.canSeeDescriptionOf(entity)) {
            group.put(JSONConstants.DESCRIPTION_KEY, entity.getDescription());
        }
        if (entity.hasOwner() && rights.canSeeOwnerOf(entity)) {
            group.set(JSONConstants.OWNER_KEY, userEncoder.encodeJSON(entity.getOwner(), rights, mediaType));
        }
        if (entity.hasCreationTime() && rights.canSeeCreationTimeOf(entity)) {
            group.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
        }
        if (entity.hasModificationTime() && rights.canSeeModificationTimeOf(entity)) {
            group.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
        }
        return group;
    }
}
