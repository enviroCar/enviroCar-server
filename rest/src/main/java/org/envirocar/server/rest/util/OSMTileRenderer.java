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

package org.envirocar.server.rest.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Coordinate;

public class OSMTileRenderer {
	final static String saveFileDir = "/var/data/envirocar/previews";//"/tmp/envirocar/previews";//
	public static final String TIME = "time";
	public static final String MAXSPEED = "maxspeed";
	int numberOfXTiles = 0;
	int numberOfYTiles = 0;
	int baseTileX = 0;
	int baseTileY = 0;

	public BufferedImage getImage(String trackID,Measurements measurements)
			throws IOException {
		BufferedImage image = null;
		if(imageExists(trackID)){
			image = loadImage(trackID);
		}else{
			image = createImage(measurements);
			saveImage(image, trackID);
		}	
		
		return image;
	}
	
	public BufferedImage createImage(Measurements measurements) throws IOException{
		ArrayList<Coordinate> coords = getCoordinates(measurements);
		int zoom = getZoomLevel(coords);
		BufferedImage image = new BufferedImage(256 * (numberOfXTiles + 1),
				256 * (numberOfYTiles + 1), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = appendImage(image, baseTileX + numberOfXTiles, baseTileX,
				baseTileY + numberOfYTiles, baseTileY, zoom);
		drawRoute(g2d, coords, zoom);
		g2d.dispose();
		return image;
	}

	public int getZoomLevel(ArrayList<Coordinate> coords) {
		BoundingBox bbox = findBoundingBoxForGivenLocations(coords);

		int leastZoomLevelX = 1;
		int leastZoomLevelY = 1;
		int finalZoom = 1;

		for (int zoom = 19; zoom >= 1; zoom--) { // considering x
			int xlength = (getTileDetails(bbox.west, bbox.north, zoom)[0] - getTileDetails(
					bbox.east, bbox.north, zoom)[0]);

			if (xlength <= 2) {
				leastZoomLevelX = zoom;
				numberOfXTiles = (xlength);
				break;
			} else {
				leastZoomLevelX = 0; // special case
			}
		}
		for (int zoom = 19; zoom >= 1; zoom--) {// considering y
			int ylength = (getTileDetails(bbox.west, bbox.south, zoom)[1] - getTileDetails(
					bbox.west, bbox.north, zoom)[1]);
			if (ylength <= 1) {
				leastZoomLevelY = zoom;
				numberOfYTiles = (ylength);
				break;
			} else {
				leastZoomLevelY = 0; // special case
			}
		}
		if (leastZoomLevelY > leastZoomLevelX) {
			finalZoom = leastZoomLevelX;
		} else if (leastZoomLevelX > leastZoomLevelY) {
			finalZoom = leastZoomLevelY;
		} else {
			finalZoom = leastZoomLevelX;
		}
		int[] ar = getTileDetails(bbox.east, bbox.north, finalZoom);
		baseTileX = ar[0];
		baseTileY = ar[1];
		return finalZoom;
	}

	public Graphics2D drawRoute(Graphics2D g2d, ArrayList<Coordinate> coords,
			int zoom) {
		g2d.setPaint(new Color(138, 7, 7));
		g2d.setStroke(new BasicStroke(3));
		for (int i = 0; i < coords.size(); i++) {
			if (i <= (coords.size() - 2)) {
				double oldX = coords.get(i).x;
				double oldY = coords.get(i).y;
				double newX = coords.get(i + 1).x;
				double newY = coords.get(i + 1).y;
				g2d.drawLine(getX(oldX, zoom, 256), getY(oldY, zoom, 256),
						getX(newX, zoom, 256), getY(newY, zoom, 256));
				System.out.println();
			}
		}
		return g2d;
	}

	public int getX(final double lon, final int zoom, int pic) {
		double x = getFraction((lon + 180) / 360 * (1 << zoom), baseTileX);
		// System.out.print(getXTile(lon, zoom) + " ++ ");
		return (int) Math.floor((x * pic));
	}

	public int getY(final double lat, final int zoom, int pic) {
		double y = getFraction(
				(1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
						/ Math.cos(Math.toRadians(lat)))
						/ Math.PI)
						/ 2 * (1 << zoom), baseTileY);
		// System.out.print(getYTile(lat, zoom) + " ++ ");
		return (int) Math.floor(y * pic);
	}

	public double getFraction(double num, int baseTileValue) {

		long iPart = (long) num;
		// System.out.print(num + " -- ");
		double fPart = num - baseTileValue; // num-baseTileNumFOr X or Y(36785)
		return fPart;
	}

	public int getInteger(double num) {

		int iPart = (int) num;
		return iPart;
	}

	public int[] getTileDetails(final double lon, final double lat,
			final int zoom) {
		int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		int ytile = (int) Math
				.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
						/ Math.cos(Math.toRadians(lat)))
						/ Math.PI)
						/ 2 * (1 << zoom));
		if (xtile < 0)
			xtile = 0;
		if (xtile >= (1 << zoom))
			xtile = ((1 << zoom) - 1);
		if (ytile < 0)
			ytile = 0;
		if (ytile >= (1 << zoom))
			ytile = ((1 << zoom) - 1);
		// System.out.println("" + zoom + "/" + xtile + "/" + ytile);
		return new int[] { xtile, ytile };
	}

	BoundingBox tile2boundingBox(final int x, final int y, final int zoom) {
		BoundingBox bb = new BoundingBox();
		bb.north = tile2lat(y, zoom);
		bb.south = tile2lat(y + 1, zoom);
		bb.west = tile2lon(x, zoom);
		bb.east = tile2lon(x + 1, zoom);
		return bb;
	}

	double tile2lon(int x, int z) {
		return x / Math.pow(2.0, z) * 360.0 - 180;
	}

	double tile2lat(int y, int z) {
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}

	public BoundingBox findBoundingBoxForGivenLocations(
			ArrayList<Coordinate> coordinates) {
		double west = 0.0;
		double east = 0.0;
		double north = 0.0;
		double south = 0.0;

		for (int lc = 0; lc < coordinates.size(); lc++) {
			Coordinate loc = coordinates.get(lc);
			// System.out.println(loc.y);
			if (lc == 0) {
				north = loc.y;
				south = loc.y;
				west = loc.x;
				east = loc.x;

			} else {
				if (loc.y > north) {
					north = loc.y;
				} else if (loc.y < south) {
					south = loc.y;
				}
				if (loc.x < west) {
					west = loc.x;
				} else if (loc.x > east) {
					east = loc.x;
				}
			}
		}
		/*
		 * double padding = 0.001; north = north + padding; south = south -
		 * padding; west = west - padding; east = east + padding;
		 */
		return new BoundingBox(north, south, west, east);
	}

	public Graphics2D appendImage(BufferedImage newTile, int highestX,
			int lowestX, int highestY, int lowestY, int zoom)
			throws IOException {
		Graphics2D g2d = newTile.createGraphics();
		System.out.println(lowestX + " : " + lowestY + " : " + highestX + " : "
				+ highestY);
		for (int i = lowestX; i <= highestX; i++) {
			for (int j = lowestY; j <= highestY; j++) {
				BufferedImage image = downloadTile(i, j, zoom);
				System.out.println(" i :" + (i - lowestX) * 256 + " j : "
						+ (j - lowestY) * 256);
				g2d.drawImage(image, (i - lowestX) * 256, (j - lowestY) * 256,
						null);
				// g2d.drawImage(image1,256,0,null);
				// temp.add(0,256,image.createGraphics());
				// temp=composite image
			}
		}
		return g2d;
	}

	public BufferedImage downloadTile(int x, int y, int zoom)
			throws IOException {
		String picUri = "http://tile.openstreetmap.org/" + zoom + "/" + x + "/"
				+ y + ".png";
		System.out.println(picUri);
		URL url = new URL(picUri);
		InputStream is = url.openStream();
		BufferedImage image = ImageIO.read(is);
		return image;
	}

	public BufferedImage loadImage(String trackId) throws IOException {
		String filePath = saveFileDir + "/" + trackId + ".png";
		File f = new File(filePath);
		return ImageIO.read(f);
	}
	
	public void saveImage(BufferedImage image, String trackId)
			throws IOException {
		String filePath = saveFileDir + "/" + trackId + ".png";
		File f = new File(filePath);
		if (!f.exists()) {
			ImageIO.write(image, "PNG", f);
		}
	}
	
	public boolean imageExists(String trackId)
			throws IOException {
		String filePath = saveFileDir + "/" + trackId + ".png";
		File f = new File(filePath);
		if (f.exists()) {
			return true;
		}else{
			return false;
		}
	}

	// //////////////////////////////?Retrieval////////////////////////////////

	public ArrayList<Coordinate> getCoordinates(Measurements measurements) {
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		for (Measurement m : measurements) {
			coords.add(m.getGeometry().getCoordinate());
		}
		return coords;
	}
	
	public HashMap<String, String> getDetails(Track track,Measurements measurements)
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
}

class BoundingBox {
	double north;
	double south;
	double east;
	double west;

	public BoundingBox() {

	}

	public BoundingBox(double north, double south, double east, double west) {
		super();
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
	}

}
