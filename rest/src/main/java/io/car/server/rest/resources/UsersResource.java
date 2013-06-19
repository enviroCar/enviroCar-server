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

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.util.Pagination;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Anonymous;
import io.car.server.rest.validation.Schema;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UsersResource extends AbstractResource {
    public static final String USER = "{username}";

    @GET
    @Schema(response = Schemas.USERS)
    @Produces({ MediaTypes.USERS })
    public Users get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page) {
        checkRights(getRights().canSeeUsers());
        return getUserService().getUsers(new Pagination(limit, page));
    }

    @POST
    @Anonymous
    @Schema(request = Schemas.USER_CREATE)
    @Consumes({ MediaTypes.USER_CREATE })
    public Response create(User user) throws ValidationException,
                                             ResourceAlreadyExistException {
        return Response.created(
                getUriInfo().getAbsolutePathBuilder()
                .path(getUserService().createUser(user).getName())
                .build()).build();
    }

    @Path(USER)
    public UserResource user(@PathParam("username") String username) throws
            UserNotFoundException {
        User user = getUserService().getUser(username);
        checkRights(getRights().canSee(user));
        return getResourceFactory().createUserResource(user);
    }
}
