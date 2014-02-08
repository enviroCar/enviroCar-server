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
