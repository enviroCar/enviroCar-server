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
package io.car.server.rest.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.User;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.rest.auth.Authenticated;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupMemberResource extends UserResource {
    private String group;

    @Inject
    public GroupMemberResource(@Assisted("group") String group,
                               @Assisted User member) {
        super(member);
        this.group = group;
    }

    @DELETE
    @Override
    @Authenticated
    public void delete() throws UserNotFoundException, GroupNotFoundException {
        if (!canModifyUser(getUser())) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getService().removeGroupMember(group, getUser());
    }
}
