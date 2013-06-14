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


import javax.ws.rs.DELETE;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.User;
import io.car.server.core.exception.ResourceNotFoundException;
import io.car.server.rest.auth.Authenticated;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class FriendResource extends UserResource {
    private User user;
    @Inject
    public FriendResource(@Assisted("user") User user,
                          @Assisted("friend") User friend) {
        super(friend);
        this.user = user;
    }

    @DELETE
    @Override
    @Authenticated
    public void delete() throws ResourceNotFoundException {
        checkRights(getAccessRights().canUnfriend(user));
        getService().removeFriend(user, getUser());
    }
}
