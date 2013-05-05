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
package io.car.server.mongo.convert;

import org.bson.BSONObject;
import org.bson.types.BasicBSONList;

import com.github.jmkgreen.morphia.converters.SimpleValueConverter;
import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.github.jmkgreen.morphia.mapping.MappedField;
import com.github.jmkgreen.morphia.mapping.MappingException;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
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
public class GeometryConverter extends TypeConverter implements SimpleValueConverter {
    public static final String TYPE_KEY = "type";
    public static final String GEOMETRY_COLLECTION_TYPE = "GeometryCollection";
    public static final String POINT_TYPE = "Point";
    public static final String MULTI_POINT_TYPE = "MultiPoint";
    public static final String LINE_STRING_TYPE = "LineString";
    public static final String MULTI_LINE_STRING_TYPE = "MultiLineString";
    public static final String POLYGON_TYPE = "Polygon";
    public static final String MULTI_POLYGON_TYPE = "MultiPolygon";
    public static final String GEOMETRIES_KEY = "geometries";
    public static final String COORDINATES_KEY = "coordinates";
    private final GeometryFactory factory;

    @Inject
    public GeometryConverter(GeometryFactory factory) {
        super(Geometry.class, GeometryCollection.class,
              Point.class, MultiPoint.class,
              LineString.class, MultiLineString.class,
              Polygon.class, MultiPolygon.class);
        this.factory = factory;
    }

    @Override
    public BSONObject encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        } else if (value instanceof Geometry) {
            return encodeGeometry((Geometry) value);
        } else {
            throw new MappingException("value is not a geometry");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Geometry decode(Class targetClass, Object db, MappedField optionalExtraInfo) {
        if (db == null) {
            return null;
        } else {
            return decodeGeometry(db);
        }
    }

    protected BSONObject encodeGeometry(Geometry geometry) {
        Preconditions.checkNotNull(geometry);
        if (geometry.isEmpty()) {
            return null;
        } else if (geometry instanceof Point) {
            return encode((Point) geometry);
        } else if (geometry instanceof LineString) {
            return encode((LineString) geometry);
        } else if (geometry instanceof Polygon) {
            return encode((Polygon) geometry);
        } else if (geometry instanceof MultiPoint) {
            return encode((MultiPoint) geometry);
        } else if (geometry instanceof MultiLineString) {
            return encode((MultiLineString) geometry);
        } else if (geometry instanceof MultiPolygon) {
            return encode((MultiPolygon) geometry);
        } else if (geometry instanceof GeometryCollection) {
            return encode((GeometryCollection) geometry);
        } else {
            throw new MappingException("unknown geometry type " + geometry.getGeometryType());
        }
    }

    protected BSONObject encode(Point geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db;
        db = new BasicDBObject();
        db.put(TYPE_KEY, POINT_TYPE);
        db.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return db;
    }

    protected BSONObject encode(LineString geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, LINE_STRING_TYPE);
        db.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return db;
    }

    protected BSONObject encode(Polygon geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, POLYGON_TYPE);
        db.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return db;
    }

    protected BSONObject encode(MultiPoint geometry) {
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

    protected BSONObject encode(MultiLineString geometry) {
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

    protected BSONObject encode(MultiPolygon geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject db = new BasicDBObject();
        db.put(TYPE_KEY, MULTI_POLYGON_TYPE);
        BasicDBList list = new BasicDBList();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Polygon) geometry.getGeometryN(i)));
        }
        db.put(COORDINATES_KEY, list);
        return db;
    }

    protected BSONObject encode(GeometryCollection geometry) {
        Preconditions.checkNotNull(geometry);
        BSONObject bson = new BasicDBObject();
        bson.put(TYPE_KEY, GEOMETRY_COLLECTION_TYPE);
        BasicDBList geometries = new BasicDBList();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            geometries.add(encodeGeometry(geometry.getGeometryN(i)));
        }
        bson.put(GEOMETRIES_KEY, geometries);
        return bson;
    }

    protected BSONObject encodeCoordinate(Coordinate coordinate) {
        BasicBSONList list = new BasicBSONList();
        list.add(coordinate.x);
        list.add(coordinate.y);
        return list;
    }

    protected BSONObject encodeCoordinates(CoordinateSequence coordinates) {
        BasicBSONList list = new BasicBSONList();
        for (int i = 0; i < coordinates.size(); ++i) {
            BasicBSONList coordinate = new BasicBSONList();
            coordinate.add(coordinates.getX(i));
            coordinate.add(coordinates.getY(i));
            list.add(coordinate);
        }
        return list;
    }

    protected BSONObject encodeCoordinates(Point geometry) {
        return encodeCoordinate(geometry.getCoordinate());
    }

    protected BSONObject encodeCoordinates(LineString geometry) {
        return encodeCoordinates(geometry.getCoordinateSequence());
    }

    protected BSONObject encodeCoordinates(Polygon geometry) {
        BasicBSONList list = new BasicBSONList();
        list.add(encodeCoordinates(geometry.getExteriorRing()));
        for (int i = 0; i < geometry.getNumInteriorRing(); ++i) {
            list.add(encodeCoordinates(geometry.getInteriorRingN(i)));
        }
        return list;
    }

    protected BasicDBList requireCoordinates(BSONObject bson) {
        if (!bson.containsField(COORDINATES_KEY)) {
            throw new MappingException("missing 'coordinates' field");
        }
        return toList(bson.get(COORDINATES_KEY));
    }

    protected Coordinate decodeCoordinate(BasicDBList list) {
        if (list.size() != 2) {
            throw new MappingException("coordinates may only have 2 dimensions");
        }
        Object x = list.get(0);
        Object y = list.get(1);
        if (!(x instanceof Number) || !(y instanceof Number)) {
            throw new MappingException("x and y have to be numbers");
        }
        return new Coordinate(((Number) x).doubleValue(), ((Number) y).doubleValue());
    }

    protected BasicDBList toList(Object o) {
        if (!(o instanceof BasicDBList)) {
            throw new MappingException("expected list");
        }
        return (BasicDBList) o;
    }

    protected Coordinate[] decodeCoordinates(BasicDBList list) {
        Coordinate[] coordinates = new Coordinate[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            coordinates[i] = decodeCoordinate(toList(list.get(i)));
        }
        return coordinates;
    }

    protected Polygon decodePolygonCoordinates(BasicDBList coordinates) throws
            MappingException {
        if (coordinates.size() < 1) {
            throw new MappingException("missing polygon shell");
        }
        LinearRing shell = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(0))));
        LinearRing[] holes = new LinearRing[coordinates.size() - 1];
        for (int i = 1; i < coordinates.size(); ++i) {
            holes[i - 1] = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(i))));
        }
        return factory.createPolygon(shell, holes);
    }

    protected Geometry decodeGeometry(Object db) {
        if (!(db instanceof BSONObject)) {
            throw new MappingException("Cannot decode " + db);
        }
        BSONObject bson = (BSONObject) db;
        if (!bson.containsField(TYPE_KEY)) {
            throw new MappingException("Can not determine geometry type (missing 'type' field)");
        }
        Object to = bson.get(TYPE_KEY);
        if (!(to instanceof String)) {
            throw new MappingException("'type' field has to be a string");
        }
        String type = (String) to;
        if (type.equals(POINT_TYPE)) {
            return decodePoint(bson);
        } else if (type.equals(MULTI_POINT_TYPE)) {
            return decodeMultiPoint(bson);
        } else if (type.equals(LINE_STRING_TYPE)) {
            return decodeLineString(bson);
        } else if (type.equals(MULTI_LINE_STRING_TYPE)) {
            return decodeMultiLineString(bson);
        } else if (type.equals(POLYGON_TYPE)) {
            return decodePolygon(bson);
        } else if (type.equals(MULTI_POLYGON_TYPE)) {
            return decodeMultiPolygon(bson);
        } else if (type.equals(GEOMETRY_COLLECTION_TYPE)) {
            return decodeGeometryCollection(bson);
        } else {
            throw new MappingException("Unkown geometry type: " + type);
        }
    }

    protected Geometry decodeMultiLineString(BSONObject bson) {
        BasicDBList coordinates = requireCoordinates(bson);
        LineString[] lineStrings = new LineString[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            Object coords = coordinates.get(i);
            lineStrings[i] = factory.createLineString(decodeCoordinates(toList(coords)));
        }
        return factory.createMultiLineString(lineStrings);
    }

    protected Geometry decodeLineString(BSONObject bson) {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(bson));
        return factory.createLineString(coordinates);
    }

    protected Geometry decodeMultiPoint(BSONObject bson) {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(bson));
        return factory.createMultiPoint(coordinates);
    }

    protected Geometry decodePoint(BSONObject bson) {
        Coordinate parsed = decodeCoordinate(requireCoordinates(bson));
        return factory.createPoint(parsed);
    }

    protected Geometry decodePolygon(BSONObject bson) {
        BasicDBList coordinates = requireCoordinates(bson);
        return decodePolygonCoordinates(coordinates);
    }

    protected Geometry decodeMultiPolygon(BSONObject bson) {
        BasicDBList coordinates = requireCoordinates(bson);
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            polygons[i] = decodePolygonCoordinates(toList(coordinates.get(i)));
        }
        return factory.createMultiPolygon(polygons);
    }

    protected Geometry decodeGeometryCollection(BSONObject bson) {
        if (!bson.containsField(GEOMETRIES_KEY)) {
            throw new MappingException("missing 'geometries' field");
        }
        BasicDBList geometries = toList(bson.get(GEOMETRIES_KEY));
        Geometry[] geoms = new Geometry[geometries.size()];
        for (int i = 0; i < geometries.size(); ++i) {
            geoms[i] = decodeGeometry(geometries.get(i));
        }
        return factory.createGeometryCollection(geoms);
    }
}
