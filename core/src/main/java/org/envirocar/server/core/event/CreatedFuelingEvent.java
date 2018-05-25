/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.core.event;

import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.User;

import com.google.common.base.Preconditions;

/**
 * Event that gets fired when a new {@code Fueling} is created.
 *
 * @author Christian Autermann
 */
public class CreatedFuelingEvent implements FuelingEvent {
    private final Fueling fueling;
    private final User user;

    /**
     * Creates a new {@code CreatedFuelingEvent} for the specified {@code User}
     * and {@code Fueling}.
     *
     * @param fueling the fueling
     * @param user    the user
     */
    public CreatedFuelingEvent(User user, Fueling fueling) {
        this.fueling = Preconditions.checkNotNull(fueling);
        this.user = Preconditions.checkNotNull(user);
    }

    @Override
    public Fueling getFueling() {
        return this.fueling;
    }

    @Override
    public User getUser() {
        return this.user;
    }

}
