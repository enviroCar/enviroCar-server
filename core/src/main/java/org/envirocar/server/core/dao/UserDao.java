/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.util.pagination.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface UserDao extends Dao {

    /**
     * Try to confirm the the user.
     *
     * @param code The confirmation code of the {@link User}.
     *
     * @return If the user could be confirmed.
     */
    User confirm(String code);

    default User getByName(String name) {
        return getByName(name, false);
    }

    default User getByMail(String mail) {
        return getByMail(mail, false);
    }

    User getByName(String name, boolean includeUnconfirmed);

    User getByMail(String mail, boolean includeUnconfirmed);

    Users get(Pagination p);

    User create(User user);

    User save(User user);

    void delete(User user, boolean deleteContent);

    Users getFriends(User user);

    User getFriend(User user, String friendName);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

    PasswordReset requestPasswordReset(User user) throws BadRequestException;

    void resetPassword(User user, String verificationCode) throws BadRequestException;

    Users getPendingIncomingFriendRequests(User user);

    Users getPendingOutgoingFriendRequests(User user);
}
