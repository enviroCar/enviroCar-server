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
package io.car.server.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.User;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.auth.Authenticated;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class GroupsResource extends AbstractResource {
    public static final String GROUP_PATH = "{group}";
    private User user;

    @AssistedInject
    public GroupsResource(@Assisted User user) {
        this.user = user;
    }

    @AssistedInject
    public GroupsResource() {
        this(null);
    }

    @GET
    @Produces(MediaTypes.GROUPS)
    public Groups get(@QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
                      @QueryParam(RESTConstants.SEARCH) String search) {
        if (user == null) {
            if (search != null && !search.trim().isEmpty()) {
                return getService().searchGroups(search, limit);
            } else {
                return getService().getGroups(limit);
            }
        } else {
            return getService().getGroupsOfUser(user, limit);
        }
    }

    @POST
    @Consumes(MediaTypes.GROUP_CREATE)
    @Authenticated
    public Response createGroup(Group group) throws UserNotFoundException, ResourceAlreadyExistException,
                                                    ValidationException {
        Group g = getService().createGroup(group.setOwner(getCurrentUser()).addMember(getCurrentUser()));
        return Response.created(getUriInfo().getRequestUriBuilder().path(g.getName()).build()).build();
    }

    @Path(GROUP_PATH)
    public GroupResource group(@PathParam("group") String groupname) throws GroupNotFoundException {
        return getResourceFactory().createGroupResource(getService().getGroup(groupname));
    }
}
