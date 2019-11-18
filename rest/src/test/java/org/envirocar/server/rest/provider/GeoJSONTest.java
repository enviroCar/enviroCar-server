/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.rest.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import org.locationtech.jts.geom.*;
import org.envirocar.server.core.exception.GeometryConverterException;
import org.envirocar.server.rest.GuiceRunner;
import org.envirocar.server.rest.schema.ValidationRule;
import org.envirocar.server.rest.util.GeoJSON;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@RunWith(GuiceRunner.class)
public class GeoJSONTest {

    @Rule
    public final ValidationRule validate = new ValidationRule();
    @Rule
    public final ErrorCollector errors = new ErrorCollector();
    @Inject
    private GeometryFactory geometryFactory;
    @Inject
    private JsonNodeCreator jsonNodeFactory;
    private final Random random = new Random();

    private Coordinate randomCoordinate() {
        return new Coordinate(random.nextInt(), random.nextInt());
    }

    private LineString randomLineString() {
        return geometryFactory.createLineString(new Coordinate[]{
                randomCoordinate(),
                randomCoordinate(),
                randomCoordinate()
        });
    }

    private MultiLineString randomMultiLineString() {
        return geometryFactory.createMultiLineString(new LineString[]{
                randomLineString(),
                randomLineString(),
                randomLineString()});
    }

    private Point randomPoint() {
        return geometryFactory.createPoint(randomCoordinate());
    }

    private LinearRing randomLinearRing() {
        Coordinate p = randomCoordinate();
        return geometryFactory.createLinearRing(new Coordinate[]{
                p,
                randomCoordinate(),
                randomCoordinate(),
                randomCoordinate(),
                p
        });
    }

    private Polygon randomPolygon() {
        return geometryFactory.createPolygon(randomLinearRing(),
                new LinearRing[]{
                        randomLinearRing(),
                        randomLinearRing(),
                        randomLinearRing()
                });
    }

    private MultiPoint randomMultiPoint() {
        return geometryFactory.createMultiPoint(new Coordinate[]{
                randomCoordinate(),
                randomCoordinate(),
                randomCoordinate(),
                randomCoordinate(),
                randomCoordinate(),
                randomCoordinate()
        });
    }

    private MultiPolygon randomMultiPolygon() {
        return geometryFactory.createMultiPolygon(new Polygon[]{
                randomPolygon(),
                randomPolygon(),
                randomPolygon()
        });
    }

    private GeometryCollection randomGeometryCollection() {
        return geometryFactory.createGeometryCollection(new Geometry[]{
                randomPoint(),
                randomMultiPoint(),
                randomLineString(),
                randomMultiLineString(),
                randomPolygon(),
                randomMultiPolygon()
        });
    }

    @Test
    public void testGeometryCollection() {
        readWriteTest(geometryFactory.createGeometryCollection(new Geometry[]{
                randomGeometryCollection(),
                randomGeometryCollection()
        }));
    }

    @Test
    public void testPolygon() {
        readWriteTest(randomPolygon());
    }

    @Test
    public void testMultiPolygon() {
        readWriteTest(randomMultiPolygon());
    }

    @Test
    public void testPoint() {
        readWriteTest(randomPoint());
    }

    @Test
    public void testMultiPoint() {
        readWriteTest(randomMultiPoint());
    }

    @Test
    public void testLineString() {
        readWriteTest(randomLineString());
    }

    @Test
    public void testMultiLineString() {
        readWriteTest(randomMultiLineString());
    }

    protected void readWriteTest(final Geometry geom) {
        try {
            GeoJSON conv = new GeoJSON(geometryFactory, jsonNodeFactory);
            JsonNode json = conv.encode(geom);
            Geometry parsed = conv.decode(json);
            assertThat(geom, is(equalTo(parsed)));
            assertThat(json, is(validate.validInstanceOf("geometry.json#")));
        } catch (GeometryConverterException ex) {
            errors.addError(ex);
        }

    }
}
