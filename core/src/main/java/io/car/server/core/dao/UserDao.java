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
package io.car.server.core.dao;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface UserDao {
    User getByName(String name);

    User getByMail(String mail);

    Users get();

    Users get(int limit);

    User create(User user);

    User save(User user);

    void delete(User user);

    Users getByGroup(Group group);

    Users getByTrack(Track track);
}
