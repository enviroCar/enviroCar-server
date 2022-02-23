/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
    public Measurement get() {
        return this.measurement;
    }

    @DELETE
    @Authenticated
    public void delete() {
        checkRights(getRights().canDelete(this.measurement));
        getDataService().deleteMeasurement(this.measurement);
    }

    @Path(SENSOR)
    public SensorResource sensor() {
        checkRights(getRights().canSeeSensorOf(this.measurement));
        Sensor sensor = this.measurement.getSensor();
        checkRights(getRights().canSee(sensor));
        return getResourceFactory().createSensorResource(sensor);
    }
}
