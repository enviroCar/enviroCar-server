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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GeoJSON extends AbstractEntityCoder<Geometry> implements GeometryConverter<JsonNode> {
    private final GeometryFactory factory;

    @Inject
    public GeoJSON(GeometryFactory factory) {
        this.factory = factory;
    }

    @Override
    public ObjectNode encode(Geometry value) throws GeometryConverterException {
        if (value == null) {
            return null;
        } else {
            return encodeGeometry(value);
        }
    }

    @Override
    public Geometry decode(JsonNode json) throws GeometryConverterException {
        if (json == null) {
            return null;
        } else {
            return decodeGeometry(json);
        }
    }

    @Override
    public Geometry decode(JsonNode j, MediaType mt) {
        try {
            return decode(j);
        } catch (GeometryConverterException ex) {
            throw new WebApplicationException(ex, Status.BAD_REQUEST);
        }
    }

    @Override
    public ObjectNode encode(Geometry t, MediaType mt) {
        try {
            return encode(t);
        } catch (GeometryConverterException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    protected ObjectNode encodeGeometry(Geometry geometry) throws GeometryConverterException {
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
    public ObjectNode encode(Point geometry) throws GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, POINT_TYPE);
        json.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    @Override
    public ObjectNode encode(LineString geometry) throws GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, LINE_STRING_TYPE);
        json.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    @Override
    public ObjectNode encode(Polygon geometry) throws GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, POLYGON_TYPE);
        json.put(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    @Override
    public ObjectNode encode(MultiPoint geometry) throws GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, MULTI_POINT_TYPE);
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Point) geometry.getGeometryN(i)));
        }
        json.put(COORDINATES_KEY, list);
        return json;
    }

    @Override
    public ObjectNode encode(MultiLineString geometry) throws GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, MULTI_LINE_STRING_TYPE);
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((LineString) geometry.getGeometryN(i)));
        }
        json.put(COORDINATES_KEY, list);
        return json;
    }

    @Override
    public ObjectNode encode(MultiPolygon geometry) throws GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, MULTI_POLYGON_TYPE);
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Polygon) geometry.getGeometryN(i)));
        }
        json.put(COORDINATES_KEY, list);
        return json;
    }

    @Override
    public ObjectNode encode(GeometryCollection geometry) throws GeometryConverterException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, GEOMETRY_COLLECTION_TYPE);
        ArrayNode geometries = getJsonFactory().arrayNode();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            geometries.add(encodeGeometry(geometry.getGeometryN(i)));
        }
        json.put(GEOMETRIES_KEY, geometries);
        return json;
    }

    protected ArrayNode encodeCoordinate(Coordinate coordinate) throws GeometryConverterException {
        ArrayNode list = getJsonFactory().arrayNode();
        list.add(coordinate.x);
        list.add(coordinate.y);
        return list;
    }

    protected ArrayNode encodeCoordinates(CoordinateSequence coordinates) throws GeometryConverterException {
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < coordinates.size(); ++i) {
            ArrayNode coordinate = getJsonFactory().arrayNode();
            coordinate.add(coordinates.getX(i));
            coordinate.add(coordinates.getY(i));
            list.add(coordinate);
        }
        return list;
    }

    protected ArrayNode encodeCoordinates(Point geometry) throws GeometryConverterException {
        return encodeCoordinate(geometry.getCoordinate());
    }

    protected ArrayNode encodeCoordinates(LineString geometry) throws GeometryConverterException {
        return encodeCoordinates(geometry.getCoordinateSequence());
    }

    protected ArrayNode encodeCoordinates(Polygon geometry) throws GeometryConverterException {
        ArrayNode list = getJsonFactory().arrayNode();
        list.add(encodeCoordinates(geometry.getExteriorRing()));
        for (int i = 0; i < geometry.getNumInteriorRing(); ++i) {
            list.add(encodeCoordinates(geometry.getInteriorRingN(i)));
        }
        return list;
    }

    protected ArrayNode requireCoordinates(JsonNode json) throws GeometryConverterException {
        if (!json.has(COORDINATES_KEY)) {
            throw new GeometryConverterException("missing 'coordinates' field");
        }
        return toList(json.get(COORDINATES_KEY));
    }

    protected Coordinate decodeCoordinate(ArrayNode list) throws GeometryConverterException {
        if (list.size() != 2) {
            throw new GeometryConverterException("coordinates may only have 2 dimensions");
        }
        Number x = list.get(0).numberValue();
        Number y = list.get(1).numberValue();
        if (x == null || y == null) {
            throw new GeometryConverterException("x and y have to be numbers");
        }
        return new Coordinate(x.doubleValue(), y.doubleValue());
    }

    protected ArrayNode toList(Object o) throws
            GeometryConverterException {
        if (!(o instanceof ArrayNode)) {
            throw new GeometryConverterException("expected list");
        }
        return (ArrayNode) o;
    }

    protected Coordinate[] decodeCoordinates(ArrayNode list) throws GeometryConverterException {
        Coordinate[] coordinates = new Coordinate[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            coordinates[i] = decodeCoordinate(toList(list.get(i)));
        }
        return coordinates;
    }

    protected Polygon decodePolygonCoordinates(ArrayNode coordinates) throws GeometryConverterException {
        if (coordinates.size() < 1) {
            throw new GeometryConverterException("missing polygon shell");
        }
        LinearRing shell = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(0))));
        LinearRing[] holes = new LinearRing[coordinates.size() - 1];
        for (int i = 1; i < coordinates.size(); ++i) {
            holes[i - 1] = factory.createLinearRing(decodeCoordinates(toList(coordinates.get(i))));
        }
        return factory.createPolygon(shell, holes);
    }

    protected Geometry decodeGeometry(Object o) throws GeometryConverterException {
        if (!(o instanceof ObjectNode)) {
            throw new GeometryConverterException("Cannot decode " + o);
        }
        ObjectNode json = (ObjectNode) o;
        if (!json.has(TYPE_KEY)) {
            throw new GeometryConverterException("Can not determine geometry type (missing 'type' field)");
        }
        Object to = json.path(TYPE_KEY).textValue();
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
    }

    @Override
    public MultiLineString decodeMultiLineString(JsonNode json) throws GeometryConverterException {
        ArrayNode coordinates = requireCoordinates(json);
        LineString[] lineStrings = new LineString[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            Object coords = coordinates.get(i);
            lineStrings[i] = factory.createLineString(decodeCoordinates(toList(coords)));
        }
        return factory.createMultiLineString(lineStrings);
    }

    @Override
    public LineString decodeLineString(JsonNode json) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return factory.createLineString(coordinates);
    }

    @Override
    public MultiPoint decodeMultiPoint(JsonNode json) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return factory.createMultiPoint(coordinates);
    }

    @Override
    public Point decodePoint(JsonNode json) throws GeometryConverterException {
        Coordinate parsed = decodeCoordinate(requireCoordinates(json));
        return factory.createPoint(parsed);
    }

    @Override
    public Polygon decodePolygon(JsonNode json) throws GeometryConverterException {
        ArrayNode coordinates = requireCoordinates(json);
        return decodePolygonCoordinates(coordinates);
    }

    @Override
    public MultiPolygon decodeMultiPolygon(JsonNode json) throws GeometryConverterException {
        ArrayNode coordinates = requireCoordinates(json);
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            polygons[i] = decodePolygonCoordinates(toList(coordinates.get(i)));
        }
        return factory.createMultiPolygon(polygons);
    }

    @Override
    public GeometryCollection decodeGeometryCollection(JsonNode json) throws GeometryConverterException {
        if (!json.has(GEOMETRIES_KEY)) {
            throw new GeometryConverterException("missing 'geometries' field");
        }
        ArrayNode geometries = toList(json.get(GEOMETRIES_KEY));
        Geometry[] geoms = new Geometry[geometries.size()];
        for (int i = 0; i < geometries.size(); ++i) {
            geoms[i] = decodeGeometry(geometries.get(i));
        }
        return factory.createGeometryCollection(geoms);
    }
}
