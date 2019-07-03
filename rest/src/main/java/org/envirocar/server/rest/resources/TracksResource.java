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
import com.vividsolutions.jts.geom.GeometryFactory;
import org.envirocar.server.core.SpatialFilter;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.rest.*;
import org.envirocar.server.rest.rights.HasAcceptedLatestLegalPolicies;
import org.envirocar.server.rest.validation.Schema;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class TracksResource extends AbstractResource {
    public static final String TRACK = "{track}";
    private final GeometryFactory factory;

    @Inject
    public TracksResource(GeometryFactory factory) {
        this.factory = factory;
    }

    @GET
    @Schema(response = Schemas.TRACKS)
    @Produces({MediaTypes.TRACKS, MediaTypes.XML_RDF, MediaTypes.TURTLE, MediaTypes.TURTLE_ALT})
    public Tracks get(@QueryParam(RESTConstants.BBOX) BoundingBox bbox) throws BadRequestException {
        SpatialFilter spatialFilter = null;
        if (bbox != null) {
            spatialFilter = SpatialFilter.bbox(bbox.asPolygon(factory));
        }
        return getDataService()
                .getTracks(new TrackFilter(spatialFilter, parseTemporalFilterForInterval(), getPagination()));
    }

    @POST
    @HasAcceptedLatestLegalPolicies
    @Schema(request = Schemas.TRACK_CREATE)
    @Consumes({MediaTypes.TRACK_CREATE})
    public Response create(Track track) throws ValidationException {
        if (track instanceof TrackWithMeasurments) {
            TrackWithMeasurments twm = (TrackWithMeasurments) track;
            track = getDataService().createTrack(twm.getTrack(), twm.getMeasurements());
        } else {
            track = getDataService().createTrack(track);
        }
        return Response.created(getUriInfo().getAbsolutePathBuilder()
                .path(track.getIdentifier()).build()).build();
    }

    @Path(TRACK)
    public TrackResource track(@PathParam("track") String id) throws TrackNotFoundException {
        return getResourceFactory().createTrackResource(getDataService().getTrack(id));
    }
}
