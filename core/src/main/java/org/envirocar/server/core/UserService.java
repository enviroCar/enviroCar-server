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
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.ActivityFilter;
import org.envirocar.server.core.util.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface UserService {
    User createUser(User user)
            throws ValidationException,
                   ResourceAlreadyExistException;

    Users getUsers(Pagination p);

    User getUser(String name) throws
            UserNotFoundException;

    void deleteUser(User user);

    User modifyUser(User user, User changes)
            throws UserNotFoundException,
                   IllegalModificationException,
                   ValidationException,
                   ResourceAlreadyExistException;

    Activities getActivities(ActivityFilter request);

    Activity getActivity(ActivityFilter request, String id);
}
