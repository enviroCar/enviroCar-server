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
package org.envirocar.server.core.validation;

import java.util.regex.Pattern;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.ValidationException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserValidator extends AbstractValidator<User> {
    public static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                     "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern NAME_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-]{4,}$");

    @Override
    public void validateCreate(User user) throws ValidationException {
        matches("name", user.getName(), NAME_PATTERN);
        matches("mail", user.getMail(), EMAIL_PATTERN);
        isNotNullOrEmpty("token", user.getToken());
        isNull("created", user.getCreationTime());
        isNull("modified", user.getModificationTime());
    }

    @Override
    public void validateUpdate(User user) throws ValidationException {
        isNull("name", user.getName());
        isNullOrMatches("mail", user.getMail(), EMAIL_PATTERN);
        isNotEmpty("token", user.getToken());
        isNull("created", user.getCreationTime());
        isNull("modified", user.getModificationTime());
    }
}
