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

import io.car.server.core.User;
import io.car.server.core.Users;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.AbstractResource;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.auth.Anonymous;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UsersResource extends AbstractResource {

    @GET
    @Produces(MediaTypes.USERS)
    public Users get(@QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit) {
        return getUserService().getAllUsers(limit);
    }

    @POST
    @Consumes(MediaTypes.USER_CREATE)
    @Anonymous
    public Response create(User user) throws ValidationException, ResourceAlreadyExistException {
        return Response.created(
                getUriInfo().getRequestUriBuilder()
                .path(getUserService().createUser(user).getName())
                .build()).build();
    }

    @Path("{username}")
    public UserResource user(@PathParam("username") String username) throws UserNotFoundException {
        return getResourceFactory().createUserResource(getUserService().getUser(username));
    }
}
