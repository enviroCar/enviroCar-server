/**
 * Copyright (C) 2013 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk 52 North Initiative for Geospatial Open Source Software GmbH Martin-Luther-King-Weg 24 48155
 * Muenster, Germany info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under the terms of the GNU General Public
 * License version 2 as published by the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied WARRANTY OF MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program (see gnu-gpl v2.txt). If
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit
 * the Free Software Foundation web page, http://www.fsf.org.
 */
package io.car.server.rest.provider;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

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

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class GeoJSONProviderTest {
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
    public void readWriteTest() throws JSONException {
        GeometryFactory fac = new GeometryFactory();
        Geometry col = fac.createGeometryCollection(new Geometry[] { randomGeometryCollection(fac),
                                                                     randomGeometryCollection(fac) });
        GeoJSONProvider conv = new GeoJSONProvider();
        JSONObject json = conv.encode(col);
        Geometry geom = conv.decode(json);
        assertThat(geom, is(equalTo(col)));

    }
}
