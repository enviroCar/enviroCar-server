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
