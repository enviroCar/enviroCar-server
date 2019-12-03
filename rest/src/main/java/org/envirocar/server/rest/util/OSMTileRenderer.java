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
package org.envirocar.server.rest.util;

import com.google.common.annotations.VisibleForTesting;
import com.vividsolutions.jts.geom.Coordinate;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Measurements;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class OSMTileRenderer implements TileRenderer {
    //private static final String TILE_URL_TEMPLATE = "http://tile.openstreetmap.org/%d/%d/%d.png";
    private static final String TILE_URL_TEMPLATE = "http://a.tile.stamen.com/terrain/%d/%d/%d.png";
    private final String saveFileDir;// "/tmp/envirocar/previews";//
    private int numberOfXTiles = 0;
    private int numberOfYTiles = 0;
    private int baseTileX = 0;
    private int baseTileY = 0;
    private int imagePadding = 1;

    public OSMTileRenderer() {
        ResourceBundle config = ResourceBundle.getBundle("OSMConfig");
        this.saveFileDir = config.getString("tile_save_location");
    }

    /**
     * @return the numberOfXTiles
     */
    @VisibleForTesting
    int getNumberOfXTiles() {
        return numberOfXTiles;
    }

    /**
     * @return the numberOfYTiles
     */
    @VisibleForTesting
    int getNumberOfYTiles() {
        return numberOfYTiles;
    }

    /**
     * @return the baseTileX
     */
    @VisibleForTesting
    int getBaseTileX() {
        return baseTileX;
    }

    /**
     * @return the baseTileY
     */
    @VisibleForTesting
    int getBaseTileY() {
        return baseTileY;
    }

    /**
     * @return the imagePadding
     */
    @VisibleForTesting
    int getImagePadding() {
        return imagePadding;
    }

    /**
     * This method outputs a rendered image when the image measurements are given
     *
     * @param measurements
     * @return BufferedImage output
     * @throws IOException
     */
    @Override
    public BufferedImage createImage(Measurements measurements)
            throws IOException {
        List<Coordinate> coords = getCoordinates(measurements);
        Map<Coordinate, Color> colors = getColors(measurements);
        int zoom = getZoomLevel(coords);
        BufferedImage image = new BufferedImage(256 * (getNumberOfXTiles() + 1 + 2 * getImagePadding()),
                                                256 * (getNumberOfYTiles() + 1 + 2 * getImagePadding()), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = appendImage(image, getBaseTileX() + getNumberOfXTiles(), getBaseTileX(), getBaseTileY() +
                                                                                                  getNumberOfYTiles(), getBaseTileY(), zoom);
        drawRoute(g2d, coords, colors, zoom, getImagePadding());
        g2d.dispose();
        return image;
    }

    /**
     * This method outputs the preffered zoom level for given set of coordinates
     *
     * @param coords
     * @return int zoom level
     */
    @VisibleForTesting
    int getZoomLevel(List<Coordinate> coords) {
        BoundingBox bbox = findBoundingBoxForGivenLocations(coords);
        int leastZoomLevelX = 1;
        int leastZoomLevelY = 1;
        int finalZoom;

        for (int zoom = 19; zoom >= 1; zoom--) { // considering x
            int xlength = (getTileDetails(bbox.west, bbox.north, zoom)[0] - getTileDetails(
                    bbox.east, bbox.north, zoom)[0]);

            if (xlength <= 2) {
                leastZoomLevelX = zoom;
                numberOfXTiles = xlength;
                break;
            } else {
                leastZoomLevelX = 0; // special case
            }
        }
        for (int zoom = 19; zoom >= 1; zoom--) {// considering y
            int ylength = (getTileDetails(bbox.west, bbox.south, zoom)[1] -
                           getTileDetails(bbox.west, bbox.north, zoom)[1]);
            if (ylength <= 1) {
                leastZoomLevelY = zoom;
                this.numberOfYTiles = ylength;
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
        this.baseTileX = ar[0];
        this.baseTileY = ar[1];
        if (finalZoom == 1) {
            this.imagePadding = 0;
        }
        return finalZoom;
    }

    /**
     * This method draws the route when the map image and image stats are provided
     *
     * @param g2d
     * @param coords
     * @param colors
     * @param zoom
     * @param padding
     * @return Graphics2D drawn route
     */
    @VisibleForTesting
    Graphics2D drawRoute(Graphics2D g2d, List<Coordinate> coords,
                         Map<Coordinate, Color> colors, int zoom, int padding) {
        g2d.setStroke(new BasicStroke(7));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        for (int i = 0; i <= coords.size() - 2; i++) {
            if (colors == null || colors.isEmpty()) {
                g2d.setPaint(Color.BLUE);
            } else {
                g2d.setPaint(colors.get(coords.get(i + 1)));
            }
            double oldX = coords.get(i).x;
            double oldY = coords.get(i).y;
            double newX = coords.get(i + 1).x;
            double newY = coords.get(i + 1).y;
            g2d.drawLine(getX(oldX, zoom, 256) + 256 * padding, getY(oldY, zoom, 256) + 256 * padding,
                         getX(newX, zoom, 256) + 256 * padding, getY(newY, zoom, 256) + 256 * padding);
        }
        return g2d;
    }

    /**
     * This method converts the longitude of a geo location in to the X coordinate of the image
     *
     * @param lon
     * @param zoom
     * @param pic
     * @return int X coordinate in the image
     */
    private int getX(final double lon, final int zoom, int pic) {
        double x = getFraction((lon + 180) / 360 * (1 << zoom), getBaseTileX());
        return (int) Math.floor((x * pic));
    }

    /**
     * This method converts the latitude of a geo location in to the Y coordinate of the image
     *
     * @param lat
     * @param zoom
     * @param pic
     * @return int Y coordinate in the image
     */
    private int getY(final double lat, final int zoom, int pic) {
        double y = getFraction((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 /
                                                                             Math.cos(Math.toRadians(lat))) /
                                    Math.PI) /
                               2 * (1 << zoom), getBaseTileY());
        return (int) Math.floor(y * pic);
    }

    /**
     * This method returns the fraction part of a given double number
     *
     * @param num
     * @param baseTileValue
     * @return double fraction of a given number
     */
    private double getFraction(double num, int baseTileValue) {
        return num - baseTileValue; // num-baseTileNumFOr X or Y(36785)
    }

    /**
     * This method returns the integer part of a given double number
     *
     * @param num
     * @return int Integer part of a number
     */
    private int getInteger(double num) {
        return (int) num;
    }

    /**
     * This method returns the OSM tile number which contains a given location (in the format { xtile number, ytile
     * number})
     *
     * @param lon
     * @param lat
     * @param zoom
     * @return int[] containing { xtile, ytile } respectively
     */
    @VisibleForTesting
    int[] getTileDetails(final double lon, final double lat,
                         final int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) /
                                          Math.PI) / 2 * (1 << zoom));
        if (xtile < 0) {
            xtile = 0;
        }
        if (xtile >= (1 << zoom)) {
            xtile = ((1 << zoom) - 1);
        }
        if (ytile < 0) {
            ytile = 0;
        }
        if (ytile >= (1 << zoom)) {
            ytile = ((1 << zoom) - 1);
        }
        return new int[]{xtile, ytile};
    }

    private double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    private double tile2lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    /**
     * This method returns a bounding box object which contains a given set of coordinates
     *
     * @param coordinates
     * @return BoundingBox Bounding box which contains a given set of coordinates
     */
    @VisibleForTesting
    BoundingBox findBoundingBoxForGivenLocations(List<Coordinate> coordinates) {
        double minx = 0.0;
        double maxx = 0.0;
        double maxy = 0.0;
        double miny = 0.0;
        Iterator<Coordinate> it = coordinates.iterator();
        if (it.hasNext()) {
            Coordinate coordinate = it.next();
            maxy = coordinate.y;
            miny = coordinate.y;
            minx = coordinate.x;
            maxx = coordinate.x;
            while (it.hasNext()) {
                coordinate = it.next();
                if (coordinate.y > maxy) {
                    maxy = coordinate.y;
                } else if (coordinate.y < miny) {
                    miny = coordinate.y;
                }
                if (coordinate.x < minx) {
                    minx = coordinate.x;
                } else if (coordinate.x > maxx) {
                    maxx = coordinate.x;
                }
            }
        }

        /*
         * double padding = 0.001; north = north + padding; south = south -
         * padding; west = west - padding; east = east + padding;
         */
        return new BoundingBox(maxy, miny, minx, maxx);
    }

    /**
     * This method returns the base map image which is used then for route drawing and statistics integration.
     *
     * @param newTile
     * @param highestX
     * @param lowestX
     * @param highestY
     * @param lowestY
     * @param zoom
     * @return Graphics2D Generates the base map image
     * @throws IOException
     */
    @VisibleForTesting
    Graphics2D appendImage(BufferedImage newTile, int highestX,
                           int lowestX, int highestY, int lowestY, int zoom)
            throws IOException {
        if (zoom != 1) {
            highestX = lowestX + 2;
            highestY = lowestY + 1;
            lowestX--;
            highestX++;
            lowestY--;
            highestY++;
        }
        Graphics2D g2d = newTile.createGraphics();
        for (int i = lowestX; i <= highestX; i++) {
            for (int j = lowestY; j <= highestY; j++) {
                BufferedImage image = downloadTile(i, j, zoom);
                g2d.drawImage(image, (i - lowestX) * 256, (j - lowestY) * 256, null);
            }
        }
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(newTile, newTile);
        return g2d;
    }

    /**
     * This method downloads a single tile from OSM tile service when xtile, ytile and zoom level is given.
     *
     * @param x
     * @param y
     * @param zoom
     * @return BufferedImage BufferedImage for a single tile which is used to create base map
     * @throws IOException
     */
    @VisibleForTesting
    BufferedImage downloadTile(int x, int y, int zoom) throws IOException {

        URL url = new URL(String.format(TILE_URL_TEMPLATE, zoom, x, y));
        try (InputStream is = url.openStream()) {
            return ImageIO.read(is);
        }

    }

    /**
     * This method reads an image from disk and return it as a BufferedImage
     *
     * @param trackId
     * @return BufferedImage loadedImage
     * @throws IOException
     */
    @Override
    public synchronized BufferedImage loadImage(String trackId) throws IOException {
        return ImageIO.read(getImageFile(trackId).toFile());
    }

    @Override
    public synchronized void saveImage(BufferedImage image, String trackId) throws IOException {
        Path f = getImageFile(trackId);
        if (!Files.exists(f)) {
            ImageIO.write(image, "PNG", f.toFile());
        }
    }

    @Override
    public synchronized boolean imageExists(String trackId) throws IOException {
        return Files.exists(getImageFile(trackId));
    }

    // //////////////////////////////?Retrieval////////////////////////////////

    /**
     * Returns a set of coordinates when the measurements of a track is given
     *
     * @param measurements
     * @return ArrayList of coordinates
     */
    private List<Coordinate> getCoordinates(Measurements measurements) {
        List<Coordinate> coords = new ArrayList<>();
        for (Measurement m : measurements) {
            coords.add(m.getGeometry().getCoordinate());
        }
        return coords;
    }

    /**
     * This method returns a hashmap which contains colorcode(color of the route based on speed) for each coordinate
     *
     * @param measurements
     * @return HashMap which has coordinate as the key and color as the value
     */
    private Map<Coordinate, Color> getColors(Measurements measurements) {
        Map<Coordinate, Color> coords = new HashMap<>();
        for (Measurement m : measurements) {
            for (MeasurementValue mv : m.getValues()) {
                if (mv.getPhenomenon().getName().equals("Speed")) {
                    coords.put(m.getGeometry().getCoordinate(),
                               getColorCode(Double.parseDouble(mv.getValue().toString())));
                }
            }
        }
        return coords;
    }

    /**
     * This method returns the relevant color object for the given speed
     *
     * @param speed
     * @return Color color of the speed
     */
    @VisibleForTesting
    Color getColorCode(double speed) {

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

    /**
     * This method calculates the distance between two given coordinates
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param unit
     * @return double Distance between two coordinates
     */
    private double calculateDistance(double lat1, double lon1, double lat2,
                                     double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                      Math.cos(deg2rad(theta));
        dist = rad2deg(Math.acos(dist));
        dist *= 60 * 1.1515;
        dist *= 1.609344;
        return (dist);
    }

    /**
     * Convert degrees value to a radian value
     *
     * @param deg
     * @return double radian value
     */
    private double deg2rad(double deg) {
        return deg * Math.PI / 180.0;
    }

    /**
     * Converts radian value to a degree value
     *
     * @param rad
     * @return double degree value
     */
    private double rad2deg(double rad) {
        return rad * 180 / Math.PI;
    }

    @Override
    public BufferedImage clipImage(BufferedImage image,
                                   Measurements measurements, int requiredwidth, int requiredHeight, int padding) {
        List<Coordinate> coords = getCoordinates(measurements);
        int zoom = getZoomLevel(coords);
        BoundingBox bbox = findBoundingBoxForGivenLocations(coords);
        int clipWidth = getX(bbox.west, zoom, 256) + 256 * padding - (getX(bbox.east, zoom, 256) + 256 * padding);
        int clipHeight = getY(bbox.south, zoom, 256) + 256 * padding -
                         (getY(bbox.north, zoom, 256) + 256 * padding);
        int x = getX(bbox.east, zoom, 256) + 256 * padding;
        int y = getY(bbox.north, zoom, 256) + 256 * padding;
        int newx = (x + clipWidth / 2) - requiredwidth / 2;
        int newy = (y + clipHeight / 2) - requiredHeight / 2;
        // bound adjustments
        if (newx <= 0) {
            newx = 0;
        }
        if (newy <= 0) {
            newy = 0;
        }
        if (newx + requiredwidth >= image.getWidth()) {
            newx = image.getWidth() - requiredwidth;
        }
        if (newy + requiredHeight >= image.getHeight()) {
            newy = image.getHeight() - requiredHeight;
        }
        return image.getSubimage(newx, newy, requiredwidth, requiredHeight);
    }

    /**
     * Finds the center of a bounding box when bounding box is given
     *
     * @param bbox
     * @return coordinate which represents the center of the bounding box
     */
    private Coordinate findBoundingBoxCenter(BoundingBox bbox) {
        double x = (bbox.west + bbox.east) / 2;
        double y = (bbox.north + bbox.south) / 2;
        return new Coordinate(x, y);
    }

    private BufferedImage addPaddingTiles(BufferedImage image,
                                          Measurements measurements, int requiredwidth, int requiredHeight) {
        List<Coordinate> coords = getCoordinates(measurements);
        int zoom = getZoomLevel(coords);
        BoundingBox bbox = findBoundingBoxForGivenLocations(coords);
        Coordinate center = findBoundingBoxCenter(bbox);
        int clipWidth = getX(bbox.west, zoom, 256) - getX(bbox.east, zoom, 256);
        int clipHeight = getY(bbox.south, zoom, 256) -
                         getY(bbox.north, zoom, 256);
        int x = getX(bbox.east, zoom, 256);
        int y = getY(bbox.north, zoom, 256);
        BufferedImage dbi = image.getSubimage(x, y, clipWidth, clipHeight);
        return dbi;
    }

    private Path getImageFile(String trackId) throws IOException {
        Path dir = Paths.get(saveFileDir);
        Files.createDirectories(dir);
        return dir.resolve(String.format("%s.png", trackId));
    }

    @VisibleForTesting
    static class BoundingBox {
        double north;
        double south;
        double east;
        double west;

        /**
         * Creates a new bounding box object when north east west south coordinates are given.
         *
         * @param north
         * @param south
         * @param east
         * @param west
         */
        BoundingBox(double north, double south, double east, double west) {
            super();
            this.north = north;
            this.south = south;
            this.east = east;
            this.west = west;
        }

    }

}
