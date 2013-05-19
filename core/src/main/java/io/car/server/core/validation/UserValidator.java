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
package io.car.server.core.validation;

import java.util.regex.Pattern;

import io.car.server.core.entities.User;
import io.car.server.core.exception.ValidationException;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserValidator extends AbstractValidator<User> {
    private final Pattern EMAIL_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                     "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private final Pattern NAME_PATTERN = Pattern.compile("^[_A-Za-z0-9-]{6,}$");

    @Override
    public User validateCreate(User user) throws ValidationException {
        matches("name", user.getName(), NAME_PATTERN);
        matches("mail", user.getMail(), EMAIL_PATTERN);
        isNotNullOrEmpty("token", user.getToken());
        isNull("created", user.getCreationDate());
        isNull("modified", user.getLastModificationDate());
        return user;
    }

    @Override
    public User validateUpdate(User user) throws ValidationException {
        isNullOrMatches("name", user.getName(), NAME_PATTERN);
        isNullOrMatches("mail", user.getMail(), EMAIL_PATTERN);
        isNull("token", user.getToken());
        isNull("created", user.getCreationDate());
        isNull("modified", user.getLastModificationDate());
        return user;
    }
}
