package org.envirocar.server.rest.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.schema.GuiceRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceRunner.class)
public class OSMTileRendererTests {
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
	
}
