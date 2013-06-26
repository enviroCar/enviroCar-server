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
package org.envirocar.server.rest.resources;

import javax.ws.rs.DELETE;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;

import org.envirocar.server.core.exception.GroupNotFoundException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.rest.auth.Authenticated;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupMemberResource extends UserResource {
    private Group group;

    @Inject
    public GroupMemberResource(@Assisted Group group,
                               @Assisted User member) {
        super(member);
        this.group = group;
    }

    @DELETE
    @Override
    @Authenticated
    public void delete() throws UserNotFoundException, GroupNotFoundException {
        checkRights(getRights().canLeaveGroup(group));
        getGroupService().removeGroupMember(group, getUser());
    }
}
