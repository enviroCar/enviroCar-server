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
package org.envirocar.server.core.dao;

import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Fuelings;
import org.envirocar.server.core.filter.FuelingFilter;

/**
 * DAO for {@link Fueling}s.
 *
 * @author Christian Autermann
 */
public interface FuelingDao {

    /**
     * Get the {@code Fueling} with the specified identifier.
     *
     * @param identifier the identifier
     *
     * @return the {@code Fueling}
     */
    Fueling getById(String identifier);

    /**
     * Save the supplied {@code Fueling}.
     *
     * @param fueling the fueling
     *
     * @return the {@code Fueling}
     */
    Fueling create(Fueling fueling);

    /**
     * Get the {@code Fueling}s matching the specified filter.
     *
     * @param request the request
     *
     * @return the {@code Fuelings}
     */
    Fuelings get(FuelingFilter request);

    public void delete(Fueling fueling);
}
