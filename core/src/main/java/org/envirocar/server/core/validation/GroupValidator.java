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

import org.envirocar.server.core.entities.Group;

import org.envirocar.server.core.exception.ValidationException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupValidator extends AbstractValidator<Group> {
    @Override
    public void validateCreate(Group t) throws ValidationException {
        isNotNullOrEmpty("name", t.getName());
        isNotNullOrEmpty("description", t.getDescription());
        isNotNull("owner", t.getOwner());
        isNull("created", t.getCreationTime());
        isNull("modified", t.getModificationTime());
    }

    @Override
    public void validateUpdate(Group t) throws ValidationException {
        isNull("name", t.getName());
        isNotEmpty("description", t.getDescription());
        isNull("owner", t.getOwner());
        isNull("created", t.getCreationTime());
        isNull("modified", t.getModificationTime());
    }
}
