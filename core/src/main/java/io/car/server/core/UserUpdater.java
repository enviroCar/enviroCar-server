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

import io.car.server.core.exception.IllegalModificationException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UserUpdater implements EntityUpdater<User> {

    @Override
    public User update(User changes, User original) throws IllegalModificationException {
        if (changes.getToken() != null) {
            throw new IllegalModificationException("token", "tokens may not be changed");
        }
        if (changes.getName() != null) {
            original.setName(changes.getName());
        }
        if (changes.getMail() != null) {
            original.setMail(changes.getMail());
        }
        return original;
    }
}
