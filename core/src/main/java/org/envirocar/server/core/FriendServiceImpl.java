/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.core;

import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.event.FriendedUserEvent;
import org.envirocar.server.core.event.UnfriendedUserEvent;
import org.envirocar.server.core.exception.UserNotFoundException;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class FriendServiceImpl implements FriendService {
    private final EventBus eventBus;
    private final UserDao userDao;

    @Inject
    public FriendServiceImpl(EventBus eventBus, UserDao userDao) {
        this.eventBus = eventBus;
        this.userDao = userDao;
    }

    @Override
    public void removeFriend(User user, User friend) {
        this.userDao.removeFriend(user, friend);
        this.eventBus.post(new UnfriendedUserEvent(user, friend));
    }

    @Override
    public void addFriend(User user, User friend) {
        this.userDao.addFriend(user, friend);
        this.eventBus.post(new FriendedUserEvent(user, friend));
    }

    @Override
    public Users getFriends(User user) {
        return this.userDao.getFriends(user);
    }

    @Override
    public User getFriend(User user, String friendName) throws
            UserNotFoundException {
        User f = this.userDao.getFriend(user, friendName);
        if (f == null) {
            throw new UserNotFoundException(friendName);
        }
        return f;
    }

    @Override
    public boolean isFriend(User user1, User user2) {
        return this.userDao.getFriend(user1, user2.getName()) != null;
    }
}
