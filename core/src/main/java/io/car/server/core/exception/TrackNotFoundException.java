package io.car.server.core.exception;

public class TrackNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 9183814565661472011L;

	public TrackNotFoundException(String track) {
		super(String.format("The track '%s' was not found", track));
	}

}
