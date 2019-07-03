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

import com.vividsolutions.jts.geom.Coordinate;
import org.envirocar.server.rest.schema.GuiceRunner;
import org.envirocar.server.rest.util.OSMTileRenderer.BoundingBox;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(GuiceRunner.class)
public class OSMTileRendererTests {
    private final OSMTileRenderer osmt;

    public OSMTileRendererTests() {
        osmt = new OSMTileRenderer();
    }

    @Test
    public void wmtServiceWorks() throws IOException {
        assertNotNull(osmt.downloadTile(1, 1, 19));
    }

    @Test
    public void saveImageWorks() throws IOException {
        osmt.saveImage(new BufferedImage(768, 512, BufferedImage.TYPE_INT_RGB), "saveImage-test");
    }

    @Test
    public void createImageTestHorizontal() throws IOException {
        BufferedImage bi = createImageTest(populateCoordinates("horizontal"));
        osmt.saveImage(bi, "1235456-test");
        assertEquals("Image height ok", 1024, bi.getHeight());
        assertEquals("Image width ok", 1280, bi.getWidth());
    }

    @Test
    public void createImageTestVertical() throws IOException {
        BufferedImage biv = createImageTest(populateCoordinates("vertical"));
        osmt.saveImage(biv, "1235456-test-vertical");
        assertEquals("Image height ok", 1024, biv.getHeight());
        assertEquals("Image width ok", 1280, biv.getWidth());
    }

    @Test
    public void createImageTestSmall() throws IOException {
        BufferedImage biv = createImageTest(populateCoordinates("regular"));
        osmt.saveImage(biv, "1235456-test-small");
        assertEquals("Image height ok", 1024, biv.getHeight());
        assertEquals("Image width ok", 1280, biv.getWidth());
    }

    @Test
    public void zoomLevelRangeTest() throws IOException {
        List<Coordinate> coords = new ArrayList<>(2);
        coords.add(new Coordinate(89.9, 179.9));
        coords.add(new Coordinate(-89.9, -179.9));
        int level = osmt.getZoomLevel(coords);
        assertEquals("Zoom level exceeds limit", 2, level);
    }

    @Test
    public void zoomLevelTest() throws IOException {
        int level = osmt.getZoomLevel(populateCoordinates("regular"));
        assertEquals(17, level);
    }

    @Test
    public void colorCodeTest() {
        double speed = 53.76; // in kmph
        Color color = osmt.getColorCode(speed);
        assertEquals(new Color(0, 204, 0).getRGB(), color.getRGB());
    }

    @Test
    public void tileDetailTest() {
        double lat = 0.0;
        double lon = 0.0;
        int zoom = 19;
        int[] tileDetails = osmt.getTileDetails(lon, lat, zoom);
        assertEquals(tileDetails[0], 262144);
        assertEquals(tileDetails[1], 262144);
    }

    @Test
    public void validateTile() {
        double lat = 51.93612792994827;
        double lon = 7.651712168008089;
        int zoom = 17;
        int[] tileDetails = osmt.getTileDetails(lon, lat, zoom);
        assertEquals(tileDetails[0], 68321);
        assertEquals(tileDetails[1], 43332);
    }

    @Test
    public void validateBBox() throws IOException {
        BoundingBox bbox = osmt.findBoundingBoxForGivenLocations(populateCoordinates("regular"));
        assertTrue(bbox.east <= 180);
        assertTrue(bbox.west <= 180);
        assertTrue(bbox.north <= 180);
        assertTrue(bbox.south <= 180);
        assertTrue(bbox.east >= 0);
        assertTrue(bbox.west >= 0);
        assertTrue(bbox.north >= 0);
        assertTrue(bbox.south >= 0);

    }

    @Test
    public void bBox() throws IOException {
        BoundingBox bbox = osmt.findBoundingBoxForGivenLocations(populateCoordinates("regular"));
        assertEquals(decimalRound(bbox.east), decimalRound(7.651712168008089), 0.0);
        assertEquals(decimalRound(bbox.west), decimalRound(7.653740337118506), 0.0);
        assertEquals(decimalRound(bbox.north), decimalRound(51.93612792994827), 0.0);
        assertEquals(decimalRound(bbox.south), decimalRound(51.934406915679574), 0.0);

    }

    public double decimalRound(double input) throws IOException {
        DecimalFormat f = new DecimalFormat("##.000000");
        double output = Double.parseDouble(f.format(input));
        return output;
    }

    private ArrayList<Coordinate> populateCoordinates(String test) throws IOException {

        BufferedReader br;
        if (test.equals("horizontal")) {
            br = new BufferedReader(new InputStreamReader(this
                    .getClass().getClassLoader()
                    .getResourceAsStream("testcordinates-horizontal.txt")));
        } else if (test.equals("vertical")) {
            br = new BufferedReader(new InputStreamReader(this
                    .getClass().getClassLoader()
                    .getResourceAsStream("testcoordinates-vertical.txt")));
        } else {
            br = new BufferedReader(new InputStreamReader(this
                    .getClass().getClassLoader()
                    .getResourceAsStream("testcordinates.txt")));
        }
        String strLine;
        ArrayList<Coordinate> coords = new ArrayList<>();
        while ((strLine = br.readLine()) != null) {
            String[] ar = strLine.split(",");
            coords.add(new Coordinate(Double.parseDouble(ar[1]),
                    Double.parseDouble(ar[0])));
        }
        br.close();
        return coords;
    }

    public BufferedImage createImageTest(ArrayList<Coordinate> coords) throws IOException {
        int zoom = osmt.getZoomLevel(coords);
        BufferedImage image = new BufferedImage(256 * (osmt.getNumberOfXTiles() + 1 + 2 * osmt.getImagePadding()),
                256 * (osmt.getNumberOfYTiles() + 1 + 2 * osmt.getImagePadding()),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = osmt.appendImage(image,
                osmt.getBaseTileX() + osmt.getNumberOfXTiles(),
                osmt.getBaseTileX(),
                osmt.getBaseTileY() + osmt.getNumberOfYTiles(),
                osmt.getBaseTileY(), zoom);
        osmt.drawRoute(g2d, coords, null, zoom, osmt.getImagePadding());
        g2d.dispose();
        return image;
    }

}
