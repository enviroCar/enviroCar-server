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
import javax.ws.rs.core.Response;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Track;
import io.car.server.core.exception.MeasurementNotFoundException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.AbstractResource;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class MeasurementsResource extends AbstractResource {
    private final Track track;

    public MeasurementsResource() {
        this(null);
    }

    public MeasurementsResource(Track track) {
        this.track = track;
    }

    @GET
    @Produces(MediaTypes.MEASUREMENTS)
    public Measurements get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit) {
        return getService().getAllMeasurements(limit);
    }

    @POST
    @Consumes(MediaTypes.MEASUREMENT_CREATE)
    public Response create(Measurement measurement) throws ResourceAlreadyExistException, ValidationException {

        getService().addMeasurement(track, measurement);

        // TODO XXX not discussed yet
        // return
        // Response.created(getUriInfo().getRequestUriBuilder().path(getService().createMeasurement(measurement.toString()));
        return null;
    }

    @Path("{measurementid}")
    public MeasurementResource measurement(@PathParam("measurementid") String id)
            throws MeasurementNotFoundException {
        return getResourceFactory().createMeasurementResource(
                getService().getMeasurement(id));
    }
}