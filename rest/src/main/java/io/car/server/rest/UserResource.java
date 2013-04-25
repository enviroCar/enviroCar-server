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
package io.car.server.rest;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import io.car.server.core.User;
import io.car.server.core.Users;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.auth.Anonymous;
import io.car.server.rest.auth.Authenticated;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UserResource extends AbstractResource {

    @GET
    @Produces(MediaTypes.USERS)
    public Users get(@QueryParam("limit") @DefaultValue(value = "0") int limit) {
        return getUserService().getAllUsers(limit);
    }

    @POST
    @Consumes(MediaTypes.USER_CREATE)
    @Anonymous
    public Response create(User user) throws ValidationException {
        return Response.created(
                getUriInfo().getRequestUriBuilder()
                .path(getUserService().createUser(user).getName())
                .build()).build();
    }

    @PUT
    @Path("{username}")
    @Consumes(MediaTypes.USER_MODIFY)
    @Authenticated
    public Response modify(@PathParam("username") String user, User changes) throws
            UserNotFoundException, IllegalModificationException, ValidationException {
        if (!canModifyUser(user)) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        User modified = getUserService().modifyUser(user, changes);
        if (modified.getName().equals(user)) {
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
    @Path("{username}")
    @Produces(MediaTypes.USER)
    @Authenticated
    public User get(@PathParam("username") String name) throws UserNotFoundException {
        return getUserService().getUser(name);
    }

    @DELETE
    @Path("{username}")
    @Authenticated
    public void delete(@PathParam("username") String name) throws UserNotFoundException {
        if (!canModifyUser(name)) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getUserService().deleteUser(name);
    }
}
