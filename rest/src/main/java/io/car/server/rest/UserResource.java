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
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import io.car.server.core.User;
import io.car.server.core.UserService;
import io.car.server.core.Users;
import io.car.server.core.exception.UserNotFoundException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UserResource {
    private final UserService service;
    private final UriInfo uriInfo;

    @Inject
    public UserResource(UserService service, UriInfo uriInfo) {
        this.service = service;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces(MediaTypes.USERS)
    public Users get() {
        return this.service.getAllUsers();
    }

    @POST
    @Consumes(MediaTypes.USER_CREATE)
    public Response create(User user) {
        return Response.created(
                uriInfo.getRequestUriBuilder()
                .path(this.service.createUser(user).getName())
                .build()).build();
    }

    @PUT
    @Path("{username}")
    @Consumes(MediaTypes.USER_MODIFY)
    public Response modify(@PathParam("username") String username, User user) throws UserNotFoundException {
        User modified = this.service.modifyUser(username, user);
        if (modified.getName().equals(username)) {
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
    public User get(@PathParam("username") String name) throws UserNotFoundException {
        return this.service.getUser(name);
    }

    @DELETE
    @Path("{username}")
    public void delete(@PathParam("username") String name) throws UserNotFoundException {
        this.service.deleteUser(name);
    }
}
