/**
 * Copyright (C) 2013 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk 52 North Initiative for Geospatial Open Source Software GmbH Martin-Luther-King-Weg 24 48155
 * Muenster, Germany info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under the terms of the GNU General Public
 * License version 2 as published by the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied WARRANTY OF MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program (see gnu-gpl v2.txt). If
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit
 * the Free Software Foundation web page, http://www.fsf.org.
 */
package io.car.server.core;

import java.util.regex.Pattern;

import io.car.server.core.exception.ValidationException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public abstract class AbstractValidator<T> implements EntityValidator<T> {
    protected void isNotNull(String name, Object o) throws ValidationException {
        if (o == null) {
            throw new ValidationException(String.format("missing %s", name));
        }
    }

    protected void isNotNullOrEmpty(String name, String o) throws ValidationException {
        isNotNull(name, o);
        if (o.isEmpty()) {
            throw new ValidationException(String.format("invalid %s", name));
        }
    }

    protected void isNotEmpty(String name, String o) throws ValidationException {
        if (o != null && o.isEmpty()) {
            throw new ValidationException(String.format("invalid %s", name));
        }
    }

    protected void isNull(String name, Object o) throws ValidationException {
        if (o != null) {
            throw new ValidationException(String.format("%s may not be changed", name));
        }
    }

    protected void isNullOrMatches(String name, String o, Pattern pattern) throws ValidationException {
        if (o != null) {
            matches(name, o, pattern);
        }
    }

    protected void matches(String name, String o, Pattern pattern) throws ValidationException {
        isNotNull(name, o);
        if (!pattern.matcher(o).matches()) {
            throw new ValidationException(String
                    .format("Invalid string property %s: %s does not match %s", name, o, pattern));
        }
    }
}
