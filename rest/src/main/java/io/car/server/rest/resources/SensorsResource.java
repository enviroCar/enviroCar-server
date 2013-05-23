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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.exception.SensorNotFoundException;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.Schemas;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class SensorsResource extends AbstractResource {
    public static final String SENSOR = "{sensor}";

    @GET
    @Schema(response = Schemas.SENSORS)
    @Produces(MediaTypes.SENSORS)
    public Sensors get() {
        return getService().getSensors();
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.SENSOR_CREATE)
    @Consumes(MediaTypes.SENSOR_CREATE)
    public Response create(Sensor sensor) {
        return Response.created(
                getUriInfo().getRequestUriBuilder()
                .path(getService().createSensor(sensor).getName())
                .build()).build();
    }

    @Path(SENSOR)
    public SensorResource sensor(@PathParam("sensor") String id)
            throws SensorNotFoundException {
        return getResourceFactory().createSensorResource(getService()
                .getSensorByName(id));
    }
}
