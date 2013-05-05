package io.car.server.rest.resources;

import javax.ws.rs.DELETE;

import io.car.server.core.Track;
import io.car.server.rest.AbstractResource;
import io.car.server.rest.auth.Authenticated;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TrackResource extends AbstractResource {
	protected final Track track;
	
	@Inject
	public TrackResource(@Assisted Track track){
		this.track = track;
	}
	
//	protected User getUser(){
//		return user;
//	}
	
//	@PUT
//	@Consumes(MediaTypes.TRACK_MODIFY)
//	@Authenticated
//	public Response 
//	@DELETE
//	@Authenticated
//	public void delete() throws TrackNotFoundException(){
//		if(!)
//	}
}
