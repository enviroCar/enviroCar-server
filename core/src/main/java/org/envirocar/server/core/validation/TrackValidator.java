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

import org.envirocar.server.core.entities.Track;

import org.envirocar.server.core.exception.ValidationException;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class TrackValidator extends AbstractValidator<Track> {
    @Override
    public void validateCreate(Track t) throws ValidationException {
        isNull("bbox", t.getBoundingBox());
        isNull("created", t.getCreationTime());
        isNull("modified", t.getModificationTime());
        isNotNull("sensor", t.getSensor());
        isNotNull("user", t.getUser());
    }

    @Override
    public void validateUpdate(Track t) throws ValidationException {
        isNull("created", t.getCreationTime());
        isNull("modified", t.getModificationTime());
        isNull("user", t.getUser());
    }
}
