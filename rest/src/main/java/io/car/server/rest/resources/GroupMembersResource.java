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

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.util.Pagination;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupMembersResource extends AbstractResource {
    public static final String MEMBER = "{member}";
    private Group group;

    @Inject
    public GroupMembersResource(@Assisted Group group) {
        this.group = group;
    }

    @GET
    @Schema(response = Schemas.USERS)
    @Produces(MediaTypes.USERS)
    public Users get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page) {
        return getService().getGroupMembers(group, new Pagination(limit, page));
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.USER_REF)
    @Consumes(MediaTypes.USER_REF)
    public void add(User user) throws UserNotFoundException,
                                      GroupNotFoundException {
        User u = getService().getUser(user.getName());
        if (!canModifyUser(u)) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getService().addGroupMember(group, u);
    }

    @Path(MEMBER)
    public GroupMemberResource member(@PathParam("member") String username)
            throws UserNotFoundException {
        User user = getService().getUser(username);
        if (!user.hasGroup(group)) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return getResourceFactory().createGroupMemberResource(group, user);
    }
}
