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
}
