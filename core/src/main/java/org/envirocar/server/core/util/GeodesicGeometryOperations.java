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
package org.envirocar.server.core.util;

import java.util.Iterator;
import java.util.List;

import org.envirocar.server.core.entities.Measurement;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticMeasurement;
import org.gavaghan.geodesy.GlobalPosition;

import com.vividsolutions.jts.geom.Coordinate;

public class GeodesicGeometryOperations implements GeometryOperations {
	
	private GeodeticCalculator geoCalc = new GeodeticCalculator();

	@Override
	public double calculateDistance(Measurement m1, Measurement m2) {
		Coordinate coord1 = m1.getGeometry().getCoordinate();
		Coordinate coord2 = m2.getGeometry().getCoordinate();
		return getDistance(coord1.y, coord1.x, coord2.y, coord2.x);
	}

	@Override
	public double calculateLength(List<Measurement> measurements) {
		Iterator<Measurement> it = measurements.iterator();
		double length = 0.0;
		Measurement previousCoordinate;
		if (it.hasNext()) {
			previousCoordinate = it.next();
		} else {
			return 0;
		}
		while (it.hasNext()) {
			Measurement currentCoordinate = it.next();
			length += calculateDistance(previousCoordinate, currentCoordinate);
			previousCoordinate = currentCoordinate;
		}
		return length;
	}

	/**
	 * Returns the geodetic distance of two Coordinates in kilometers.
	 *
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return distance in km
	 */
	private double getDistance(double lat1, double lng1, double lat2, double lng2) {
		// set position 1
		GlobalPosition position1 = new GlobalPosition(lat1, lng1, 0.0);
		// set position 2
		GlobalPosition position2 = new GlobalPosition(lat2, lng2, 0.0);
		// calculate the geodetic measurement
		GeodeticMeasurement geoMeasurement = geoCalc
				.calculateGeodeticMeasurement(Ellipsoid.WGS84, position1,
						position2);
		double p2pKilometers = geoMeasurement.getPointToPointDistance() / 1000.0;
		return p2pKilometers;
	}

}
