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

import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.rest.BoundingBox;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.TrackWithMeasurments;
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
    @Produces({ MediaTypes.TRACKS,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Tracks get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page,
            @QueryParam(RESTConstants.BBOX) BoundingBox bbox)
            throws UserNotFoundException {
        Polygon poly = null;
        if (bbox != null) {
            poly = bbox.asPolygon(factory);
        }
        return getDataService()
                .getTracks(new TrackFilter(user, poly,
                                           parseTemporalFilterForInterval(),
                                           new Pagination(limit, page)));
    }

    @POST
    @Schema(request = Schemas.TRACK_CREATE)
    @Consumes({ MediaTypes.TRACK_CREATE })
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
            track = getDataService().createTrack(twm.getTrack(),
                                                 twm.getMeasurements());
        } else {
            track = getDataService().createTrack(track);
        }
        return Response.created(getUriInfo().getAbsolutePathBuilder()
                .path(track.getIdentifier()).build()).build();
    }

    @Path(TRACK)
    public TrackResource track(@PathParam("track") String id)
            throws TrackNotFoundException {
        Track track = getDataService().getTrack(id);
        checkRights(getRights().canSee(track));
        return getResourceFactory().createTrackResource(track);
    }
}
