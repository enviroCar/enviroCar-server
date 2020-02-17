/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server.core.util;

import org.envirocar.server.core.entities.Measurement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class GeodesicGeometryOperationsTest {

	private static final double TOLERANCE = 0.00005;
	private static final double EXPECTED_DISTANCE_NORTH_SOUTH = 10001.9657;
	private static final double EXPECTED_DISTANCE_EAST_WEST = 19903.5934;
	
	private final GeodesicGeometryOperations ggo = new GeodesicGeometryOperations();
	private final GeometryFactory gf = new GeometryFactory();
	
	@Mock
	private Measurement m1;
	@Mock
	private Measurement m2;
	@Mock
	private Measurement m3;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(m1.getGeometry()).thenReturn(gf.createPoint(new Coordinate(0.0, 90.0)));
		when(m2.getGeometry()).thenReturn(gf.createPoint(new Coordinate(0.0, 0.0)));
		when(m3.getGeometry()).thenReturn(gf.createPoint(new Coordinate(180.0, 0.0)));
	}
	
	@Test
	public void testDistanceCalculation() {
		double dist = ggo.calculateDistance(m1, m2);
		double delta = Math.abs(dist - EXPECTED_DISTANCE_NORTH_SOUTH);
		Assert.assertTrue(delta <= TOLERANCE);
		
		dist = ggo.calculateDistance(m3, m2);
		delta = Math.abs(dist - EXPECTED_DISTANCE_EAST_WEST);
		Assert.assertTrue(delta <= TOLERANCE);
	}
	
}
