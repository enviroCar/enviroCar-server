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
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.validation.Schema;

import javax.ws.rs.*;
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
    @Authenticated
    @Schema(request = Schemas.TRACK_MODIFY)
    @Consumes({ MediaTypes.TRACK_MODIFY })
    public Response modify(Track changes) throws TrackNotFoundException,
                                                 IllegalModificationException,
                                                 ValidationException {
        getDataService().modifyTrack(track, changes);
        return Response.ok().build();
    }

    @GET
    @Schema(response = Schemas.TRACK)
    @Produces({ MediaTypes.TRACK,
                MediaTypes.TEXT_CSV,
                MediaTypes.APPLICATION_ZIPPED_SHP})
    public Track get() throws TrackNotFoundException {
        return track;
    }

    @DELETE
    @Authenticated
    public void delete() throws TrackNotFoundException{
        getDataService().deleteTrack(track);
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        return getResourceFactory().createMeasurementsResource(track);
    }

    @Path(SENSOR)
    public SensorResource sensor() throws TrackNotFoundException {
        return getResourceFactory().createSensorResource(track.getSensor());
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        return getResourceFactory().createStatisticsResource(track);
    }
    
    @Path(SHARE)
    public ShareResource share(){
    	return getResourceFactory().createShareResource(track);
    }
    
    @Path(PREVIEW)
    public PreviewResource preview(){
    	return getResourceFactory().createPreviewResource(track);
        
    }
}
