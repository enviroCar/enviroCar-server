/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import com.google.common.collect.Sets;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.SensorNotFoundException;
import org.envirocar.server.core.filter.PropertyFilter;
import org.envirocar.server.core.filter.SensorFilter;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.validation.Schema;

import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class SensorsResource extends AbstractResource {
    public static final String SENSOR = "{sensor}";

    @GET
    @Schema(response = Schemas.SENSORS)
    @Produces(MediaTypes.SENSORS)
    public Sensors get(
            @QueryParam(RESTConstants.TYPE) String type) throws BadRequestException {
        MultivaluedMap<String, String> queryParameters =
                getUriInfo().getQueryParameters();
        Set<PropertyFilter> filters = Sets.newHashSet();
        for (String key : queryParameters.keySet()) {
            if (key.equals(RESTConstants.LIMIT) ||
                key.equals(RESTConstants.PAGE) ||
                key.equals(RESTConstants.TYPE)) {
                continue;
            }
            List<String> list = queryParameters.get(key);
            for (String value : list) {
                filters.add(new PropertyFilter(key, value));
            }
        }
        return getDataService().getSensors(new SensorFilter(type, filters, getPagination()));
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.SENSOR_CREATE)
    @Consumes({ MediaTypes.SENSOR_CREATE })
    public Response create(Sensor sensor) {
        return Response.created(
                getUriInfo().getAbsolutePathBuilder()
                .path(getDataService().createSensor(sensor).getIdentifier())
                .build()).build();
    }

    @Path(SENSOR)
    public SensorResource sensor(@PathParam("sensor") String id)
            throws SensorNotFoundException {
        Sensor sensor = getDataService().getSensorByName(id);
        return getResourceFactory().createSensorResource(sensor);
    }
}
