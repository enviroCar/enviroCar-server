/*
 * Copyright (C) 2013-2021 The enviroCar project
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

/**
 * Interface for {@code Event}s involving {@link Fueling}s.
 *
 * @author Christian Autermann
 */
public interface FuelingEvent extends UserCausedEvent {
    /**
     * Get the involved {@code Fueling}.
     *
     * @return the {@code Fueling}
     */
    Fueling getFueling();
}
