/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package io.car.server.core;


import java.util.regex.Pattern;

import io.car.server.core.exception.ValidationException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UserValidator extends AbstractValidator<User> {
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                                                          "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private final Pattern NAME_PATTERN = Pattern.compile("^[_A-Za-z0-9-]{6,}$");

    @Override
    public void validateCreate(User user) throws ValidationException {
        matches("name", user.getName(), NAME_PATTERN);
        matches("mail", user.getMail(), EMAIL_PATTERN);
        isNotNullOrEmpty("token", user.getToken());
        isNull("created", user.getCreationDate());
        isNull("modified", user.getLastModificationDate());
    }

    @Override
    public void validateUpdate(User user) throws ValidationException {
        isNullOrMatches("name", user.getName(), NAME_PATTERN);
        isNullOrMatches("mail", user.getMail(), EMAIL_PATTERN);
        isNull("token", user.getToken());
        isNull("created", user.getCreationDate());
        isNull("modified", user.getLastModificationDate());
    }
}
