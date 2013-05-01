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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.base.Preconditions;
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
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class GeoJSONProvider {
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

    public JSONObject encode(Geometry value) throws JSONException {
        if (value == null) {
            return null;
        } else {
            return encodeGeometry(value);
        }
    }

    public Geometry decode(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        } else {
            return decodeGeometry(json, new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326));
        }
    }

    protected JSONObject encodeGeometry(Geometry geometry) throws JSONException {
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
            throw new JSONException("unknown geometry type " + geometry.getGeometryType());
        }
    }

    protected JSONObject encode(Point geometry) throws JSONException {
        Preconditions.checkNotNull(geometry);
        JSONObject json;
        json = new JSONObject();
        json.put(TYPE_KEY, POINT_TYPE);
        json.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    protected JSONObject encode(LineString geometry) throws JSONException {
        Preconditions.checkNotNull(geometry);
        JSONObject json = new JSONObject();
        json.put(TYPE_KEY, LINE_STRING_TYPE);
        json.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    protected JSONObject encode(Polygon geometry) throws JSONException {
        Preconditions.checkNotNull(geometry);
        JSONObject json = new JSONObject();
        json.put(TYPE_KEY, POLYGON_TYPE);
        json.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    protected JSONObject encode(MultiPoint geometry) throws JSONException {
        Preconditions.checkNotNull(geometry);
        JSONObject json = new JSONObject();
        json.put(TYPE_KEY, MULTI_POINT_TYPE);
        JSONArray list = new JSONArray();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.put(encodeCoordinates((Point) geometry.getGeometryN(i)));
        }
        json.put(COORDINATES_KEY, list);
        return json;
    }

    protected JSONObject encode(MultiLineString geometry) throws JSONException {
        Preconditions.checkNotNull(geometry);
        JSONObject json = new JSONObject();
        json.put(TYPE_KEY, MULTI_LINE_STRING_TYPE);
        JSONArray list = new JSONArray();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.put(encodeCoordinates((LineString) geometry.getGeometryN(i)));
        }
        json.put(COORDINATES_KEY, list);
        return json;
    }

    protected JSONObject encode(MultiPolygon geometry) throws JSONException {
        Preconditions.checkNotNull(geometry);
        JSONObject json = new JSONObject();
        json.put(TYPE_KEY, MULTI_POLYGON_TYPE);
        JSONArray list = new JSONArray();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.put(encodeCoordinates((Polygon) geometry.getGeometryN(i)));
        }
        json.put(COORDINATES_KEY, list);
        return json;
    }

    protected JSONObject encode(GeometryCollection geometry) throws JSONException {
        Preconditions.checkNotNull(geometry);
        JSONObject json = new JSONObject();
        json.put(TYPE_KEY, GEOMETRY_COLLECTION_TYPE);
        JSONArray geometries = new JSONArray();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            geometries.put(encodeGeometry(geometry.getGeometryN(i)));
        }
        json.put(GEOMETRIES_KEY, geometries);
        return json;
    }

    protected JSONArray encodeCoordinate(Coordinate coordinate) throws JSONException {
        JSONArray list = new JSONArray();
        list.put(coordinate.x);
        list.put(coordinate.y);
        return list;
    }

    protected JSONArray encodeCoordinates(CoordinateSequence coordinates) throws JSONException {
        JSONArray list = new JSONArray();
        for (int i = 0; i < coordinates.size(); ++i) {
            JSONArray coordinate = new JSONArray();
            coordinate.put(coordinates.getX(i));
            coordinate.put(coordinates.getY(i));
            list.put(coordinate);
        }
        return list;
    }

    protected JSONArray encodeCoordinates(Point geometry) throws JSONException {
        return encodeCoordinate(geometry.getCoordinate());
    }

    protected JSONArray encodeCoordinates(LineString geometry) throws JSONException {
        return encodeCoordinates(geometry.getCoordinateSequence());
    }

    protected JSONArray encodeCoordinates(Polygon geometry) throws JSONException {
        JSONArray list = new JSONArray();
        list.put(encodeCoordinates(geometry.getExteriorRing()));
        for (int i = 0; i < geometry.getNumInteriorRing(); ++i) {
            list.put(encodeCoordinates(geometry.getInteriorRingN(i)));
        }
        return list;
    }

    protected JSONArray requireCoordinates(JSONObject json) throws JSONException {
        if (!json.has(COORDINATES_KEY)) {
            throw new JSONException("missing 'coordinates' field");
        }
        return toList(json.get(COORDINATES_KEY));
    }

    protected Coordinate decodeCoordinate(JSONArray list) throws JSONException {
        if (list.length() != 2) {
            throw new JSONException("coordinates may only have 2 dimensions");
        }
        Object x = list.get(0);
        Object y = list.get(1);
        if (!(x instanceof Number) || !(y instanceof Number)) {
            throw new JSONException("x and y have to be numbers");
        }
        return new Coordinate(((Number) x).doubleValue(), ((Number) y).doubleValue());
    }

    protected JSONArray toList(Object o) throws
            JSONException {
        if (!(o instanceof JSONArray)) {
            throw new JSONException("expected list");
        }
        return (JSONArray) o;
    }

    protected Coordinate[] decodeCoordinates(JSONArray list) throws JSONException {
        Coordinate[] coordinates = new Coordinate[list.length()];
        for (int i = 0; i < list.length(); ++i) {
            coordinates[i] = decodeCoordinate(toList(list.get(i)));
        }
        return coordinates;
    }

    protected Polygon decodePolygonCoordinates(JSONArray coordinates, GeometryFactory factory) throws JSONException {
        if (coordinates.length() < 1) {
            throw new JSONException("missing polygon shell");
        }
        LinearRing shell = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(0))));
        LinearRing[] holes = new LinearRing[coordinates.length() - 1];
        for (int i = 1; i < coordinates.length(); ++i) {
            holes[i - 1] = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(i))));
        }
        return factory.createPolygon(shell, holes);
}

    protected Geometry decodeGeometry(Object o, GeometryFactory factory) throws JSONException {
        if (!(o instanceof JSONObject)) {
            throw new JSONException("Cannot decode " + o);
        }
        JSONObject json = (JSONObject) o;
        if (!json.has(TYPE_KEY)) {
            throw new JSONException("Can not determine geometry type (missing 'type' field)");
        }
        Object to = json.get(TYPE_KEY);
        if (!(to instanceof String)) {
            throw new JSONException("'type' field has to be a string");
        }
        String type = (String) to;
        if (type.equals(POINT_TYPE)) {
            return decodePoint(json, factory);
        } else if (type.equals(MULTI_POINT_TYPE)) {
            return decodeMultiPoint(json, factory);
        } else if (type.equals(LINE_STRING_TYPE)) {
            return decodeLineString(json, factory);
        } else if (type.equals(MULTI_LINE_STRING_TYPE)) {
            return decodeMultiLineString(json, factory);
        } else if (type.equals(POLYGON_TYPE)) {
            return decodePolygon(json, factory);
        } else if (type.equals(MULTI_POLYGON_TYPE)) {
            return decodeMultiPolygon(json, factory);
        } else if (type.equals(GEOMETRY_COLLECTION_TYPE)) {
            return decodeGeometryCollection(json, factory);
        } else {
            throw new JSONException("Unkown geometry type: " + type);
        }
    }

    protected Geometry decodeMultiLineString(JSONObject json, GeometryFactory factory) throws JSONException {
        JSONArray coordinates = requireCoordinates(json);
        LineString[] lineStrings = new LineString[coordinates.length()];
        for (int i = 0; i < coordinates.length(); ++i) {
            Object coords = coordinates.get(i);
            lineStrings[i] = factory.createLineString(decodeCoordinates(toList(coords)));
        }
        return factory.createMultiLineString(lineStrings);
    }

    protected Geometry decodeLineString(JSONObject json, GeometryFactory factory) throws JSONException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return factory.createLineString(coordinates);
    }

    protected Geometry decodeMultiPoint(JSONObject json, GeometryFactory factory) throws JSONException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return factory.createMultiPoint(coordinates);
    }

    protected Geometry decodePoint(JSONObject json, GeometryFactory factory) throws JSONException {
        Coordinate parsed = decodeCoordinate(requireCoordinates(json));
        return factory.createPoint(parsed);
    }

    protected Geometry decodePolygon(JSONObject json, GeometryFactory factory) throws JSONException {
        JSONArray coordinates = requireCoordinates(json);
        return decodePolygonCoordinates(coordinates, factory);
    }

    protected Geometry decodeMultiPolygon(JSONObject json, GeometryFactory factory) throws JSONException {
        JSONArray coordinates = requireCoordinates(json);
        Polygon[] polygons = new Polygon[coordinates.length()];
        for (int i = 0; i < coordinates.length(); ++i) {
            polygons[i] = decodePolygonCoordinates(toList(coordinates.get(i)), factory);
        }
        return factory.createMultiPolygon(polygons);
    }

    protected Geometry decodeGeometryCollection(JSONObject json, GeometryFactory factory) throws JSONException {
        if (!json.has(GEOMETRIES_KEY)) {
            throw new JSONException("missing 'geometries' field");
        }
        JSONArray geometries = toList(json.get(GEOMETRIES_KEY));
        Geometry[] geoms = new Geometry[geometries.length()];
        for (int i = 0; i < geometries.length(); ++i) {
            geoms[i] = decodeGeometry(geometries.get(i), factory);
        }
        return factory.createGeometryCollection(geoms);
    }
}
