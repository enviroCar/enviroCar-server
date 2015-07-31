package org.envirocar.server.rest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.envirocar.server.rest.schema.GuiceRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vividsolutions.jts.geom.Coordinate;

@RunWith(GuiceRunner.class)
public class OSMTileRendererTests {
	OSMTileRenderer osmt;

	public OSMTileRendererTests() {
		osmt = new OSMTileRenderer();
	}

	@Test
	public void wmtServiceWorks() throws IOException {
		assertNotNull(osmt.downloadTile(1, 1, 19));
	}

	@Test
	public void saveImageWorks() throws IOException {
		osmt.saveImage(new BufferedImage(768, 512, BufferedImage.TYPE_INT_RGB),
				"1235456");
	}

	@Test
	public void zoomLevelRangeTest() throws IOException {
		int level = osmt.getZoomLevel(populateCoordinates());
		assertTrue("Zoom level exceeds limit", 19 >= level);
		assertTrue("Zoom level lower than limit", 0 <= level);
	}

	@Test
	public void zoomLevelTest() throws IOException {
		int level = osmt.getZoomLevel(populateCoordinates());
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
		BoundingBox bbox = osmt
				.findBoundingBoxForGivenLocations(populateCoordinates());
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
		BoundingBox bbox = osmt
				.findBoundingBoxForGivenLocations(populateCoordinates());
		assertTrue(bbox.east == 7.651712168008089);
		assertTrue(bbox.west == 7.653740337118506);
		assertTrue(bbox.north == 51.93612792994827);
		assertTrue(bbox.south == 51.934406915679574);
	}
	
	@Test
	public void tile2bboxTest() throws IOException {
		int xTile=0;
		int yTile=0;
		int zoom = 19;
		BoundingBox bbox=osmt.tile2boundingBox(xTile,yTile,zoom);
		assertTrue(bbox.east == -179.9993133544922);
		assertTrue(bbox.west == -180.0);
		assertTrue(bbox.north == 85.05112877980659);
		assertTrue(bbox.south == 85.05106954478461);
	}
	
	private ArrayList<Coordinate> populateCoordinates() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(this
				.getClass().getClassLoader()
				.getResourceAsStream("testcordinates.txt")));
		String strLine;
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		while ((strLine = br.readLine()) != null) {
			String[] ar = strLine.split(",");
			coords.add(new Coordinate(Double.parseDouble(ar[0]), Double
					.parseDouble(ar[1])));
		}
		br.close();
		return coords;
	}
}
