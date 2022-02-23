/*
 * Copyright (C) 2013-2022 The enviroCar project
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
package org.envirocar.server.rest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.locationtech.jts.geom.*;
import org.envirocar.server.core.exception.GeometryConverterException;
import org.envirocar.server.core.util.GeometryConverter;

import java.util.Objects;

import static org.envirocar.server.core.util.GeoJSONConstants.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GeoJSON extends GeometryConverter.AbstractGeometryConverter<JsonNode> {
    protected final GeometryFactory geometryFactory;
    private final JsonNodeCreator jsonFactory;

    @Inject
    public GeoJSON(GeometryFactory geometryFactory, JsonNodeCreator jsonFactory) {
        this.geometryFactory = geometryFactory;
        this.jsonFactory = jsonFactory;
    }

    private JsonNodeCreator getJsonFactory() {
        return this.jsonFactory;
    }

    private GeometryFactory getGeometryFactory() {
        return this.geometryFactory;
    }

    @Override
    public Geometry decode(JsonNode json) throws GeometryConverterException {
        return json == null ? null : decodeGeometry(json);
    }

    @Override
    public ObjectNode encode(Point geometry) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, POINT_TYPE);
        json.set(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    @Override
    public ObjectNode encode(LineString geometry) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, LINE_STRING_TYPE);
        json.set(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    @Override
    public ObjectNode encode(Polygon geometry) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, POLYGON_TYPE);
        json.set(COORDINATES_KEY, encodeCoordinates(geometry));
        return json;
    }

    @Override
    public ObjectNode encode(MultiPoint geometry) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, MULTI_POINT_TYPE);
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Point) geometry.getGeometryN(i)));
        }
        json.set(COORDINATES_KEY, list);
        return json;
    }

    @Override
    public ObjectNode encode(MultiLineString geometry) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, MULTI_LINE_STRING_TYPE);
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((LineString) geometry.getGeometryN(i)));
        }
        json.set(COORDINATES_KEY, list);
        return json;
    }

    @Override
    public ObjectNode encode(MultiPolygon geometry) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = getJsonFactory().objectNode();
        json.put(TYPE_KEY, MULTI_POLYGON_TYPE);
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Polygon) geometry.getGeometryN(i)));
        }
        json.set(COORDINATES_KEY, list);
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
        json.set(GEOMETRIES_KEY, geometries);
        return json;
    }

    private ArrayNode encodeCoordinate(Coordinate coordinate) {
        ArrayNode list = getJsonFactory().arrayNode();
        list.add(coordinate.x);
        list.add(coordinate.y);
        return list;
    }

    private ArrayNode encodeCoordinates(CoordinateSequence coordinates) {
        ArrayNode list = getJsonFactory().arrayNode();
        for (int i = 0; i < coordinates.size(); ++i) {
            ArrayNode coordinate = getJsonFactory().arrayNode();
            coordinate.add(coordinates.getX(i));
            coordinate.add(coordinates.getY(i));
            list.add(coordinate);
        }
        return list;
    }

    private ArrayNode encodeCoordinates(Point geometry) {
        return encodeCoordinate(geometry.getCoordinate());
    }

    private ArrayNode encodeCoordinates(LineString geometry) {
        return encodeCoordinates(geometry.getCoordinateSequence());
    }

    private ArrayNode encodeCoordinates(Polygon geometry) {
        ArrayNode list = getJsonFactory().arrayNode();
        list.add(encodeCoordinates(geometry.getExteriorRing()));
        for (int i = 0; i < geometry.getNumInteriorRing(); ++i) {
            list.add(encodeCoordinates(geometry.getInteriorRingN(i)));
        }
        return list;
    }

    private ArrayNode requireCoordinates(JsonNode json) throws GeometryConverterException {
        if (!json.has(COORDINATES_KEY)) {
            throw new GeometryConverterException("missing 'coordinates' field");
        }
        return toList(json.get(COORDINATES_KEY));
    }

    private Coordinate decodeCoordinate(ArrayNode list) throws GeometryConverterException {
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

    private ArrayNode toList(Object o) throws GeometryConverterException {
        if (!(o instanceof ArrayNode)) {
            throw new GeometryConverterException("expected list");
        }
        return (ArrayNode) o;
    }

    private Coordinate[] decodeCoordinates(ArrayNode list) throws GeometryConverterException {
        Coordinate[] coordinates = new Coordinate[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            coordinates[i] = decodeCoordinate(toList(list.get(i)));
        }
        return coordinates;
    }

    private Polygon decodePolygonCoordinates(ArrayNode coordinates) throws GeometryConverterException {
        if (coordinates.size() < 1) {
            throw new GeometryConverterException("missing polygon shell");
        }
        LinearRing shell = getGeometryFactory()
                .createLinearRing(decodeCoordinates(toList(coordinates.get(0))));
        LinearRing[] holes = new LinearRing[coordinates.size() - 1];
        for (int i = 1; i < coordinates.size(); ++i) {
            holes[i - 1] = getGeometryFactory()
                    .createLinearRing(decodeCoordinates(toList(coordinates
                            .get(i))));
        }
        return getGeometryFactory().createPolygon(shell, holes);
    }

    private Geometry decodeGeometry(JsonNode o) throws GeometryConverterException {
        if (!(o instanceof ObjectNode)) {
            throw new GeometryConverterException(String.format("Cannot decode %s", o));
        }
        ObjectNode json = (ObjectNode) o;
        if (!json.has(TYPE_KEY)) {
            throw new GeometryConverterException("Can not determine geometry type (missing 'type' field)");
        }
        String type = json.path(TYPE_KEY).textValue();
        switch (type) {
            case POINT_TYPE:
                return decodePoint(json);
            case MULTI_POINT_TYPE:
                return decodeMultiPoint(json);
            case LINE_STRING_TYPE:
                return decodeLineString(json);
            case MULTI_LINE_STRING_TYPE:
                return decodeMultiLineString(json);
            case POLYGON_TYPE:
                return decodePolygon(json);
            case MULTI_POLYGON_TYPE:
                return decodeMultiPolygon(json);
            case GEOMETRY_COLLECTION_TYPE:
                return decodeGeometryCollection(json);
            default:
                throw new GeometryConverterException("Unknown geometry type: " + type);
        }
    }

    @Override
    public MultiLineString decodeMultiLineString(JsonNode json) throws GeometryConverterException {
        ArrayNode coordinates = requireCoordinates(json);
        LineString[] lineStrings = new LineString[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            lineStrings[i] = getGeometryFactory().createLineString(decodeCoordinates(toList(coordinates.get(i))));
        }
        return getGeometryFactory().createMultiLineString(lineStrings);
    }

    @Override
    public LineString decodeLineString(JsonNode json) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return getGeometryFactory().createLineString(coordinates);
    }

    @Override
    public MultiPoint decodeMultiPoint(JsonNode json) throws GeometryConverterException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(json));
        return getGeometryFactory().createMultiPointFromCoords(coordinates);
    }

    @Override
    public Point decodePoint(JsonNode json) throws GeometryConverterException {
        Coordinate parsed = decodeCoordinate(requireCoordinates(json));
        return getGeometryFactory().createPoint(parsed);
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
        return getGeometryFactory().createMultiPolygon(polygons);
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
        return getGeometryFactory().createGeometryCollection(geoms);
    }
}
