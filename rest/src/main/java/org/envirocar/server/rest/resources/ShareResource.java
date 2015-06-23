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

import com.vividsolutions.jts.geom.Coordinate;

public class ShareResource extends AbstractResource {
	public static final String TRACK = "{track}";
	public static final String TIME = "time";
	public static final String MAXSPEED = "maxspeed";

	@GET
	@Path(TRACK)
	@Produces(MediaTypes.PNG_IMAGE)
	public Response getShareImage(@PathParam("track") String trackId) {
		ByteArrayOutputStream byteArrayOS = null;
		try {
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream("images/gmaps.jpg");
			BufferedImage image = ImageIO.read(in);
			Track track;
			Measurements measurements;
			track = getDataService().getTrack(trackId);
			measurements = getDataService().getMeasurements(
					new MeasurementFilter(track));
			ShareImageRenderUtil imp = new ShareImageRenderUtil();
			HashMap<String, String> hm = getDetails(trackId,track,measurements);
			BufferedImage renderedImage = imp.process(image, hm.get(MAXSPEED),
					hm.get(TIME), "0l/100km");
			/*OSMTileRenderer osm=new OSMTileRenderer();
			BufferedImage renderedImage = osm.getTile(getCoordinates(measurements));*/
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
	
	@GET
	@Path("preview/"+TRACK)
	@Produces(MediaTypes.PNG_IMAGE)
	public Response getMapImage(@PathParam("track") String trackId) {
		ByteArrayOutputStream byteArrayOS = null;
		try {
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream("images/gmaps.jpg");
			BufferedImage image = ImageIO.read(in);
			Track track;
			Measurements measurements;
			track = getDataService().getTrack(trackId);
			measurements = getDataService().getMeasurements(
					new MeasurementFilter(track));
			ShareImageRenderUtil imp = new ShareImageRenderUtil();
			HashMap<String, String> hm = getDetails(trackId,track,measurements);
			/*BufferedImage renderedImage = imp.process(image, hm.get(MAXSPEED),
					hm.get(TIME), "0l/100km");*/
			OSMTileRenderer osm=new OSMTileRenderer();
			BufferedImage renderedImage = osm.getTile(getCoordinates(measurements));
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

	// "5577008eccf24b5d2a54a5e9"
	public HashMap<String, String> getDetails(String trackId,Track track,Measurements measurements)
			throws TrackNotFoundException {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(TIME, calculateTime(track.getBegin(), track.getEnd()));
		hm.put(MAXSPEED, maxSpeed(measurements));
		return hm;
	}

	public String calculateTime(DateTime begin, DateTime end) {
		String time = "";
		long spareTime = 0;
		long diffInSecs = (end.getMillis() - begin.getMillis()) / 1000;
		long diffInDays = diffInSecs / 86400;
		spareTime = diffInSecs % 86400;
		if (diffInDays != 0) {
			time += diffInDays + "d ";
		}
		long diffInHours = spareTime / 3600;
		spareTime = spareTime % 3600;
		if (diffInHours != 0) {
			time += diffInHours + "h ";
		}
		long diffInMins = spareTime / 60;
		spareTime = spareTime % 60;
		if (diffInMins != 0) {
			time += diffInMins + "m ";
		}
		if (spareTime != 0) {
			time += spareTime + "s ";
		}
		return time;
	}

	private double calculateDistance(double lat1, double lon1, double lat2,
			double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public String maxSpeed(Measurements measurements) {
		String maxSpeed = "0";
		for (Measurement m : measurements) {
			Double tempSpeed = 0.0;
			for (MeasurementValue mv : m.getValues()) {
				if (mv.getPhenomenon().getName().equalsIgnoreCase("Speed")) {
					Double instanceSpeed = Double.parseDouble(mv.getValue()
							+ "");
					if (tempSpeed < instanceSpeed) {
						tempSpeed = instanceSpeed;
						maxSpeed = tempSpeed + " "
								+ mv.getPhenomenon().getUnit();
					}
				}
			}

		}
		return maxSpeed;
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
	
	public ArrayList<Coordinate> getCoordinates(Measurements measurements){
		ArrayList<Coordinate> coords=new ArrayList<Coordinate>();
		for (Measurement m : measurements) {
			coords.add(m.getGeometry().getCoordinate()) ;
		}
		return coords;
	}
}
