package io.car.server.core.exception;

public class MeasurementNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 8868660032711428006L;

	public MeasurementNotFoundException(String measurement) {
		super(String.format("The group '%s' was not found", measurement));
	}

}
