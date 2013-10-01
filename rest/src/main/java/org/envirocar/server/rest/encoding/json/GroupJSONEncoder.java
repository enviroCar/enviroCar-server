/*
 * Copyright (C) 2013 The enviroCar project
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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;

import org.envirocar.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class GroupJSONEncoder extends AbstractJSONEntityEncoder<Group> {
    private JSONEntityEncoder<User> userEncoder;

    @Inject
    public GroupJSONEncoder(JSONEntityEncoder<User> userProvider) {
        super(Group.class);
        this.userEncoder = userProvider;
    }

    @Override
    public ObjectNode encodeJSON(Group t, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode group = getJsonFactory().objectNode();
        if (t.hasName() && rights.canSeeNameOf(t)) {
            group.put(JSONConstants.NAME_KEY, t.getName());
        }
        if (t.hasDescription() && rights.canSeeDescriptionOf(t)) {
            group.put(JSONConstants.DESCRIPTION_KEY, t.getDescription());
        }
        if (t.hasOwner() && rights.canSeeOwnerOf(t)) {
            group.put(JSONConstants.OWNER_KEY,
                      userEncoder.encodeJSON(t.getOwner(), rights, mediaType));
        }
        if (t.hasCreationTime() && rights.canSeeCreationTimeOf(t)) {
            group.put(JSONConstants.CREATED_KEY,
                      getDateTimeFormat().print(t.getCreationTime()));
        }
        if (t.hasModificationTime() && rights
                .canSeeModificationTimeOf(t)) {
            group.put(JSONConstants.MODIFIED_KEY,
                      getDateTimeFormat().print(t.getModificationTime()));
        }
        return group;
    }
}
