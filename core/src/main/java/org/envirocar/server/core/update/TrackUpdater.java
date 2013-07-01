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
package org.envirocar.server.core.update;

import org.envirocar.server.core.entities.Track;

import org.envirocar.server.core.exception.IllegalModificationException;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class TrackUpdater implements EntityUpdater<Track> {
    @Override
    public void update(Track changes, Track original)
            throws IllegalModificationException {
        if (changes.getBoundingBox() != null) {
            original.setBoundingBox(changes.getBoundingBox());
        }
    }
}
