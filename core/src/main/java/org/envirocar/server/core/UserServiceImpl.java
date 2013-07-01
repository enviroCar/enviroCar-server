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

import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.dao.ActivityDao;
import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.event.ChangedProfileEvent;
import org.envirocar.server.core.event.DeletedUserEvent;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.ActivityFilter;
import org.envirocar.server.core.update.EntityUpdater;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.core.util.PasswordEncoder;
import org.envirocar.server.core.validation.EntityValidator;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

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

    @Override
    public Activity getActivity(ActivityFilter request, String id) {
        return this.activityDao.get(request, id);
    }
}
