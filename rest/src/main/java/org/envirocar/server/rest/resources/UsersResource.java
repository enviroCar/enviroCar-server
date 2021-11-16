/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Anonymous;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UsersResource extends AbstractResource {

    public static final String USER = "{username}";

    @GET
    @Schema(response = Schemas.USERS)
    @Produces({MediaTypes.JSON, MediaTypes.XML_RDF, MediaTypes.TURTLE, MediaTypes.TURTLE_ALT})
    public Users get() throws BadRequestException {
        checkRights(getRights().canSeeUsers());
        return getUserService().getUsers(getPagination());
    }

    @POST
    @Anonymous
    @Schema(request = Schemas.USER_CREATE)
    @Consumes({MediaTypes.JSON})
    public Response create(User user) throws ValidationException, ResourceAlreadyExistException {
//        checkMail(user);
        user = getUserService().createUser(user);
        return Response.created(getUriInfo().getAbsolutePathBuilder().path(user.getName()).build()).build();
    }

    @Path(USER)
    public UserResource user(@PathParam("username") String username) throws UserNotFoundException {
        User user = getUserService().getUser(username);
        checkRights(getRights().canSee(user));
        return getResourceFactory().createUserResource(user);
    }
}
