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

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.exception.MeasurementNotFoundException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MeasurementsResource extends AbstractResource {
    public static final String MEASUREMENT_PATH = "{measurement}";
    private final Track track;
    private final User user;

    @AssistedInject
    public MeasurementsResource() {
        this(null, null);
    }

    @AssistedInject
    public MeasurementsResource(@Assisted Track track) {
        this(track, null);
    }

    @AssistedInject
    public MeasurementsResource(@Assisted User user) {
        this(null, user);
    }

    @AssistedInject
    public MeasurementsResource(@Assisted Track track, @Assisted User user) {
        this.track = track;
        this.user = user;
    }

    @GET
    @Schema(response = Schemas.MEASUREMENTS)
    @Produces(MediaType.APPLICATION_JSON)
    public Measurements get(@QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit) {
        if (track == null) {
            if (user == null) {
                return getService().getMeasurements(limit);
            } else {
                return getService().getMeasurements(user);
            }
        } else {
            return track.getMeasurements();
        }

    }

    @POST
    @Authenticated
    @Schema(request = Schemas.MEASUREMENT_CREATE)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Measurement measurement) throws ResourceAlreadyExistException, ValidationException,
                                                           UserNotFoundException {
        Measurement m;
        if (track != null) {
            if (!canModifyUser(track.getUser())) {
                throw new WebApplicationException(Status.FORBIDDEN);
            }
            m = getService().createMeasurement(track, measurement.setUser(track.getUser()));
        } else {
            m = getService().createMeasurement(measurement.setUser(getCurrentUser()));
        }
        return Response.created(
                getUriInfo()
                .getRequestUriBuilder()
                .path(m.getIdentifier()).build()).build();
    }

    @Path(MEASUREMENT_PATH)
    public MeasurementResource measurement(@PathParam("measurement") String id) throws MeasurementNotFoundException {
        Measurement m = getService().getMeasurement(id);
        if (track != null) {
            if (!m.getTrack().equals(track)) {
                throw new MeasurementNotFoundException(id);
            }
        }
        return getResourceFactory().createMeasurementResource(m);
    }
}