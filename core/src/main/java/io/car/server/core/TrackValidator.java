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

import io.car.server.core.entities.Track;
import io.car.server.core.exception.ValidationException;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class TrackValidator extends AbstractValidator<Track> {
    @Override
    public Track validateCreate(Track t) throws ValidationException {
        isNull("bbox", t.getBbox());
        isNull("created", t.getCreationDate());
        isNull("modified", t.getLastModificationDate());
        isNotNull("sensor", t.getSensor());
        isNotNull("user", t.getUser());
        return t;
    }

    @Override
    public Track validateUpdate(Track t) throws ValidationException {
        isNull("created", t.getCreationDate());
        isNull("modified", t.getLastModificationDate());
        isNull("user", t.getUser());
        return t;
    }
}
