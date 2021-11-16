/*
 * Copyright (C) 2013-2021 The enviroCar project
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
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class TrackResource extends AbstractResource {
    public static final String MEASUREMENTS = "measurements";
    public static final String SENSOR = "sensor";
    public static final String STATISTICS = "statistics";
    public static final String SHARE = "share";
    public static final String PREVIEW = "preview";
    private final Track track;

    @Inject
    public TrackResource(@Assisted Track track) {
        this.track = track;
    }

    @PUT
    @Schema(request = Schemas.TRACK_MODIFY)
    @Consumes({MediaTypes.JSON})
    @Authenticated
    public Response modify(Track changes) throws IllegalModificationException, ValidationException {
        checkRights(getRights().canModify(track));
        getDataService().modifyTrack(track, changes);
        return Response.ok().build();
    }

    @GET
    @Schema(response = Schemas.TRACK)
    @Produces({MediaTypes.JSON,
               MediaTypes.XML_RDF,
               MediaTypes.TURTLE,
               MediaTypes.TURTLE_ALT,
               MediaTypes.CSV,
               MediaTypes.APPLICATION_ZIPPED_SHP})
    public Track get() {
        return track;
    }

    @DELETE
    @Authenticated
    public void delete() {
        checkRights(getRights().canDelete(track));
        getDataService().deleteTrack(track);
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        checkRights(getRights().canSeeMeasurementsOf(track));
        return getResourceFactory().createMeasurementsResource(null, track);
    }

    @Path(SENSOR)
    public SensorResource sensor() {
        checkRights(getRights().canSeeSensorOf(track));
        return getResourceFactory().createSensorResource(track.getSensor());
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        checkRights(getRights().canSeeStatisticsOf(track));
        return getResourceFactory().createStatisticsResource(track);
    }

    @Path(SHARE)
    public ShareResource share() {
        return getResourceFactory().createShareResource(track);
    }

    @Path(PREVIEW)
    public PreviewResource preview() {
        return getResourceFactory().createPreviewResource(track);

    }
}
