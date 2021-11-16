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
package org.envirocar.server.core.mail;

import org.envirocar.server.core.entities.User;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class UserMail implements Mail {

    private final String address;
    private final String name;

    public UserMail(User user) {
        this.address = user.getMail();
        this.name = getRecipentName(user);
    }

    @Override
    public String getRecipientAddress() {
        return this.address;
    }

    @Override
    public String getRecipientName() {
        return this.name;
    }


    public static String getRecipentName(User user) {
        if (Strings.isNullOrEmpty(user.getFirstName())) {
            return user.getName();
        } else if (Strings.isNullOrEmpty(user.getLastName())) {
            return user.getFirstName();
        } else {
            return String.format("%s %s", user.getFirstName(), user.getLastName());
        }
    }
}
