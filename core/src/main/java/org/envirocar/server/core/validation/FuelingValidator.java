package org.envirocar.server.core.validation;

import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.exception.ValidationException;

/**
 * {@link EntityValidator} for {@link Fueling}s.
 *
 * @author Christian Autermann
 */
public class FuelingValidator extends AbstractValidator<Fueling> {

    @Override
    public void validateCreate(Fueling t) throws ValidationException {
        isNotNull("user", t.getUser());
        isNotNull("car", t.getCar());
        isNotNull("cost", t.getCost());
        isNotNull("fuelType", t.getFuelType());
        isNotNull("mileage", t.getMileage());
        isNotNull("time", t.getTime());
        isNotNull("volume", t.getVolume());
    }

    @Override
    public void validateUpdate(Fueling t) throws ValidationException {
        throw new UnsupportedOperationException();
    }

}
