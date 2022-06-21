/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import org.envirocar.server.core.entities.Group;

import org.envirocar.server.core.exception.IllegalModificationException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GroupUpdater implements EntityUpdater<Group> {
    @Override
    public void update(Group changes, Group original) throws
            IllegalModificationException {
        if (changes.getDescription() != null) {
            original.setDescription(changes.getDescription());
        }
        if (changes.getName() != null) {
            original.setName(changes.getName());
        }
    }
}
