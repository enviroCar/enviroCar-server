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

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.Group;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupResource extends AbstractResource {
    public static final String MEMBERS = "members";
    public static final String ACTIVITIES = "activities";
    private Group group;

    @Inject
    public GroupResource(@Assisted Group group) {
        this.group = group;
    }

    @GET
    @Schema(response = Schemas.GROUP)
    @Produces(MediaTypes.GROUP)
    public Group get() throws GroupNotFoundException {
        return group;
    }

    @PUT
    @Authenticated
    @Schema(request = Schemas.GROUP_MODIFY)
    @Consumes(MediaTypes.GROUP_MODIFY)
    public Response modify(Group changes) throws UserNotFoundException,
                                                 ValidationException,
                                                 IllegalModificationException,
                                                 GroupNotFoundException {
        checkRights(getRights().canModify(group));
        Group modified = getGroupService().modifyGroup(group, changes);
        if (modified.getName().equals(group.getName())) {
            return Response.noContent().build();
        } else {
            UriBuilder b = getUriInfo().getBaseUriBuilder();
            List<PathSegment> pathSegments = getUriInfo().getPathSegments();
            Iterator<PathSegment> ps = pathSegments.iterator();
            for (int i = 0; i < pathSegments.size() - 1; ++i) {
                b.path(ps.next().getPath());
            }
            return Response.seeOther(b.path(modified.getName()).build()).build();
        }
    }

    @DELETE
    @Authenticated
    public void delete() throws UserNotFoundException, GroupNotFoundException {
        checkRights(getRights().canDelete(group));
        getGroupService().deleteGroup(group);
    }

    @Path(MEMBERS)
    public GroupMembersResource members() {
        return getResourceFactory().createGroupMembersResource(group);
    }

    @Path(ACTIVITIES)
    public ActivitiesResource activities() {
        checkRights(getRights().canSeeActivitiesOf(group));
        return getResourceFactory().createActivitiesResource(group);
    }
}
