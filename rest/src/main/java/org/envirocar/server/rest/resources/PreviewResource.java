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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.util.OSMTileRenderer;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PreviewResource extends AbstractResource {
    private final Track track;

    @Inject
    public PreviewResource(@Assisted Track track) {
        this.track = track;
    }
    @GET
    @Produces(MediaTypes.PNG_IMAGE)
    public Response getMapImage() {
        ByteArrayOutputStream byteArrayOS = null;

        try {
            OSMTileRenderer osm=OSMTileRenderer.create();
            BufferedImage renderedImage = null;
            if(osm.imageExists(track.getIdentifier())){
                renderedImage = osm.loadImage(track.getIdentifier());
            }else{
                Measurements measurements = getDataService().getMeasurements(
                        new MeasurementFilter(track));
                renderedImage = osm.createImage(measurements);
                BufferedImage clipImage=osm.clipImage(renderedImage,measurements, 768, 512,1);
                renderedImage = clipImage;
                osm.saveImage(renderedImage, track.getIdentifier());
            }

            byteArrayOS = new ByteArrayOutputStream();
            ImageIO.write(renderedImage, "png", byteArrayOS);
            byte[] imageData = byteArrayOS.toByteArray();
            return Response.ok(imageData).build();
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }  catch (NullPointerException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    /*@GET
    @Produces(MediaTypes.PNG_IMAGE)
    public Response getMapImage() {
        ByteArrayOutputStream byteArrayOS = null;
        try {
            OSMTileRenderer osm=OSMTileRenderer.create();
            BufferedImage renderedImage = null;
            if(osm.imageExists(track.getIdentifier())){
                renderedImage = osm.loadImage(track.getIdentifier());
            }else{
                Measurements measurements = getDataService().getMeasurements(
                        new MeasurementFilter(track));
                renderedImage = osm.createImage(measurements);
                osm.saveImage(renderedImage, track.getIdentifier());
            }
            byteArrayOS = new ByteArrayOutputStream();
            ImageIO.write(renderedImage, "png", byteArrayOS);
            byte[] imageData = byteArrayOS.toByteArray();
            return Response.ok(imageData).build();
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }  catch (NullPointerException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }*/
}
