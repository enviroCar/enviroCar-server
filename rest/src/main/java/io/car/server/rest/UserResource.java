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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import io.car.server.core.User;
import io.car.server.core.UserService;
import io.car.server.core.Users;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.rest.auth.Anonymous;
import io.car.server.rest.auth.AuthConstants;
import io.car.server.rest.auth.Authenticated;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UserResource {
    private final UserService service;
    private final UriInfo uriInfo;
    private SecurityContext securityContext;

    @Inject
    public UserResource(UserService service, UriInfo uriInfo, SecurityContext securityContext) {
        this.service = service;
        this.uriInfo = uriInfo;
        this.securityContext = securityContext;
    }

    @GET
    @Produces(MediaTypes.USERS)
    public Users get() {
        return this.service.getAllUsers();
    }

    @POST
    @Consumes(MediaTypes.USER_CREATE)
    @Anonymous
    public Response create(User user) {
        return Response.created(
                uriInfo.getRequestUriBuilder()
                .path(this.service.createUser(user).getName())
                .build()).build();
    }

    @PUT
    @Path("{username}")
    @Consumes(MediaTypes.USER_MODIFY)
    @Authenticated
    public Response modify(@PathParam("username") String user, User changes) throws
            UserNotFoundException, IllegalModificationException {
        if (!canModifyUser(user)) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        User modified = this.service.modifyUser(user, changes);
        if (modified.getName().equals(user)) {
            return Response.noContent().build();
        } else {
            UriBuilder b = uriInfo.getBaseUriBuilder();
            List<PathSegment> pathSegments = uriInfo.getPathSegments();
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
        return this.service.getUser(name);
    }

    @DELETE
    @Path("{username}")
    @Authenticated
    public void delete(@PathParam("username") String name) throws UserNotFoundException {
        if (!canModifyUser(name)) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        this.service.deleteUser(name);
    }

    private boolean canModifyUser(String username) {
        return securityContext.isUserInRole(AuthConstants.ADMIN_ROLE) ||
               (securityContext.getUserPrincipal() != null &&
                securityContext.getUserPrincipal().getName().equals(username));
    }
}
