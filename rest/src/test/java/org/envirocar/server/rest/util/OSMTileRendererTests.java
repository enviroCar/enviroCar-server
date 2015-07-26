package org.envirocar.server.rest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.envirocar.server.rest.schema.GuiceRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vividsolutions.jts.geom.Coordinate;

@RunWith(GuiceRunner.class)
public class OSMTileRendererTests  {
	OSMTileRenderer osmt;
	
	public OSMTileRendererTests() {
		osmt=new OSMTileRenderer();
	}
	
	@Test(expected = IOException.class)
	public void WMTServiceWorks() throws IOException {
			assertNotNull(osmt.downloadTile(1,1, 19));
	}
	
	@Test(expected = IOException.class)
	public void SaveImageWorks() throws IOException {
			osmt.saveImage(new BufferedImage(768, 512, BufferedImage.TYPE_INT_RGB), "1235456");
	}
	
	@Test
	public void zoomLevelTest(){
		int level=osmt.getZoomLevel(populateCoordinates());
		assertTrue("Zoom level exceeds limit", 19 >= level);
		assertTrue("Zoom level lower than limit",  0  <= level);
	}
	
	@Test
	public void colorCodeTest(){
		Random r = new Random(); 
		Color color=osmt.getColorCode(r.nextDouble() * 90.0);
		assertTrue(color.getRGB() != 0);
	}
	
	@Test
	public void tileDetailTest() {
		double lat = 0.0; 
	    double lon = 0.0; 
	    int zoom = 19;
		int [] tileDetails=osmt.getTileDetails(lon,lat,zoom);
		assertEquals (tileDetails[0],262144);
		assertEquals (tileDetails[1],262144);
	}
	
	@Test
	public void validateTile(){
		Random r = new Random(); 
		double lat = r.nextDouble() * 90.0; 
	    double lon = r.nextDouble() * 180.0; 
	    int zoom = r.nextInt(19);
		int [] tileDetails=osmt.getTileDetails(lon,lat,zoom);
		assertTrue(tileDetails[0] <= Math.pow(2, 2*zoom));
		assertTrue(tileDetails[1] <= Math.pow(2, 2*zoom));
	}
	
	@Test
	public void validateBBox(){
		BoundingBox bbox=osmt.findBoundingBoxForGivenLocations(populateCoordinates());
		assertTrue(bbox.east <= 180);
		assertTrue(bbox.west <= 180);
		assertTrue(bbox.north <= 180);
		assertTrue(bbox.south <= 180);
		assertTrue(bbox.east >= 0);
		assertTrue(bbox.west >= 0);
		assertTrue(bbox.north >= 0);
		assertTrue(bbox.south >= 0);
		
	}
	
	private ArrayList<Coordinate> populateCoordinates() {
		ArrayList<Coordinate> coords =new ArrayList<Coordinate>();
		Random r = new Random(); 
	   for(int i=0;i<100;i++){
			double lat = r.nextDouble() * 90.0; 
		    double lon = r.nextDouble() * 180.0; 
			coords.add(new Coordinate(lon, lat));
		}
		
		return coords;
	}
	
}
