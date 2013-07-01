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

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.google.common.collect.Sets;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;

import org.envirocar.server.core.exception.SensorNotFoundException;
import org.envirocar.server.core.filter.PropertyFilter;
import org.envirocar.server.core.filter.SensorFilter;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;

import org.envirocar.server.rest.validation.Schema;

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
    @Produces({ MediaTypes.SENSORS,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Sensors get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.TYPE) String type) {
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
        Pagination p = new Pagination(limit, page);
        return getDataService().getSensors(new SensorFilter(type, filters, p));
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
        checkRights(getRights().canSee(sensor));
        return getResourceFactory().createSensorResource(sensor);
    }
}
