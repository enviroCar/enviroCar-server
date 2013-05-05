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

import io.car.server.core.Track;
import io.car.server.core.Tracks;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.TrackNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.AbstractResource;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.auth.Authenticated;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class TracksResource extends AbstractResource {

	@GET
	@Produces(MediaTypes.TRACKS)
	public Tracks get(
			@QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit) {
		return getUserService().getAllTracks();
	}

	@POST
	@Consumes(MediaTypes.TRACK_CREATE)
	@Authenticated
	public Response create(Track track) throws ValidationException,
			ResourceAlreadyExistException {
		// TODO FIXME XXX any unique id instead of carname =C !?
		return Response.created(
				getUriInfo().getRequestUriBuilder()
						.path(getUserService().createTrack(track).getCar())
						.build()).build();
	}
	
	@Path("{trackid}")
	public TrackResource user(@PathParam("trackid") String trackId)  throws TrackNotFoundException {
		return getResourceFactory().createTrackResource(getUserService().getTrack(trackId));
	}
}
