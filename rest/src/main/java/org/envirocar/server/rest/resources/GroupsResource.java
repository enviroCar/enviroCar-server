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

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Groups;
import org.envirocar.server.core.entities.User;

import org.envirocar.server.core.exception.GroupNotFoundException;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;

import org.envirocar.server.rest.validation.Schema;

/**
 * TODO JavaDoc
 *
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
    @Produces({ MediaTypes.GROUPS,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Groups get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.SEARCH) String search) {
        Pagination p = new Pagination(limit, page);
        if (user != null) {
            return getGroupService().getGroups(user, p);
        } else {
            if (search != null && !search.trim().isEmpty()) {
                return getGroupService().searchGroups(search, p);
            } else {
                return getGroupService().getGroups(p);
            }
        }
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.GROUP_CREATE)
    @Consumes({ MediaTypes.GROUP_CREATE })
    public Response createGroup(Group group) throws UserNotFoundException,
                                                    ResourceAlreadyExistException,
                                                    ValidationException {
        Group g = getGroupService().createGroup(getCurrentUser(), group);
        return Response.created(getUriInfo().getAbsolutePathBuilder().path(g
                .getName()).build()).build();
    }

    @Path(GROUP)
    public GroupResource group(@PathParam("group") String groupName) throws
            GroupNotFoundException {
        Group group;
        if (user != null) {
            group = getGroupService().getGroup(user, groupName);
        } else {
            group = getGroupService().getGroup(groupName);
        }
        checkRights(getRights().canSee(group));
        return getResourceFactory().createGroupResource(group);
    }
}
