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
package org.envirocar.server.core.dao;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Groups;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.util.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface GroupDao {
    Group getByName(String name);

    Groups search(String search, Pagination p);

    Groups getByOwner(User owner, Pagination p);

    Groups get(Pagination p);

    Group create(Group group);

    Group save(Group group);

    void delete(Group group);

    Group get(User user, String groupName);

    User getMember(Group group, String username);

    Groups getByMember(User user, Pagination p);

    void removeMember(Group group, User user);

    void addMember(Group group, User user);

    Users getMembers(Group group, Pagination pagination);

    boolean shareGroup(User user, User user0);
}
