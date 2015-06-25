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
import javax.ws.rs.core.Response;

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
			Measurements measurements = getDataService().getMeasurements(
					new MeasurementFilter(track));
			OSMTileRenderer osm=new OSMTileRenderer();
			BufferedImage renderedImage = osm.getImage(track.getIdentifier(),measurements);
			byteArrayOS = new ByteArrayOutputStream();
			ImageIO.write(renderedImage, "png", byteArrayOS);

		} catch (IOException e) {
			e.printStackTrace();
		} 
		byte[] imageData = byteArrayOS.toByteArray();
		return Response.ok(imageData).build();
	}
}
