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
import io.car.server.core.exception.ValidationException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Singleton
public class UserService {
    private final UserDao dao;
    private final EntityUpdater<User> updater;
    private final EntityValidator<User> validator;
    private final PasswordEncoder passwordEncoder;
    @Inject
    public UserService(UserDao dao,
                       PasswordEncoder passwordEncoder,
                       EntityUpdater<User> updater,
                       EntityValidator<User> validator) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
        this.updater = updater;
        this.validator = validator;
    }

    public User createUser(User user) throws ValidationException {
        validator.validateCreate(user);
        user.setToken(passwordEncoder.encode(user.getToken()));
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

    public User modifyUser(User user, User changes) throws UserNotFoundException, IllegalModificationException,
                                                           ValidationException {
        validator.validateUpdate(user);
        return this.dao.saveUser(this.updater.update(changes, user));
    }

    public void deleteUser(String username) throws UserNotFoundException {
        deleteUser(getUser(username));
    }

    public void deleteUser(User user) throws UserNotFoundException {
        this.dao.deleteUser(user);
    }

    public void removeFriend(User user, User friend) throws UserNotFoundException {
        this.dao.saveUser(user.removeFriend(getUser(friend.getName())));
    }

    public void addFriend(User user, User friend) throws UserNotFoundException {
        this.dao.saveUser(user.addFriend(getUser(friend.getName())));
    }
}
