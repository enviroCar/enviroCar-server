/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest.provider;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import io.car.server.core.exception.GeometryConverterException;
import io.car.server.rest.coding.GeoJSON;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GeoJSONTest {
    private final Random random = new Random();
    private Coordinate randomCoordinate() {
        return new Coordinate(random.nextInt(), random.nextInt());
    }
    protected LineString randomLineString(GeometryFactory fac) {
        return fac.createLineString(new Coordinate[] { randomCoordinate(),
                                                       randomCoordinate(),
                                                       randomCoordinate() });
    }

    protected MultiLineString randomMultiLineString(GeometryFactory fac) {
        return fac.createMultiLineString(new LineString[] { randomLineString(fac),
                                                            randomLineString(fac),
                                                            randomLineString(fac) });
    }

    protected Point randomPoint(GeometryFactory fac) {
        return fac.createPoint(randomCoordinate());
    }

    protected LinearRing randomLinearRing(GeometryFactory fac) {
        Coordinate p = randomCoordinate();
        LinearRing linearRing = fac.createLinearRing(new Coordinate[] { p,
                                                                        randomCoordinate(),
                                                                        randomCoordinate(),
                                                                        randomCoordinate(),
                                                                        p });
        return linearRing;
    }

    protected Polygon randomPolygon(GeometryFactory fac) {
        return fac.createPolygon(randomLinearRing(fac), new LinearRing[] { randomLinearRing(fac),
                                                                           randomLinearRing(fac),
                                                                           randomLinearRing(fac) });
    }

    protected MultiPoint randomMultiPoint(GeometryFactory fac) {
        return fac.createMultiPoint(new Coordinate[] { randomCoordinate(),
                                                       randomCoordinate(),
                                                       randomCoordinate(),
                                                       randomCoordinate(),
                                                       randomCoordinate(),
                                                       randomCoordinate() });
    }

    protected MultiPolygon randomMultiPolygon(GeometryFactory fac) {
        return fac.createMultiPolygon(new Polygon[] { randomPolygon(fac),
                                                      randomPolygon(fac),
                                                      randomPolygon(fac) });
    }

    protected GeometryCollection randomGeometryCollection(GeometryFactory fac) {
        return fac.createGeometryCollection(new Geometry[] { randomPoint(fac), randomMultiPoint(fac),
                                                             randomLineString(fac), randomMultiLineString(fac),
                                                             randomPolygon(fac), randomMultiPolygon(fac) });
    }

    @Test
    public void readWriteTest() throws GeometryConverterException {
        GeometryFactory fac = new GeometryFactory();
        Geometry col = fac.createGeometryCollection(new Geometry[] { randomGeometryCollection(fac),
                                                                     randomGeometryCollection(fac) });
        GeoJSON conv = new GeoJSON(fac);
        JsonNode json = conv.encode(col);
        Geometry geom = conv.decode(json);
        assertThat(geom, is(equalTo(col)));

    }
}
