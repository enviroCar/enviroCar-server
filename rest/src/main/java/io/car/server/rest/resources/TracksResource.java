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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.TrackNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.util.Pagination;
import io.car.server.rest.BoundingBox;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.Schemas;
import io.car.server.rest.TrackWithMeasurments;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class TracksResource extends AbstractResource {
    public static final String TRACK = "{track}";
    private final User user;
    private final GeometryFactory factory;

    @Inject
    public TracksResource(@Assisted @Nullable User user,
                          GeometryFactory factory) {
        this.user = user;
        this.factory = factory;
    }

    @GET
    @Schema(response = Schemas.TRACKS)
    @Produces(MediaTypes.TRACKS)
    public Tracks get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.BBOX) BoundingBox bbox)
            throws UserNotFoundException {
        Pagination p = new Pagination(limit, page);
        if (bbox != null) {
            Polygon poly = bbox.asPolygon(factory);
            return user != null
                   ? getService().getTracksByBbox(poly, user, p)
                   : getService().getTracksByBbox(poly, p);
        } else {
            return user != null
                   ? getService().getTracks(user, p)
                   : getService().getTracks(p);
        }
    }

    @POST
    @Schema(request = Schemas.TRACK_CREATE)
    @Consumes(MediaTypes.TRACK_CREATE)
    @Authenticated
    public Response create(Track track) throws ValidationException,
                                               ResourceAlreadyExistException,
                                               UserNotFoundException {
        if (user != null) {
            checkRights(getRights().isSelf(user));
        }
        track.setUser(getCurrentUser());

        if (track instanceof TrackWithMeasurments) {
            TrackWithMeasurments twm = (TrackWithMeasurments) track;
            track = getService().createTrack(twm.getTrack());

            for (Measurement m : twm.getMeasurements()) {
                m.setUser(getCurrentUser());
                getService().createMeasurement(twm.getTrack(), m);
            }

        } else {
            track = getService().createTrack(track);
        }
        return Response.created(getUriInfo().getAbsolutePathBuilder()
                .path(track.getIdentifier()).build()).build();
    }

    @Path(TRACK)
    public TrackResource track(@PathParam("track") String id)
            throws TrackNotFoundException {
        Track track = getService().getTrack(id);
        checkRights(getRights().canSee(track));
        return getResourceFactory().createTrackResource(track);
    }
}
