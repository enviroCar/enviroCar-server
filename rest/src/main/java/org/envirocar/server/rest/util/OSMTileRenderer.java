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

import javax.imageio.ImageIO;

import com.vividsolutions.jts.geom.Coordinate;

public class OSMTileRenderer {
	final static String saveFileDir = "";

	public BufferedImage getTile(ArrayList<Coordinate> coords)
			throws IOException {
		int zoom = 19;
		Coordinate coor = getLowestLat(coords);
		double lat = coor.y;
		double lon = coor.x;
		zoom= getZoomLevel(coords);
		// format to single tile
		String picUri = "http://tile.openstreetmap.org/"
				+ getTileNumber(lat, lon, zoom) + ".png";
		System.out.println(picUri);
		
		URL url = new URL(picUri);
		InputStream is = url.openStream();
		BufferedImage image = new BufferedImage(512, 256,BufferedImage.TYPE_INT_RGB);//ImageIO.read(is);
		BufferedImage image1 = ImageIO.read(is);
		Graphics2D g2d = image.createGraphics();
		g2d.drawImage(image1,0,0,null);
		g2d.drawImage(image1,256,0,null);
		drawRoute(g2d, coords, zoom); 

		g2d.dispose();
		return image;
	}

	public Coordinate getLowestLat(ArrayList<Coordinate> coords) {
		double tempLat = 360.0;
		Coordinate low = null;
		for (Coordinate coord : coords) {
			if (tempLat > coord.x) {
				tempLat = coord.x;
				low = coord;
				System.out.println(low);
			}
		}
		System.out.println(low);
		return low;
	}

	public int getXTile(double lon, int zoom) {
		int x = getInteger((lon + 180) / 360 * (1 << zoom));
		return x;
	}

	public int getYTile(double lat, int zoom) {
		int y = getInteger((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
				/ Math.cos(Math.toRadians(lat)))
				/ Math.PI)
				/ 2 * (1 << zoom));
		return y;
	}

	public Graphics2D drawRoute(Graphics2D g2d, ArrayList<Coordinate> coords,
			int zoom) {
		g2d.setPaint(new Color(4, 138, 191));
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
		double x = getFraction((lon + 180) / 360 * (1 << zoom),34160); 
		System.out.print(getXTile(lon, zoom) + " ++ ");
		return (int) Math.round((x * pic));
	}

	public int getY(final double lat, final int zoom, int pic) {
		double y = getFraction((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
				/ Math.cos(Math.toRadians(lat)))
				/ Math.PI)
				/ 2 * (1 << zoom),21666);
		 System.out.print(getYTile(lat, zoom) + " ++ ");
		return (int) Math.round(y * pic);
	}

	public int getZoomLevel(ArrayList<Coordinate> coords) {
		BoundingBox bbox = findBoundingBoxForGivenLocations(coords);
		int leastZoomLevelX = 1;
		int leastZoomLevelY = 1;
		int baseTileY;
		int baseTileX;
		int finalZoom = 1;
		for (int zoom = 19; zoom >= 1; zoom--) { // considering x
			if ((getTileDetails(bbox.east, bbox.north, zoom)[1] - getTileDetails(
					bbox.west, bbox.north, zoom)[1]) <= 0) {
				leastZoomLevelX = zoom;
				break;
			} else {
				leastZoomLevelX = 0; // special case
			}
		}
		for (int zoom = 19; zoom >= 1; zoom--) {// considering y
			if ((getTileDetails(bbox.west, bbox.north, zoom)[0] - getTileDetails(
					bbox.west, bbox.south, zoom)[0]) <= 0) {
				leastZoomLevelY = zoom;
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
		return finalZoom;
	}

	public int getLastXTile(final double lastLon, final int zoom, int pic) {
		int x = getX(lastLon, zoom, pic);
		int numAdd = 0;
		if (x > 256) {
			numAdd = x / 256;
		}
		return numAdd;
	}

	public double getFraction(double num,int baseTileValue) {

		long iPart = (long) num;
		// System.out.print(num + " -- ");
		double fPart = num - baseTileValue; // num-baseTileNumFOr X or Y(36785)
		return fPart;
	}

	public int getInteger(double num) {

		int iPart = (int) num;
		return iPart;
	}

	public String getTileNumber(final double lat, final double lon,
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

		return ("" + zoom + "/" + xtile + "/" + ytile);
	}

	public int[] getTileDetails(final double lat, final double lon,
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
		System.out.println("" + zoom + "/" + xtile + "/" + ytile);
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

		return new BoundingBox(north, south, east, west);
	}

	public void saveFile(BufferedImage image, String trackId)
			throws IOException {
		String filePath = saveFileDir + "/" + trackId + ".png";
		File f = new File(filePath);
		if (!f.exists()) {
			ImageIO.write(image, "PNG", new File(filePath));
		}
	}

	public Graphics2D appendImage(BufferedImage newTile,int highestX,int lowestX,int highestY,int lowestY,int zoom)
			throws IOException {
		Graphics2D g2d = newTile.createGraphics();
		for (int i = lowestX; i <= highestX; i++) {
			Graphics2D temp;
			for (int j = lowestY; j <= highestY; j++) {
				BufferedImage image=downloadTile(i, j, zoom);
				//temp.add(0,256,image.createGraphics());
				//temp=composite image
			}
			//g2d.add(256*(i-lowestX),0,temp);
		}
		return g2d;
	}
	
	public BufferedImage downloadTile(int x,int y,int zoom) throws IOException {
		String picUri = "http://tile.openstreetmap.org/"
				 + zoom + "/" + x + "/" + y+ ".png";
		URL url = new URL(picUri);
		InputStream is = url.openStream();
		BufferedImage image = ImageIO.read(is);
		return image;
	}

	public void loadFile(String trackId) throws IOException {
		// if exists load image as a BI else download and create new BI
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

class OSMTile {
	int xValue;
	int yValue;
	BufferedImage image;

}