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
package io.car.server.core;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import io.car.server.core.activities.Activities;
import io.car.server.core.dao.ActivityDao;
import io.car.server.core.dao.UserDao;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.event.ChangedProfileEvent;
import io.car.server.core.event.DeletedUserEvent;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.filter.ActivityFilter;
import io.car.server.core.update.EntityUpdater;
import io.car.server.core.util.Pagination;
import io.car.server.core.util.PasswordEncoder;
import io.car.server.core.validation.EntityValidator;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserServiceImpl implements UserService {
    private final ActivityDao activityDao;
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final EntityValidator<User> userValidator;
    private final EntityUpdater<User> userUpdater;
    private final EventBus eventBus;

    @Inject
    public UserServiceImpl(ActivityDao activityDao,
                           UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           EntityValidator<User> userValidator,
                           EntityUpdater<User> userUpdater,
                           EventBus eventBus) {
        this.activityDao = activityDao;
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.userValidator = userValidator;
        this.userUpdater = userUpdater;
        this.eventBus = eventBus;
    }

    @Override
    public User createUser(User user) throws ValidationException,
                                             ResourceAlreadyExistException {
        userValidator.validateCreate(user);
        if (userDao.getByName(user.getName()) != null) {
            throw new ResourceAlreadyExistException();
        }
        if (userDao.getByMail(user.getMail()) != null) {
            throw new ResourceAlreadyExistException();
        }
        user.setToken(passwordEncoder.encode(user.getToken()));
        return this.userDao.create(user);
    }

    @Override
    public User getUser(String name) throws UserNotFoundException {
        User user = this.userDao.getByName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        return user;
    }

    @Override
    public Users getUsers(Pagination p) {
        return this.userDao.get(p);
    }

    @Override
    public User modifyUser(User user, User changes) throws UserNotFoundException,
                                                           IllegalModificationException,
                                                           ValidationException,
                                                           ResourceAlreadyExistException {
        this.userValidator.validateUpdate(changes);
        if (changes.hasMail() && !changes.getMail().equals(user.getMail())) {
            if (this.userDao.getByMail(changes.getMail()) != null) {
                throw new ResourceAlreadyExistException();
            }
        }
        this.userUpdater.update(changes, user);
        this.userDao.save(user);
        this.eventBus.post(new ChangedProfileEvent(user));
        return user;
    }

    @Override
    public void deleteUser(User user) {
        this.userDao.delete(user);
        this.eventBus.post(new DeletedUserEvent(user));
    }

    @Override
    public Activities getActivities(ActivityFilter request) {
        return this.activityDao.get(request);
    }
}
