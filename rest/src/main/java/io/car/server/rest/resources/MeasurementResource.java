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
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Sensor;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.MeasurementNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class MeasurementResource extends AbstractResource {
    public static final String SENSOR = "sensor";
    protected final Measurement measurement;

    @Inject
    public MeasurementResource(@Assisted Measurement measurement) {
        this.measurement = measurement;
    }

    @PUT
    @Schema(request = Schemas.MEASUREMENT_MODIFY)
    @Consumes(MediaTypes.MEASUREMENT_MODIFY)
    @Authenticated
    public Response modify(Measurement changes)
            throws MeasurementNotFoundException, UserNotFoundException,
                   ValidationException, IllegalModificationException {
        checkRights(getRights().canModify(measurement));
        getDataService().modifyMeasurement(measurement, changes);
        return Response.ok().build();
    }

    @GET
    @Schema(response = Schemas.MEASUREMENT)
    @Produces(MediaTypes.MEASUREMENT)
    public Measurement get() throws MeasurementNotFoundException {
        return measurement;
    }

    @DELETE
    @Authenticated
    public void delete() throws MeasurementNotFoundException,
                                UserNotFoundException {
        checkRights(getRights().canDelete(measurement));
        getDataService().deleteMeasurement(measurement);
    }

    @Path(SENSOR)
    public SensorResource sensor() throws MeasurementNotFoundException {
        checkRights(getRights().canSeeSensorOf(measurement));
        Sensor sensor = measurement.getSensor();
        checkRights(getRights().canSee(sensor));
        return getResourceFactory().createSensorResource(sensor);
    }
}
