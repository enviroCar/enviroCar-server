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

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.TrackNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.util.Pagination;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.RESTConstants;
import io.car.server.rest.Schemas;
import io.car.server.rest.TrackWithMeasurments;
import io.car.server.rest.auth.Authenticated;
import io.car.server.rest.validation.Schema;

import java.util.ArrayList;

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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class TracksResource extends AbstractResource {
    public static final String TRACK = "{track}";
    private User user;
    
    @Inject
    private GeometryFactory factory;

    @Inject
    public TracksResource(@Assisted @Nullable User user) {
        this.user = user;
    }

    @GET
    @Schema(response = Schemas.TRACKS)
    @Produces(MediaTypes.TRACKS)
    public Tracks get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.BBOX) @DefaultValue("") String bbox)
            throws UserNotFoundException {
        Pagination p = new Pagination(limit, page);
        if (!bbox.equals("")) {
            final String[] coords = bbox.split(",");
            Double[] d = new ArrayList<Double>() {
                {
                    for (String coord : coords)
                        add(Double.parseDouble(coord));
                }
            }.toArray(new Double[coords.length]);
            Coordinate[] coordinates = new Coordinate[] { 
                    new Coordinate(d[0], d[1]),
                    new Coordinate(d[2], d[1]), 
                    new Coordinate(d[2], d[3]), 
                    new Coordinate(d[0], d[3]), 
                    new Coordinate(d[0], d[1]) };
            return getService().getTracksByBbox(factory.createPolygon(coordinates), p);
//            return getService().getTracksByBbox( //
//                    Double.parseDouble(coords[0]),
//                    Double.parseDouble(coords[1]),
//                    Double.parseDouble(coords[2]),
//                    Double.parseDouble(coords[3]), p);
        }
        return user != null
               ? getService().getTracks(user, p)
               : getService().getTracks(p);
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
