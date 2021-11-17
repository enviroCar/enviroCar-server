/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.mongo.util;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.locationtech.jts.geom.*;
import org.bson.BSONObject;
import org.bson.types.BasicBSONList;
import org.envirocar.server.core.exception.GeometryConverterException;
import org.envirocar.server.core.util.GeometryConverter;

import java.util.Objects;

import static org.envirocar.server.core.util.GeoJSONConstants.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GeoBSON extends GeometryConverter.AbstractGeometryConverter<BSONObject> {
    private final GeometryFactory factory;

    @Inject
    public GeoBSON(GeometryFactory factory) {
        this.factory = factory;
    }

    @Override
    public BSONObject encode(Point geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db;
        db = new BasicDBObject();
        db.put(TYPE_KEY, POINT_TYPE);
        db.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return db;
    }

    @Override
    public BSONObject encode(LineString geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, LINE_STRING_TYPE);
        db.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return db;
    }

    @Override
    public BSONObject encode(Polygon geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, POLYGON_TYPE);
        db.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return db;
    }

    @Override
    public BSONObject encode(MultiPoint geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, MULTI_POINT_TYPE);
        BasicDBList list = new BasicDBList();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Point) geometry.getGeometryN(i)));
        }
        db.put(COORDINATES_KEY, list);
        return db;
    }

    @Override
    public BSONObject encode(MultiLineString geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, MULTI_LINE_STRING_TYPE);
        BasicDBList list = new BasicDBList();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((LineString) geometry.getGeometryN(i)));
        }
        db.put(COORDINATES_KEY, list);
        return db;
    }

    @Override
    public BSONObject encode(MultiPolygon geometry) {
        Preconditions.checkNotNull(geometry);
        BasicDBList list = new BasicDBList();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Polygon) geometry.getGeometryN(i)));
        }
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, MULTI_POLYGON_TYPE);
        db.put(COORDINATES_KEY, list);
        return db;
    }

    @Override
    public BSONObject encode(GeometryCollection geometry) throws
            GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        BasicDBList geometries = new BasicDBList();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            geometries.add(encodeGeometry(geometry.getGeometryN(i)));
        }
        BSONObject bson = new BasicDBObject();
        bson.put(TYPE_KEY, GEOMETRY_COLLECTION_TYPE);
        bson.put(GEOMETRIES_KEY, geometries);
        return bson;
    }

    private BSONObject encodeCoordinate(Coordinate coordinate) {
        BasicBSONList list = new BasicBSONList();
        list.add(coordinate.x);
        list.add(coordinate.y);
        return list;
    }

    private BSONObject encodeCoordinates(CoordinateSequence coordinates) {
        BasicBSONList list = new BasicBSONList();
        for (int i = 0; i < coordinates.size(); ++i) {
            BasicBSONList coordinate = new BasicBSONList();
            coordinate.add(coordinates.getX(i));
            coordinate.add(coordinates.getY(i));
            list.add(coordinate);
        }
        return list;
    }

    private BSONObject encodeCoordinates(Point geometry) {
        return encodeCoordinate(geometry.getCoordinate());
    }

    private BSONObject encodeCoordinates(LineString geometry) {
        return encodeCoordinates(geometry.getCoordinateSequence());
    }

    private BSONObject encodeCoordinates(Polygon geometry) {
        BasicBSONList list = new BasicBSONList();
        list.add(encodeCoordinates(geometry.getExteriorRing()));
        for (int i = 0; i < geometry.getNumInteriorRing(); ++i) {
            list.add(encodeCoordinates(geometry.getInteriorRingN(i)));
        }
        return list;
    }

    private BasicDBList requireCoordinates(BSONObject bson) throws GeometryConverterException {
        if (!bson.containsField(COORDINATES_KEY)) {
            throw new GeometryConverterException("missing 'coordinates' field");
        }
        return toList(bson.get(COORDINATES_KEY));
    }

    private Coordinate decodeCoordinate(BasicDBList list) throws GeometryConverterException {
        if (list.size() != 2) {
            throw new GeometryConverterException("coordinates may only have 2 dimensions");
        }
        Object x = list.get(0);
        Object y = list.get(1);
        if (!(x instanceof Number) || !(y instanceof Number)) {
            throw new GeometryConverterException("x and y have to be numbers");
        }
        return new Coordinate(((Number) x).doubleValue(), ((Number) y).doubleValue());
    }

    private BasicDBList toList(Object o) throws GeometryConverterException {
        if (!(o instanceof BasicDBList)) {
            throw new GeometryConverterException("expected list");
        }
        return (BasicDBList) o;
    }

    private Coordinate[] decodeCoordinates(BasicDBList list) throws GeometryConverterException {
        Coordinate[] coordinates = new Coordinate[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            coordinates[i] = decodeCoordinate(toList(list.get(i)));
        }
        return coordinates;
    }

    private Polygon decodePolygonCoordinates(BasicDBList coordinates) throws GeometryConverterException {
        if (coordinates.size() < 1) {
            throw new GeometryConverterException("missing polygon shell");
        }
        LinearRing shell = this.factory
                .createLinearRing(decodeCoordinates(toList(coordinates.get(0))));
        LinearRing[] holes = new LinearRing[coordinates.size() - 1];
        for (int i = 1; i < coordinates.size(); ++i) {
            holes[i - 1] = this.factory
                    .createLinearRing(decodeCoordinates(toList(coordinates
                            .get(i))));
        }
        return this.factory.createPolygon(shell, holes);
    }

    private Geometry decodeGeometry(Object db) throws GeometryConverterException {
        BSONObject bson = (BSONObject) db;
        if (!bson.containsField(TYPE_KEY)) {
            throw new GeometryConverterException("Can not determine geometry type (missing 'type' field)");
        }
        Object to = bson.get(TYPE_KEY);
        if (!(to instanceof String)) {
            throw new GeometryConverterException("'type' field has to be a string");
        }
        String type = (String) to;
        switch (type) {
            case POINT_TYPE:
                return decodePoint(bson);
            case MULTI_POINT_TYPE:
                return decodeMultiPoint(bson);
            case LINE_STRING_TYPE:
                return decodeLineString(bson);
            case MULTI_LINE_STRING_TYPE:
                return decodeMultiLineString(bson);
            case POLYGON_TYPE:
                return decodePolygon(bson);
            case MULTI_POLYGON_TYPE:
                return decodeMultiPolygon(bson);
            case GEOMETRY_COLLECTION_TYPE:
                return decodeGeometryCollection(bson);
            default:
                throw new GeometryConverterException(String.format("Unknown geometry type: %s", type));
        }
    }

    @Override
    public MultiLineString decodeMultiLineString(BSONObject bson) throws GeometryConverterException {
        BasicDBList coordinates = requireCoordinates(bson);
        LineString[] lineStrings = new LineString[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            Object coords = coordinates.get(i);
            lineStrings[i] = this.factory.createLineString(decodeCoordinates(toList(coords)));
        }
        return this.factory.createMultiLineString(lineStrings);
    }

    @Override
    public LineString decodeLineString(BSONObject bson) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(bson));
        return this.factory.createLineString(coordinates);
    }

    @Override
    public MultiPoint decodeMultiPoint(BSONObject bson) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(bson));
        return this.factory.createMultiPointFromCoords(coordinates);
    }

    @Override
    public Point decodePoint(BSONObject bson) throws GeometryConverterException {
        Coordinate parsed = decodeCoordinate(requireCoordinates(bson));
        return this.factory.createPoint(parsed);
    }

    @Override
    public Polygon decodePolygon(BSONObject bson) throws GeometryConverterException {
        BasicDBList coordinates = requireCoordinates(bson);
        return decodePolygonCoordinates(coordinates);
    }

    @Override
    public MultiPolygon decodeMultiPolygon(BSONObject bson) throws GeometryConverterException {
        BasicDBList coordinates = requireCoordinates(bson);
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            polygons[i] = decodePolygonCoordinates(toList(coordinates.get(i)));
        }
        return this.factory.createMultiPolygon(polygons);
    }

    @Override
    public GeometryCollection decodeGeometryCollection(BSONObject bson) throws GeometryConverterException {
        if (!bson.containsField(GEOMETRIES_KEY)) {
            throw new GeometryConverterException("missing 'geometries' field");
        }
        BasicDBList geometries = toList(bson.get(GEOMETRIES_KEY));
        Geometry[] geoms = new Geometry[geometries.size()];
        for (int i = 0; i < geometries.size(); ++i) {
            geoms[i] = decodeGeometry(geometries.get(i));
        }
        return this.factory.createGeometryCollection(geoms);
    }

    @Override
    public Geometry decode(BSONObject json) throws GeometryConverterException {
        return json == null ? null : decodeGeometry(json);
    }
}
