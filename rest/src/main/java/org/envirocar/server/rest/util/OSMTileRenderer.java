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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.exception.TrackNotFoundException;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Coordinate;

public class OSMTileRenderer {

	private String saveFileDir;// "/tmp/envirocar/previews";//
	public static final String TIME = "time";
	private ResourceBundle config;
	public static final String MAXSPEED = "maxspeed";
	public static final String AVGSPEED = "avgspeed";
	public static final String CMAF = "Calculated MAF";
	public static final String AVGRPM = "RPM";
	public static final String IPRESSURE = "IntakePressure";
	public static final String ITEMP = "IntakeTemperature";
	public static final String CONSUMPTION = "Consumption";
	public static final String CO2 = "CO2";
	int numberOfXTiles = 0;
	int numberOfYTiles = 0;
	int baseTileX = 0;
	int baseTileY = 0;
	int imagePadding = 1;

	OSMTileRenderer() {
		super();
		config = ResourceBundle.getBundle("OSMConfig");
		saveFileDir = config.getString("tile_save_location");
	}

	public static OSMTileRenderer create() {
		return new OSMTileRenderer();
	}

	public BufferedImage createImage(Measurements measurements)
			throws IOException {
		ArrayList<Coordinate> coords = getCoordinates(measurements);
		HashMap<Coordinate, Color> colors = getColors(measurements);
		int zoom = getZoomLevel(coords);
		BufferedImage image = new BufferedImage(256 * (numberOfXTiles + 1 + 2*imagePadding),
				256 * (numberOfYTiles + 1 + 2*imagePadding), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = appendImage(image, baseTileX + numberOfXTiles,
				baseTileX, baseTileY + numberOfYTiles, baseTileY, zoom);
		drawRoute(g2d, coords, colors, zoom,imagePadding);
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
		if(finalZoom==1){
			imagePadding=0;
		}
		return finalZoom;
	}

	public Graphics2D drawRoute(Graphics2D g2d, ArrayList<Coordinate> coords,
			HashMap<Coordinate, Color> colors, int zoom,int padding) {
		g2d.setStroke(new BasicStroke(7));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		for (int i = 0; i < coords.size(); i++) {
			if (i <= (coords.size() - 2)) {
				if( colors==null || colors.isEmpty())g2d.setPaint(Color.BLACK);
				else g2d.setPaint(colors.get(coords.get(i + 1)));
				double oldX = coords.get(i).x;
				double oldY = coords.get(i).y;
				double newX = coords.get(i + 1).x;
				double newY = coords.get(i + 1).y;
				g2d.drawLine(getX(oldX, zoom, 256)+256*padding, getY(oldY, zoom, 256)+256*padding,
						getX(newX, zoom, 256)+256*padding, getY(newY, zoom, 256)+256*padding);
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
		//generalize
		
		if(zoom!=1){
		highestX = lowestX+2;
		highestY = lowestY+1;
		lowestX--;highestX++;lowestY--;highestY++;
		}
		//
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
		ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(newTile, newTile);
		return g2d;
	}

	public BufferedImage downloadTile(int x, int y, int zoom)
			throws IOException {
		/*
		 * String picUri = "http://tile.openstreetmap.org/" + zoom + "/" + x +
		 * "/" + y + ".png";
		 */
		String picUri = "http://otile1.mqcdn.com/tiles/1.0.0/map/" + zoom + "/"
				+ x + "/" + y + ".png";
		System.out.println(picUri);
		URL url = new URL(picUri);
		InputStream is = url.openStream();
		BufferedImage image = ImageIO.read(is);
		return image;
	}

	public synchronized BufferedImage loadImage(String trackId)
			throws IOException {
		String filePath = new StringBuilder(saveFileDir).append(File.separator)
				.append(trackId).append(".png").toString();
		File f = new File(filePath);
		return ImageIO.read(f);
	}

	public synchronized void saveImage(BufferedImage image, String trackId)
			throws IOException {
		String filePath = new StringBuilder(saveFileDir).append(File.separator)
				.append(trackId).append(".png").toString();
		File f = new File(filePath);
		if (!f.exists()) {
			ImageIO.write(image, "PNG", f);
		}
	}

	public synchronized boolean imageExists(String trackId) throws IOException {
		String filePath = new StringBuilder(saveFileDir).append(File.separator)
				.append(trackId).append(".png").toString();
		System.out.println(filePath);
		File f = new File(filePath);
		if (f.exists()) {
			return true;
		} else {
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

	public HashMap<Coordinate, Color> getColors(Measurements measurements) {
		HashMap<Coordinate, Color> coords = new HashMap<Coordinate, Color>();
		for (Measurement m : measurements) {
			for (MeasurementValue mv : m.getValues()) {
				if (mv.getPhenomenon().getName().equals("Speed")) {
					coords.put(m.getGeometry().getCoordinate(),
							getColorCode(Double.parseDouble(mv.getValue()
									.toString())));
				}
			}
		}
		return coords;
	}

	public Color getColorCode(double speed) {

		if (speed <= 30) {
			return new Color(0, 102, 0);
		} else if (speed > 30 && speed <= 50) {
			return new Color(0, 153, 0);
		} else if (speed > 50 && speed <= 70) {
			return new Color(0, 204, 0);
		} else if (speed > 70 && speed <= 90) {
			return new Color(204, 204, 0);
		} else if (speed > 90 && speed <= 110) {
			return new Color(255, 255, 0);
		} else if (speed > 110 && speed <= 130) {
			return new Color(255, 128, 0);
		} else if (speed > 130 && speed <= 160) {
			return new Color(204, 0, 0);
		} else if (speed > 160) {
			return new Color(255, 0, 0);
		} else {
			return new Color(255, 7, 7);
		}

	}

	public HashMap<String, String> getDetails(Track track, Statistics statistics)
			throws TrackNotFoundException {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(TIME, calculateTime(track.getBegin(), track.getEnd()));
		hm.put(MAXSPEED, new StringBuilder("N/A").toString());
		hm.put(AVGSPEED, new StringBuilder("N/A").toString());
		hm.put(CMAF, new StringBuilder("N/A").toString());
		hm.put(AVGRPM, new StringBuilder("N/A").toString());
		hm.put(IPRESSURE, new StringBuilder("N/A").toString());
		hm.put(ITEMP, new StringBuilder("N/A").toString());
		hm.put(CONSUMPTION, new StringBuilder("N/A").toString());
		hm.put(CO2, new StringBuilder("N/A").toString());

		for (Statistic statistic : statistics) {
			if (statistic.getPhenomenon().getName().equalsIgnoreCase("Speed")) {
				hm.put(MAXSPEED,
						new StringBuilder(
								Math.round(statistic.getMax() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
				hm.put(AVGSPEED,
						new StringBuilder(
								Math.round(statistic.getMean() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
			}
			if (statistic.getPhenomenon().getName()
					.equalsIgnoreCase("Calculated MAF")) {
				hm.put(CMAF,
						new StringBuilder(
								Math.round(statistic.getMean() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
			}
			if (statistic.getPhenomenon().getName().equalsIgnoreCase("Rpm")) {
				hm.put(AVGRPM,
						new StringBuilder(
								Math.round(statistic.getMean() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
			}
			if (statistic.getPhenomenon().getName()
					.equalsIgnoreCase("Intake Temperature")) {
				hm.put(IPRESSURE,
						new StringBuilder(
								Math.round(statistic.getMean() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
			}
			if (statistic.getPhenomenon().getName()
					.equalsIgnoreCase("Intake Pressure")) {
				hm.put(ITEMP,
						new StringBuilder(
								Math.round(statistic.getMean() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
			}
			if (statistic.getPhenomenon().getName()
					.equalsIgnoreCase(CONSUMPTION)) {
				hm.put(CONSUMPTION,
						new StringBuilder(
								Math.round(statistic.getMean() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
			}
			if (statistic.getPhenomenon().getName().equalsIgnoreCase(CO2)) {
				hm.put(CO2,
						new StringBuilder(
								Math.round(statistic.getMean() * 100.0) / 100.0
										+ "").append(" ")
								.append(statistic.getPhenomenon().getUnit())
								.toString());
			}

		}

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

	public BufferedImage clipImage(BufferedImage image,
			Measurements measurements, int requiredwidth, int requiredHeight,int padding) {
		ArrayList<Coordinate> coords = getCoordinates(measurements);
		int zoom = getZoomLevel(coords);
		BoundingBox bbox = findBoundingBoxForGivenLocations(coords);
		Coordinate center = findBoundingBoxCenter(bbox);
		int clipWidth = getX(bbox.west, zoom, 256)+256*padding - (getX(bbox.east, zoom, 256)+256*padding);
		int clipHeight = getY(bbox.south, zoom, 256)+256*padding
				- (getY(bbox.north, zoom, 256)+256*padding);
		int x = getX(bbox.east, zoom, 256)+256*padding;
		int y = getY(bbox.north, zoom, 256)+256*padding;
		int newx = (x+clipWidth/2)-requiredwidth/2;
		int newy = (y+clipHeight/2)-requiredHeight/2;
		// bound adjustments
		if(newx<=0){newx = 0;}
		if(newy<=0){newy = 0;}
		if(newx+requiredwidth>=image.getWidth()){newx = image.getWidth()-requiredwidth;}
		if(newy+requiredHeight>=image.getHeight()){newy = image.getHeight()-requiredHeight;}
		//
		System.out.println("x : "+x);
		System.out.println("y : "+y);
		System.out.println("clipWidth : "+clipWidth);
		System.out.println("clipHeight : "+clipHeight);
		System.out.println("newx : "+newx);
		System.out.println("newy : "+newy);
		BufferedImage dbi = image.getSubimage(newx, newy, requiredwidth, requiredHeight);
		/*
		 * System.out.println(x+" : "+clipWidth);
		 * System.out.println(y+" : "+clipHeight); Graphics2D
		 * g2d=dbi.createGraphics(); g2d.setClip(new Rectangle(x, y, clipWidth,
		 * clipHeight)); g2d.clipRect(x, y, clipWidth, clipHeight);
		 * AffineTransform at =
		 * AffineTransform.getScaleInstance(clipWidth,clipHeight);
		 * g2d.drawImage(image,0, 0, clipWidth, clipHeight,null);
		 */
		return dbi;
	}
	
	public Coordinate findBoundingBoxCenter(BoundingBox bbox) {
		double x = (bbox.west+bbox.east)/2;	
		double y = (bbox.north+bbox.south)/2;	
		return new Coordinate(x,y);
	}
	
	public Coordinate getSubImage(int centerX,int centerY,int x,int y,int subx,int suby) {
		if((centerX-(subx/2))>=x/2 && (centerY-(suby/2))>=y/2){
			return new Coordinate(0,0);
		}else if((centerX-(subx/2))>=x/2 && (centerY-(suby/2))<y/2){
			return new Coordinate(0,0);
		}else if((centerX-(subx/2))>=x/2 && (centerY-(suby/2))>=y/2){
			return new Coordinate(0,0);
		}else if((centerX-(subx/2))>=x/2 && (centerY-(suby/2))>=y/2){
			return new Coordinate(0,0);
		}else{
			return new Coordinate(0,0);
		}
			
		
	}
	
	public BufferedImage addPaddingTiles(BufferedImage image,
			Measurements measurements, int requiredwidth, int requiredHeight) {
		ArrayList<Coordinate> coords = getCoordinates(measurements);
		int zoom = getZoomLevel(coords);
		BoundingBox bbox = findBoundingBoxForGivenLocations(coords);
		Coordinate center = findBoundingBoxCenter(bbox);
		int clipWidth = getX(bbox.west, zoom, 256) - getX(bbox.east, zoom, 256);
		int clipHeight = getY(bbox.south, zoom, 256)
				- getY(bbox.north, zoom, 256);
		int x = getX(bbox.east, zoom, 256);
		int y = getY(bbox.north, zoom, 256);
		BufferedImage dbi = image.getSubimage(x, y, clipWidth, clipHeight);
		return dbi;
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
