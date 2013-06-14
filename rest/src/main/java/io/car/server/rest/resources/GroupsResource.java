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

import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.User;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.util.Pagination;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupsResource extends AbstractResource {
    public static final String GROUP = "{group}";
    private User user;

    @Inject
    public GroupsResource(@Assisted @Nullable User user) {
        this.user = user;
    }

    @GET
    @Schema(response = Schemas.GROUPS)
    @Produces(MediaTypes.GROUPS)
    public Groups get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.SEARCH) String search) {
        Pagination p = new Pagination(limit, page);
        if (user != null) {
            return getService().getGroups(user, p);
        } else {
            if (search != null && !search.trim().isEmpty()) {
                return getService().searchGroups(search, p);
            } else {
                return getService().getGroups(p);
            }
        }
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.GROUP_CREATE)
    @Consumes(MediaTypes.GROUP_CREATE)
    public Response createGroup(Group group) throws UserNotFoundException,
                                                    ResourceAlreadyExistException,
                                                    ValidationException {
        Group g = getService().createGroup(getCurrentUser(), group);
        return Response.created(getUriInfo().getAbsolutePathBuilder().path(g
                .getName()).build()).build();
    }

    @Path(GROUP)
    public GroupResource group(@PathParam("group") String groupName) throws
            GroupNotFoundException {
        Group group;
        if (user != null) {
            group = getService().getGroup(user, groupName);
        } else {
            group = getService().getGroup(groupName);
        }
        checkRights(getRights().canSee(group));
        return getResourceFactory().createGroupResource(group);
    }
}
