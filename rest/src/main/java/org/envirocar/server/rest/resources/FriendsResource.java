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
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.UserReference;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class FriendsResource extends AbstractResource {
    public static final String FRIEND = "{friend}";
    public static final String INCOMING_FRIEND_REQUESTS = "incomingRequests";
    public static final String OUTGOING_FRIEND_REQUESTS = "outgoingRequests";
    public static final String DECLINE_FRIEND_REQUEST = "declineRequest";
    private final User user;

    @Inject
    public FriendsResource(@Assisted User user) {
        this.user = user;
    }

    @GET
    @Schema(response = Schemas.USERS)
    public Users get() {
        checkRights(getRights().canSeeFriendsOf(this.user));
        return getFriendService().getFriends(this.user);
    }

    @POST
    @Authenticated
    @Consumes(MediaTypes.JSON)
    @Schema(request = Schemas.USER_REF)
    public void add(UserReference friend) throws UserNotFoundException {
        if (friend.getName() == null || friend.getName().equals(getCurrentUser().getName())) {
            throw new BadRequestException();
        }
        User f = getUserService().getUser(friend.getName());
        checkRights(getRights().canFriend(this.user, f));
        getFriendService().addFriend(this.user, f);
    }

    @Path(FRIEND)
    public FriendResource friend(@PathParam("friend") String friendName) throws UserNotFoundException {
        checkRights(getRights().canSeeFriendsOf(this.user));
        User friends = getFriendService().getFriend(this.user, friendName);
        return getResourceFactory().createFriendResource(this.user, friends);
    }

    @GET
    @Path(INCOMING_FRIEND_REQUESTS)
    public Users pendingIncomingFriendRequests() {
        checkRights(getRights().canSeeFriendsOf(this.user));
        return getFriendService().pendingIncomingRequests(this.user);
    }

    @GET
    @Path(OUTGOING_FRIEND_REQUESTS)
    public Users pendingOutgoingFriendRequests() {
        checkRights(getRights().canSeeFriendsOf(this.user));
        return getFriendService().pendingOutgoingRequests(this.user);
    }

    @POST
    @Path(DECLINE_FRIEND_REQUEST)
    @Schema(request = Schemas.USER_REF)
    @Consumes(MediaTypes.JSON)
    @Authenticated
    public void decline(UserReference friend) throws UserNotFoundException {
        User f = getUserService().getUser(friend.getName());

        if (f == null) {
            throw new UserNotFoundException(friend.getName());
        }

        getFriendService().removeFriend(f, this.user);
    }
}
