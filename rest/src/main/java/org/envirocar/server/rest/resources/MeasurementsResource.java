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

import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.MeasurementNotFoundException;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.rest.BoundingBox;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.validation.Schema;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MeasurementsResource extends AbstractResource {
    public static final String MEASUREMENT = "{measurement}";
    private final Track track;
    private final User user;
    private final GeometryFactory geometryFactory;

    @Inject
    public MeasurementsResource(@Assisted @Nullable Track track,
                                @Assisted @Nullable User user,
                                GeometryFactory geometryFactory) {
        this.track = track;
        this.user = user;
        this.geometryFactory = geometryFactory;
    }

    @GET
    @Schema(response = Schemas.MEASUREMENTS)
    @Produces({ MediaTypes.MEASUREMENTS,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Measurements get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.BBOX) BoundingBox bbox)
            throws UserNotFoundException, TrackNotFoundException {
        Polygon poly = null;
        if (bbox != null) {
            poly = bbox.asPolygon(geometryFactory);
        }
        return getDataService()
                .getMeasurements(new MeasurementFilter(track, user, poly,
                                                       parseTemporalFilterForInstant(),
                                                       new Pagination(limit, page)));
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.MEASUREMENT_CREATE)
    @Consumes({ MediaTypes.MEASUREMENT_CREATE })
    public Response create(Measurement measurement) throws
            ResourceAlreadyExistException, ValidationException,
            UserNotFoundException {
        Measurement m;
        if (track != null) {
            checkRights(getRights().canModify(track));
            measurement.setUser(getCurrentUser());
            m = getDataService().createMeasurement(track, measurement);
        } else {
            measurement.setUser(getCurrentUser());
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
        if (user != null) {
            checkRights(getRights().canSeeMeasurementsOf(user));
        }
        if (track != null) {
            checkRights(getRights().canSeeMeasurementsOf(track));
        }

        Measurement m = getDataService().getMeasurement(id);
        checkRights(getRights().canSee(m));
        return getResourceFactory().createMeasurementResource(m, user, track);
    }
}
