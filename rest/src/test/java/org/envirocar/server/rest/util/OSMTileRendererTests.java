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
