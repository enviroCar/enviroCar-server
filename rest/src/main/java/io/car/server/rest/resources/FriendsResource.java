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

import io.car.server.core.User;
import io.car.server.core.Users;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.rest.AbstractResource;
import io.car.server.rest.MediaTypes;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class FriendsResource extends AbstractResource {
    private final User user;

    @Inject
    public FriendsResource(@Assisted User user) {
        this.user = user;
    }

    @GET
    @Produces(MediaTypes.USERS)
    public Users get() {
        return new Users(user.getFriends());
    }

    @POST
    @Consumes(MediaTypes.USER_REF)
    public void add(User friend) throws UserNotFoundException {
        if (friend.getName() == null) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        getUserService().addFriend(user, friend);
    }

    @DELETE
    @Consumes(MediaTypes.USER_REF)
    public void remove(User friend) throws UserNotFoundException {
        if (friend.getName() == null) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        getUserService().removeFriend(user, friend);
    }

    @Path("{username}")
    public UserResource friend(@PathParam("username") String username) throws UserNotFoundException {
        return getResourceFactory().createUserResource(getUserService().getUser(username));
    }
}
