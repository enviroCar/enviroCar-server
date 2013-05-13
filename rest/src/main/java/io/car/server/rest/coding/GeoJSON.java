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
package io.car.server.rest.coding;

import static io.car.server.core.util.GeoJSONConstants.*;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
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

import io.car.server.core.exception.GeometryConverterException;
import io.car.server.core.util.GeometryConverter;
import io.car.server.rest.EntityDecoder;
import io.car.server.rest.EntityEncoder;
/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class GeoJSON implements GeometryConverter<JSONObject>, EntityDecoder<Geometry>, EntityEncoder<Geometry> {
    private final GeometryFactory factory;

    @Inject
    public GeoJSON(GeometryFactory factory) {
        this.factory = factory;
    }

    @Override
    public JSONObject encode(Geometry value) throws GeometryConverterException {
        if (value == null) {
            return null;
        } else {
            return encodeGeometry(value);
        }
    }

    @Override
    public Geometry decode(JSONObject json) throws GeometryConverterException {
        if (json == null) {
            return null;
        } else {
            return decodeGeometry(json);
        }
    }

    @Override
    public Geometry decode(JSONObject j, MediaType mt) throws JSONException {
        try {
            return decode(j);
        } catch (GeometryConverterException ex) {
            if (ex.getCause() != null && ex.getCause() instanceof JSONException) {
                throw (JSONException) ex.getCause();
            } else {
                throw new JSONException(ex);
            }
        }
    }

    @Override
    public JSONObject encode(Geometry t, MediaType mt) throws JSONException {
        try {
            return encode(t);
        } catch (GeometryConverterException ex) {
            if (ex.getCause() != null && ex.getCause() instanceof JSONException) {
                throw (JSONException) ex.getCause();
            } else {
                throw new JSONException(ex);
            }
        }
    }

    protected JSONObject encodeGeometry(Geometry geometry) throws GeometryConverterException {
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
            throw new GeometryConverterException("unknown geometry type " + geometry.getGeometryType());
        }
    }

    @Override
    public JSONObject encode(Point geometry) throws GeometryConverterException {
        try {
            Preconditions.checkNotNull(geometry);
            JSONObject json;
            json = new JSONObject();
            json.put(TYPE_KEY, POINT_TYPE);
            json.put(COORDINATES_KEY, encodeCoordinates(geometry));
            return json;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public JSONObject encode(LineString geometry) throws GeometryConverterException {
        try {
            Preconditions.checkNotNull(geometry);
            JSONObject json = new JSONObject();
            json.put(TYPE_KEY, LINE_STRING_TYPE);
            json.put(COORDINATES_KEY, encodeCoordinates(geometry));
            return json;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public JSONObject encode(Polygon geometry) throws GeometryConverterException {
        try {
            Preconditions.checkNotNull(geometry);
            JSONObject json = new JSONObject();
            json.put(TYPE_KEY, POLYGON_TYPE);
            json.put(COORDINATES_KEY, encodeCoordinates(geometry));
            return json;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public JSONObject encode(MultiPoint geometry) throws GeometryConverterException {
        try {
            Preconditions.checkNotNull(geometry);
            JSONObject json = new JSONObject();
            json.put(TYPE_KEY, MULTI_POINT_TYPE);
            JSONArray list = new JSONArray();
            for (int i = 0; i < geometry.getNumGeometries(); ++i) {
                list.put(encodeCoordinates((Point) geometry.getGeometryN(i)));
            }
            json.put(COORDINATES_KEY, list);
            return json;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public JSONObject encode(MultiLineString geometry) throws GeometryConverterException {
        try {
            Preconditions.checkNotNull(geometry);
            JSONObject json = new JSONObject();
            json.put(TYPE_KEY, MULTI_LINE_STRING_TYPE);
            JSONArray list = new JSONArray();
            for (int i = 0; i < geometry.getNumGeometries(); ++i) {
                list.put(encodeCoordinates((LineString) geometry.getGeometryN(i)));
            }
            json.put(COORDINATES_KEY, list);
            return json;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public JSONObject encode(MultiPolygon geometry) throws GeometryConverterException {
        try {
            Preconditions.checkNotNull(geometry);
            JSONObject json = new JSONObject();
            json.put(TYPE_KEY, MULTI_POLYGON_TYPE);
            JSONArray list = new JSONArray();
            for (int i = 0; i < geometry.getNumGeometries(); ++i) {
                list.put(encodeCoordinates((Polygon) geometry.getGeometryN(i)));
            }
            json.put(COORDINATES_KEY, list);
            return json;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public JSONObject encode(GeometryCollection geometry) throws GeometryConverterException {
        try {
            Preconditions.checkNotNull(geometry);
            JSONObject json = new JSONObject();
            json.put(TYPE_KEY, GEOMETRY_COLLECTION_TYPE);
            JSONArray geometries = new JSONArray();
            for (int i = 0; i < geometry.getNumGeometries(); ++i) {
                geometries.put(encodeGeometry(geometry.getGeometryN(i)));
            }
            json.put(GEOMETRIES_KEY, geometries);
            return json;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    protected JSONArray encodeCoordinate(Coordinate coordinate) throws GeometryConverterException {
        try {
            JSONArray list = new JSONArray();
            list.put(coordinate.x);
            list.put(coordinate.y);
            return list;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    protected JSONArray encodeCoordinates(CoordinateSequence coordinates) throws GeometryConverterException {
        JSONArray list = new JSONArray();
        try {
            for (int i = 0; i < coordinates.size(); ++i) {
                JSONArray coordinate = new JSONArray();
                coordinate.put(coordinates.getX(i));
                coordinate.put(coordinates.getY(i));
                list.put(coordinate);
            }
            return list;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    protected JSONArray encodeCoordinates(Point geometry) throws GeometryConverterException {
        return encodeCoordinate(geometry.getCoordinate());
    }

    protected JSONArray encodeCoordinates(LineString geometry) throws GeometryConverterException {
        return encodeCoordinates(geometry.getCoordinateSequence());
    }

    protected JSONArray encodeCoordinates(Polygon geometry) throws GeometryConverterException {
        JSONArray list = new JSONArray();
        list.put(encodeCoordinates(geometry.getExteriorRing()));
        for (int i = 0; i < geometry.getNumInteriorRing(); ++i) {
            list.put(encodeCoordinates(geometry.getInteriorRingN(i)));
        }
        return list;
    }

    protected JSONArray requireCoordinates(JSONObject json) throws GeometryConverterException {
        try {
            if (!json.has(COORDINATES_KEY)) {
                throw new GeometryConverterException("missing 'coordinates' field");
            }
            return toList(json.get(COORDINATES_KEY));
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    protected Coordinate decodeCoordinate(JSONArray list) throws GeometryConverterException {
        try {
            if (list.length() != 2) {
                throw new GeometryConverterException("coordinates may only have 2 dimensions");
            }
            Object x = list.get(0);
            Object y = list.get(1);
            if (!(x instanceof Number) || !(y instanceof Number)) {
                throw new GeometryConverterException("x and y have to be numbers");
            }
            return new Coordinate(((Number) x).doubleValue(), ((Number) y).doubleValue());
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    protected JSONArray toList(Object o) throws
            GeometryConverterException {
        if (!(o instanceof JSONArray)) {
            throw new GeometryConverterException("expected list");
        }
        return (JSONArray) o;
    }

    protected Coordinate[] decodeCoordinates(JSONArray list) throws GeometryConverterException {
        try {
            Coordinate[] coordinates = new Coordinate[list.length()];
            for (int i = 0; i < list.length(); ++i) {
                coordinates[i] = decodeCoordinate(toList(list.get(i)));
            }
            return coordinates;
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    protected Polygon decodePolygonCoordinates(JSONArray coordinates) throws GeometryConverterException {
        try {
            if (coordinates.length() < 1) {
                throw new GeometryConverterException("missing polygon shell");
            }
            LinearRing shell = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(0))));
            LinearRing[] holes = new LinearRing[coordinates.length() - 1];
            for (int i = 1; i < coordinates.length(); ++i) {
                holes[i - 1] = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(i))));
            }
            return factory.createPolygon(shell, holes);
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    protected Geometry decodeGeometry(Object o) throws GeometryConverterException {
        try {
            if (!(o instanceof JSONObject)) {
                throw new GeometryConverterException("Cannot decode " + o);
            }
            JSONObject json = (JSONObject) o;
            if (!json.has(TYPE_KEY)) {
                throw new GeometryConverterException("Can not determine geometry type (missing 'type' field)");
            }
            Object to = json.get(TYPE_KEY);
            if (!(to instanceof String)) {
                throw new GeometryConverterException("'type' field has to be a string");
            }
            String type = (String) to;
            if (type.equals(POINT_TYPE)) {
                return decodePoint(json);
            } else if (type.equals(MULTI_POINT_TYPE)) {
                return decodeMultiPoint(json);
            } else if (type.equals(LINE_STRING_TYPE)) {
                return decodeLineString(json);
            } else if (type.equals(MULTI_LINE_STRING_TYPE)) {
                return decodeMultiLineString(json);
            } else if (type.equals(POLYGON_TYPE)) {
                return decodePolygon(json);
            } else if (type.equals(MULTI_POLYGON_TYPE)) {
                return decodeMultiPolygon(json);
            } else if (type.equals(GEOMETRY_COLLECTION_TYPE)) {
                return decodeGeometryCollection(json);
            } else {
                throw new GeometryConverterException("Unkown geometry type: " + type);
            }
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public MultiLineString decodeMultiLineString(JSONObject json) throws GeometryConverterException {
        try {
            JSONArray coordinates = requireCoordinates(json);
            LineString[] lineStrings = new LineString[coordinates.length()];
            for (int i = 0; i < coordinates.length(); ++i) {
                Object coords = coordinates.get(i);
                lineStrings[i] = factory.createLineString(decodeCoordinates(toList(coords)));
            }
            return factory.createMultiLineString(lineStrings);
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public LineString decodeLineString(JSONObject json) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return factory.createLineString(coordinates);
    }

    @Override
    public MultiPoint decodeMultiPoint(JSONObject json) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return factory.createMultiPoint(coordinates);
    }

    @Override
    public Point decodePoint(JSONObject json) throws GeometryConverterException {
        Coordinate parsed = decodeCoordinate(requireCoordinates(json));
        return factory.createPoint(parsed);
    }

    @Override
    public Polygon decodePolygon(JSONObject json) throws GeometryConverterException {
        JSONArray coordinates = requireCoordinates(json);
        return decodePolygonCoordinates(coordinates);
    }

    @Override
    public MultiPolygon decodeMultiPolygon(JSONObject json) throws GeometryConverterException {
        try {
            JSONArray coordinates = requireCoordinates(json);
            Polygon[] polygons = new Polygon[coordinates.length()];
            for (int i = 0; i < coordinates.length(); ++i) {
                polygons[i] = decodePolygonCoordinates(toList(coordinates.get(i)));
            }
            return factory.createMultiPolygon(polygons);
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }

    @Override
    public GeometryCollection decodeGeometryCollection(JSONObject json) throws GeometryConverterException {
        try {
            if (!json.has(GEOMETRIES_KEY)) {
                throw new GeometryConverterException("missing 'geometries' field");
            }
            JSONArray geometries = toList(json.get(GEOMETRIES_KEY));
            Geometry[] geoms = new Geometry[geometries.length()];
            for (int i = 0; i < geometries.length(); ++i) {
                geoms[i] = decodeGeometry(geometries.get(i));
            }
            return factory.createGeometryCollection(geoms);
        } catch (JSONException ex) {
            throw new GeometryConverterException(ex);
        }
    }
}
