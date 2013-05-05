package io.car.server.core;

import io.car.server.core.exception.IllegalModificationException;

/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class TrackUpdater implements EntityUpdater<Track>{

	@Override
	public Track update(Track changes, Track original)
			throws IllegalModificationException {
		if(changes.getCar() != null)
			original.setCar(changes.getCar());
		if(changes.getBbox() != null)
			original.setBbox(changes.getBbox());
		return original;
	}
}
