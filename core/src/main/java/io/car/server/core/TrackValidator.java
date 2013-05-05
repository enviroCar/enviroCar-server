package io.car.server.core;

import io.car.server.core.exception.ValidationException;

/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class TrackValidator extends AbstractValidator<Track> {

	@Override
	public void validateCreate(Track t) throws ValidationException {
		isNotNullOrEmpty("car", t.getCar());
		isNull("bbox", t.getBbox());
		isNull("created", t.getCreationDate());
		isNull("modified", t.getLastModificationDate());
	}

	@Override
	public void validateUpdate(Track t) throws ValidationException {
		isNotEmpty("car", t.getCar());
        isNull("created", t.getCreationDate());
        isNull("modified", t.getLastModificationDate());
	}
	
}
