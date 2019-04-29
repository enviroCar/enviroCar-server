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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.envirocar.server.core.SpatialFilter;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.exception.*;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.*;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.validation.Schema;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.security.InvalidParameterException;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MeasurementsResource extends AbstractResource {
    public static final String MEASUREMENT = "{measurement}";
    private final Track track;
    private final GeometryFactory geometryFactory;

    @Inject
    public MeasurementsResource(@Assisted @Nullable Track track,
                                GeometryFactory geometryFactory) {
        this.track = track;
        this.geometryFactory = geometryFactory;
    }

    @GET
    @Schema(response = Schemas.MEASUREMENTS)
    @Produces({MediaTypes.MEASUREMENTS})
    public Measurements get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.BBOX) BoundingBox bbox,
            @QueryParam(RESTConstants.NEAR_POINT) NearPoint nearPoint)
            throws TrackNotFoundException, BadRequestException {
    	
    	//check spatial filter
        SpatialFilter sf = null;
        if (bbox != null && nearPoint != null){
        	throw new InvalidParameterException("Only one spatial filter can be applied!");
        } else if (bbox != null) {
            sf = SpatialFilter.bbox(bbox.asPolygon(geometryFactory));
        } else if (nearPoint != null) {
            sf = SpatialFilter.nearPoint(nearPoint.getPoint(),
                                         nearPoint.getDistance());
        }

        return getDataService()
                .getMeasurements(new MeasurementFilter(track, sf,
                                                       parseTemporalFilterForInstant(),
                                                       getPagination()));
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.MEASUREMENT_CREATE)
    @Consumes({ MediaTypes.MEASUREMENT_CREATE })
    public Response create(Measurement measurement) throws
            ResourceAlreadyExistException, ValidationException{
        Measurement m;
        if (track != null) {
            m = getDataService().createMeasurement(track, measurement);
        } else {
            m = getDataService().createMeasurement(measurement);
        }
        return Response.created(
                getUriInfo()
                .getAbsolutePathBuilder()
                .path(m.getIdentifier()).build()).build();
    }

    @Path(MEASUREMENT)
    public MeasurementResource measurement(@PathParam("measurement") String id)
            throws MeasurementNotFoundException {
        Measurement m = getDataService().getMeasurement(id);
        return getResourceFactory().createMeasurementResource(m, track);
    }
}
