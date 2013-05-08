/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.core.util;

import io.car.server.core.exception.GeometryConverterException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public interface GeometryConverter<T> {
    String COORDINATES_KEY = "coordinates";
    String GEOMETRIES_KEY = "geometries";
    String GEOMETRY_COLLECTION_TYPE = "GeometryCollection";
    String LINE_STRING_TYPE = "LineString";
    String MULTI_LINE_STRING_TYPE = "MultiLineString";
    String MULTI_POINT_TYPE = "MultiPoint";
    String MULTI_POLYGON_TYPE = "MultiPolygon";
    String POINT_TYPE = "Point";
    String POLYGON_TYPE = "Polygon";
    String TYPE_KEY = "type";

    Geometry decode(T json) throws GeometryConverterException;
    GeometryCollection decodeGeometryCollection(T json) throws GeometryConverterException;

    Point decodePoint(T json) throws GeometryConverterException;
    LineString decodeLineString(T json) throws GeometryConverterException;
    Polygon decodePolygon(T json) throws GeometryConverterException;
    MultiPoint decodeMultiPoint(T json) throws GeometryConverterException;
    MultiLineString decodeMultiLineString(T json) throws GeometryConverterException;
    MultiPolygon decodeMultiPolygon(T json) throws GeometryConverterException;
    

    T encode(Geometry value) throws GeometryConverterException;
    T encode(GeometryCollection geometry) throws GeometryConverterException;
    
    T encode(Point geometry) throws GeometryConverterException;
    T encode(LineString geometry) throws GeometryConverterException;
    T encode(Polygon geometry) throws GeometryConverterException;

    T encode(MultiLineString geometry) throws GeometryConverterException;
    T encode(MultiPoint geometry) throws GeometryConverterException;
    T encode(MultiPolygon geometry) throws GeometryConverterException;
}
