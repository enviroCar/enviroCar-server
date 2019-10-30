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
package org.envirocar.server.core.util;

import com.vividsolutions.jts.geom.Coordinate;
import org.envirocar.server.core.entities.Measurement;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;

import java.util.Iterator;
import java.util.List;

public class GeodesicGeometryOperations implements GeometryOperations {

    private final GeodeticCalculator geoCalc = new GeodeticCalculator();

    @Override
    public double calculateDistance(Measurement m1, Measurement m2) {
        Coordinate c1 = m1.getGeometry().getCoordinate();
        Coordinate c2 = m2.getGeometry().getCoordinate();
        return getDistance(c1.y, c1.x, c2.y, c2.x);
    }

    @Override
    public double calculateLength(List<Measurement> measurements) {
        Iterator<Measurement> it = measurements.iterator();
        double length = 0.0;
        if (it.hasNext()) {
            Measurement prev = it.next();
            while (it.hasNext()) {
                Measurement curr = it.next();
                length += calculateDistance(prev, curr);
                prev = curr;
            }
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
        GlobalPosition p1 = new GlobalPosition(lat1, lng1, 0.0);
        GlobalPosition p2 = new GlobalPosition(lat2, lng2, 0.0);
        return geoCalc.calculateGeodeticMeasurement(Ellipsoid.WGS84, p1, p2).getPointToPointDistance() / 1000.0;
    }

}
