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
package io.car.server.core;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.car.server.core.db.UserDao;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.UserNotFoundException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Singleton
public class UserService {
    private final UserDao dao;
    private final EntityFactory factory;
    private final EntityUpdater<User> updater;

    @Inject
    public UserService(UserDao dao, EntityFactory factory, EntityUpdater<User> updater) {
        this.dao = dao;
        this.factory = factory;
        this.updater = updater;
    }

    public void createUser(String name) {
        this.dao.createUser(factory.createUser().setName(name));
    }

    public User createUser(User user) {
        return this.dao.createUser(user);
    }

    public User getUser(String name) throws UserNotFoundException {
        User user = this.dao.getUserByName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        return user;
    }

    public Users getAllUsers(int limit) {
        return this.dao.getAll(limit);
    }

    public Users getAllUsers() {
        return getAllUsers(0);
    }

    public User modifyUser(String username, User user) throws UserNotFoundException, IllegalModificationException {
        return this.dao.saveUser(this.updater.update(user, getUser(username)));
    }

    public void deleteUser(String username) throws UserNotFoundException {
        this.dao.deleteUser(this.getUser(username));
    }
}
