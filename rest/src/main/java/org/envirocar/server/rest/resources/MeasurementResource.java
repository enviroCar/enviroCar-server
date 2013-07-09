/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.rest.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.exception.MeasurementNotFoundException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.validation.Schema;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MeasurementResource extends AbstractResource {
    public static final String SENSOR = "sensor";
    protected final Measurement measurement;

    @Inject
    public MeasurementResource(@Assisted Measurement measurement) {
        this.measurement = measurement;
    }

    @GET
    @Schema(response = Schemas.MEASUREMENT)
    @Produces({ MediaTypes.MEASUREMENT,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
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
