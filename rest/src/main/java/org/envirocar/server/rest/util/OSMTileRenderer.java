package org.envirocar.server.rest.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.vividsolutions.jts.geom.Coordinate;

public class OSMTileRenderer {
	 int xTile = 0, yTile = 0;

	public  BufferedImage getTile(ArrayList<Coordinate> coords)
			throws IOException {
		int zoom = 17;
		Coordinate coor = getLowestLat(coords);
		double lat = coor.y;
		double lon = coor.x;
		// format to single tile
		String picUri = "http://tile.openstreetmap.org/"
				+ getTileNumber(lat, lon, zoom) + ".png";
		System.out.println(picUri);
		URL url;

		url = new URL(picUri);
		InputStream is = url.openStream();
		BufferedImage image = ImageIO.read(is);
		Graphics2D g2d = image.createGraphics();
		drawRoute(g2d, coords, zoom);

		g2d.dispose();
		return image;
	}

	public  Coordinate getLowestLat(ArrayList<Coordinate> coords) {
		double tempLat = 360.0;
		Coordinate low = null;
		for (Coordinate coord : coords) {
			if (tempLat > coord.y) {
				tempLat = coord.y;
				low = coord;
			}
		}
		return low;
	}

	public  int getXTile(double lon, int zoom) {
		int x = getInteger((lon + 180) / 360 * (1 << zoom));
		return x;
	}

	public  int getYTile(double lat, int zoom) {
		int y = getInteger((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
				/ Math.cos(Math.toRadians(lat)))
				/ Math.PI)
				/ 2 * (1 << zoom));
		return y;
	}

	public  Graphics2D drawRoute(Graphics2D g2d,
			ArrayList<Coordinate> coords, int zoom) {
		g2d.setPaint(new Color(4, 138, 191));
		g2d.setStroke(new BasicStroke(3));
		for (int i = 0; i < coords.size(); i++) {
			if (i <= (coords.size() - 2)) {
				double oldX = coords.get(i).x;
				double oldY = coords.get(i).y;
				double newX = coords.get(i + 1).x;
				double newY = coords.get(i + 1).y;
				if (xTile == getXTile(oldX, zoom) // check whether point is out of the bbox
						&& xTile == getXTile(newX, zoom)) { //check for yTiles too, route last point and new point
					/*System.out.println(i + "==> " + oldX + ":" + oldY + ":"
							+ newX + ":" + newY + "===>"
							+ getX(oldX, zoom, 256) + ":"
							+ getY(oldY, zoom, 256) + ":"
							+ getX(newX, zoom, 256) + ":"
							+ getY(newY, zoom, 256));*/
					g2d.drawLine(getX(oldX, zoom, 256), getY(oldY, zoom, 256),
							getX(newX, zoom, 256), getY(newY, zoom, 256));
				}
			}
		}
		return g2d;
	}

	public  int getX(final double lon, final int zoom, int pic) {
		double x = getFraction((lon + 180) / 360 * (1 << zoom));
		System.out.print(x + " ++ ");
		return (int) Math.round((x * pic));
	}

	public  int getY(final double lat, final int zoom, int pic) {
		double y = getFraction((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
				/ Math.cos(Math.toRadians(lat)))
				/ Math.PI)
				/ 2 * (1 << zoom));
		//System.out.print(y + " ++ ");
		return (int) Math.round(y * pic);
	}

	public int getLastXTile(final double lastLon, final int zoom, int pic) {
		int x = getX(lastLon, zoom, pic);
		int numAdd = 0;
		if (x > 256) {
			numAdd = x / 256;
		}
		return numAdd;
	}

	public double getFraction(double num) {

		long iPart = (long) num;
		//System.out.print(num + " -- ");
		double fPart = num - iPart;
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
		xTile = xtile;
		yTile = ytile;
		return ("" + zoom + "/" + xtile + "/" + ytile);
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
}

class BoundingBox {
	double north;
	double south;
	double east;
	double west;
}