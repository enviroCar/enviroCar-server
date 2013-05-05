/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
