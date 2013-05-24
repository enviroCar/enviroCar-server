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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class FriendsResource extends AbstractResource {
    public static final String FRIEND = "{friend}";
    private final User user;

    @Inject
    public FriendsResource(@Assisted User user) {
        this.user = user;
    }

    @GET
    @Schema(response = Schemas.USERS)
    @Produces(MediaTypes.USERS)
    public Users get() {
        return user.getFriends();
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.USER_REF)
    @Consumes(MediaTypes.USER_REF)
    public void add(User friend) throws UserNotFoundException {
        if (friend.getName() == null) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        getService().addFriend(user, friend);
    }

    @DELETE
    @Authenticated
    @Schema(request = Schemas.USER_REF)
    @Consumes(MediaTypes.USER_REF)
    public void remove(User friend) throws UserNotFoundException {
        if (friend.getName() == null) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        getService().removeFriend(user, friend);
    }

    @Path(FRIEND)
    public UserResource friend(@PathParam("friend") String username) throws
            UserNotFoundException {
        //TODO throw 404 for users that are not friends
        return getResourceFactory().createUserResource(getService()
                .getUser(username));
    }
}
