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
package io.car.server.core;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.util.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface GroupService {
    void addGroupMember(Group group, User user);

    Group createGroup(User user, Group group) throws
            ResourceAlreadyExistException;

    void deleteGroup(Group group) throws
            GroupNotFoundException;

    Group getGroup(String name) throws
            GroupNotFoundException;

    Group getGroup(User user, String groupName) throws
            GroupNotFoundException;

    User getGroupMember(Group group, String username) throws
            UserNotFoundException;

    Users getGroupMembers(Group group, Pagination pagination);

    Groups getGroups(Pagination p);

    Groups getGroups(User user, Pagination p);

    boolean isGroupMember(Group group, User user);

    Group modifyGroup(Group group, Group changes) throws
            ValidationException,
            IllegalModificationException;

    void removeGroupMember(Group group, User user) throws
            UserNotFoundException,
            GroupNotFoundException;

    Groups searchGroups(String search, Pagination p);

    boolean shareGroup(User user, User user0);
}
