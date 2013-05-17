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

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.User;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class UserResource extends AbstractResource {
    public static final String GROUPS = "groups";
    public static final String FRIENDS = "friends";
    public static final String TRACKS = "tracks";
    public static final String MEASUREMENTS = "measurements";
    protected final User user;

    @Inject
    public UserResource(@Assisted User user) {
        this.user = Preconditions.checkNotNull(user);
    }

    protected User getUser() {
        return user;
    }

    @PUT
    @Schema(request = Schemas.USER_MODIFY)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response modify(User changes) throws
            UserNotFoundException, IllegalModificationException, ValidationException {
        Preconditions.checkNotNull(user);
        if (!canModifyUser(getUser())) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        User modified = getService().modifyUser(getUser(), changes);
        if (modified.getName().equals(getUser().getName())) {
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

    @GET
    @Schema(response = Schemas.USER)
    @Produces(MediaType.APPLICATION_JSON)
    public User get() throws UserNotFoundException {
        return getUser();
    }

    @DELETE
    @Authenticated
    public void delete() throws UserNotFoundException {
        if (!canModifyUser(getUser())) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getService().deleteUser(getUser());
    }

    @Path(FRIENDS)
    public FriendsResource friends() {
        return getResourceFactory().createFriendsResource(getUser());
    }

    @Path(GROUPS)
    public GroupsResource groups() {
        return getResourceFactory().createGroupsResource(getUser());
    }
    
    @Path(TRACKS)
    public TracksResource tracks(){
    	return getResourceFactory().createTracksResource(getUser());
    }
    
    @Path(MEASUREMENTS)
    public MeasurementsResource measurements(){
    	return getResourceFactory().createMeasurementsResource(getUser());
    }
}
