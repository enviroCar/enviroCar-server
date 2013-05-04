package io.car.server.rest.resources;

import io.car.server.core.Track;
import io.car.server.rest.AbstractResource;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TrackResource extends AbstractResource {
	protected final Track track;
	
	@Inject
	public TrackResource(@Assisted Track track){
		this.track = track;
	}
	
	protected Track getUser(){
		return track;
	}
	
//	@PUT
//	@Consumes(MediaTypes.TRACK_MODIFY);
}
