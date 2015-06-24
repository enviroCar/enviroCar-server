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
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.util.OSMTileRenderer;
import org.envirocar.server.rest.util.ShareImageRenderUtil;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vividsolutions.jts.geom.Coordinate;

public class ShareResource extends AbstractResource {
	public static final String TRACK = "{track}";
	private final Track track;

    @Inject
    public ShareResource(@Assisted Track track) {
        this.track = track;
    }
	
	@GET
	@Produces(MediaTypes.PNG_IMAGE)
	public Response getShareImage() {
		ByteArrayOutputStream byteArrayOS = null;
		try {
			Measurements measurements = getDataService().getMeasurements(
					new MeasurementFilter(track));
			ShareImageRenderUtil imp = new ShareImageRenderUtil();
			OSMTileRenderer osm=new OSMTileRenderer();
			BufferedImage mapImage = osm.getImage(track.getIdentifier(),measurements);
			HashMap<String, String> hm = osm.getDetails(track,measurements);
			BufferedImage renderedImage = imp.process(mapImage, hm.get(osm.MAXSPEED),
					hm.get(osm.TIME), "0l/100km");
			byteArrayOS = new ByteArrayOutputStream();
			ImageIO.write(renderedImage, "png", byteArrayOS);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TrackNotFoundException e) {
			e.printStackTrace();
		}

		byte[] imageData = byteArrayOS.toByteArray();
		return Response.ok(imageData).build();
	}
	
	/*@GET
	@Path(TRACK)
	@Produces("text/html")
	public String testService(@PathParam("track") String trackId)
			throws TrackNotFoundException {
		Track track;
		Measurements measurements;
		Statistics statistics;
		track = getDataService().getTrack(trackId);
		measurements = getDataService().getMeasurements(
				new MeasurementFilter(track));
		String html = "<h2>All stuff</h2><ul>";
		html += "<li>" + "Hi all" + "</li>";
		html += "<li>" + track.getBegin() + "</li>";
		html += "<li>" + track.getEnd() + "</li>";
		html += "<li>" + track.getDescription() + "</li>";
		html += "<li>" + track.getObdDevice() + "</li>";
		html += "<li>" + track.getTouVersion() + "</li>";
		html += "<li>" + track.getIdentifier() + "</li>";
		
		for (Measurement m : measurements) {
			html += "<li>" + m.getGeometry().getCoordinate().x + ":"
					+  m.getGeometry().getCoordinate().y + ":" 
					+ "</li>";
			for (MeasurementValue mv : m.getValues()) {
				html += "<li>" + mv.getPhenomenon().getName() + ":"
						+ mv.getValue() + " " + mv.getPhenomenon().getUnit()
						+ "</li>";
			}
			html += "</br>";
		}
		

		html += "<li>" + track.getSensor() + "</li>";
		html += "</ul>";
		return html;
	}*/
	
	
}
