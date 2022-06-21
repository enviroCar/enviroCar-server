/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.exception.GroupNotFoundException;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Iterator;
import java.util.List;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupResource extends AbstractResource {
    public static final String MEMBERS = "members";
    public static final String ACTIVITIES = "activities";
    private final Group group;

    @Inject
    public GroupResource(@Assisted Group group) {
        this.group = group;
    }

    @GET
    @Schema(response = Schemas.GROUP)
    public Group get() {
        return this.group;
    }

    @PUT
    @Authenticated
    @Consumes(MediaTypes.JSON)
    @Schema(request = Schemas.GROUP_MODIFY)
    public Response modify(Group changes) throws ValidationException, IllegalModificationException {
        checkRights(getRights().canModify(this.group));
        Group modified = getGroupService().modifyGroup(this.group, changes);
        if (modified.getName().equals(this.group.getName())) {
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
    public void delete() throws GroupNotFoundException {
        checkRights(getRights().canDelete(this.group));
        getGroupService().deleteGroup(this.group);
    }

    @Path(MEMBERS)
    public GroupMembersResource members() {
        return getResourceFactory().createGroupMembersResource(this.group);
    }

    @Path(ACTIVITIES)
    public ActivitiesResource activities() {
        checkRights(getRights().canSeeActivitiesOf(this.group));
        return getResourceFactory().createActivitiesResource(this.group);
    }
}
