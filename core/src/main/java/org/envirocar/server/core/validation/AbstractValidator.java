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

import java.util.Collection;
import java.util.regex.Pattern;

import org.envirocar.server.core.exception.ValidationException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractValidator<T> implements EntityValidator<T> {
    protected void isNotNull(String name, Object o) throws ValidationException {
        if (o == null) {
            throw new ValidationException(String.format("missing %s", name));
        }
    }

    protected void isNotNullOrEmpty(String name, String o) throws
            ValidationException {
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
            throw new ValidationException(String
                    .format("%s may not be changed", name));
        }
    }

    protected void isNullOrMatches(String name, String o, Pattern pattern)
            throws ValidationException {
        if (o != null) {
            matches(name, o, pattern);
        }
    }

    protected void isEmpty(String name, Collection<?> o) throws
            ValidationException {
        if (o != null && !o.isEmpty()) {
            throw new ValidationException(String.format("%s is not empty", name));
        }
    }

    protected void isEmpty(String name, Iterable<?> o) throws
            ValidationException {
        if (o != null && !o.iterator().hasNext()) {
            throw new ValidationException(String.format("%s is not empty", name));
        }
    }

    protected void matches(String name, String o, Pattern pattern) throws
            ValidationException {
        isNotNull(name, o);
        if (!pattern.matcher(o).matches()) {
            throw new ValidationException(String
                    .format("Invalid string property %s: %s does not match %s", name, o, pattern));
        }
    }
}
