/*
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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.Track;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.TrackNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class TrackResource extends AbstractResource {
    public static final String MEASUREMENTS = "measurements";
    public static final String SENSOR = "sensor";
    protected final Track track;

    @Inject
    public TrackResource(@Assisted Track track) {
        this.track = track;
    }

    @PUT
    @Schema(request = Schemas.TRACK_MODIFY)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response modify(Track changes) throws TrackNotFoundException,
                                                 UserNotFoundException,
                                                 IllegalModificationException,
                                                 ValidationException {
        if (!canModifyUser(getCurrentUser())) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getService().modifyTrack(track, changes);
        return Response.ok().build();
    }

    @GET
    @Schema(response = Schemas.TRACK)
    @Produces(MediaType.APPLICATION_JSON)
    public Track get() throws TrackNotFoundException {
        return track;
    }

    @DELETE
    @Authenticated
    public void delete() throws TrackNotFoundException, UserNotFoundException {
        if (!canModifyUser(getCurrentUser())) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getService().deleteTrack(track);
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        return getResourceFactory().createMeasurementsResource(track);
    }

    @Path(SENSOR)
    @Authenticated
    public SensorResource sensor() {
        return getResourceFactory().createSensorResource(track.getSensor());
    }
}
