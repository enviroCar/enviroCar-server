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
import org.envirocar.server.core.SpatialFilter;
import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.rest.BoundingBox;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.TrackWithMeasurments;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.schema.Schema;
import org.locationtech.jts.geom.GeometryFactory;

import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class TracksResource extends AbstractResource {
    public static final String TRACK = "{track}";
    private final User user;
    private final GeometryFactory factory;

    @Inject
    public TracksResource(@Assisted @Nullable User user, GeometryFactory factory) {
        this.user = user;
        this.factory = factory;
    }

    @GET
    @Schema(response = Schemas.TRACKS)
    public Tracks get(@QueryParam(RESTConstants.BBOX) BoundingBox bbox,
                      @QueryParam(RESTConstants.STATUS) TrackStatus status) throws BadRequestException {
        SpatialFilter spatialFilter = null;
        if (bbox != null) {
            spatialFilter = SpatialFilter.bbox(bbox.asPolygon(this.factory));
        }
        TemporalFilter temporalFilter = parseTemporalFilterForInterval();
        if (status == null) {
            status = TrackStatus.FINISHED;
        }
        TrackFilter filter = new TrackFilter(this.user, spatialFilter, temporalFilter, status, getPagination());
        return getDataService().getTracks(filter);
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.TRACK_CREATE)
    @Consumes(MediaTypes.JSON)
    public Response create(Track track) throws ValidationException {
        if (this.user != null) {
            checkRights(getRights().isSelf(this.user));
        }
        track.setUser(getCurrentUser());

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
        Track track = getDataService().getTrack(id);
        checkRights(getRights().canSee(track));
        return getResourceFactory().createTrackResource(track);
    }
}
