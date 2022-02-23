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
package org.envirocar.server.core.util;

import org.envirocar.server.core.exception.GeometryConverterException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface GeometryConverter<T> {
    Geometry decode(T t) throws GeometryConverterException;

    GeometryCollection decodeGeometryCollection(T t) throws GeometryConverterException;

    Point decodePoint(T t) throws GeometryConverterException;

    LineString decodeLineString(T t) throws GeometryConverterException;

    Polygon decodePolygon(T t) throws GeometryConverterException;

    MultiPoint decodeMultiPoint(T t) throws GeometryConverterException;

    MultiLineString decodeMultiLineString(T t) throws GeometryConverterException;

    MultiPolygon decodeMultiPolygon(T t) throws GeometryConverterException;

    T encode(Geometry value) throws GeometryConverterException;

    T encode(GeometryCollection geometry) throws GeometryConverterException;

    T encode(Point geometry) throws GeometryConverterException;

    T encode(LineString geometry) throws GeometryConverterException;

    T encode(Polygon geometry) throws GeometryConverterException;

    T encode(MultiLineString geometry) throws GeometryConverterException;

    T encode(MultiPoint geometry) throws GeometryConverterException;

    T encode(MultiPolygon geometry) throws GeometryConverterException;

    abstract class AbstractGeometryConverter<T> implements GeometryConverter<T> {

        @Override
        public T encode(Geometry value) throws GeometryConverterException {
            return value == null ? null : encodeGeometry(value);
        }
        protected T encodeGeometry(Geometry geometry) throws GeometryConverterException {
            Objects.requireNonNull(geometry);
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
                throw new GeometryConverterException(String.format("unknown geometry type %s", geometry.getGeometryType()));
            }
        }
    }
}
