/*
 * Copyright (C) 2013 The enviroCar project
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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface GeometryConverter<T> {
    Geometry decode(T t) throws GeometryConverterException;

    GeometryCollection decodeGeometryCollection(T t) throws
            GeometryConverterException;

    Point decodePoint(T t) throws GeometryConverterException;

    LineString decodeLineString(T t) throws GeometryConverterException;

    Polygon decodePolygon(T t) throws GeometryConverterException;

    MultiPoint decodeMultiPoint(T t) throws GeometryConverterException;

    MultiLineString decodeMultiLineString(T t) throws
            GeometryConverterException;

    MultiPolygon decodeMultiPolygon(T t) throws GeometryConverterException;

    T encode(Geometry value) throws GeometryConverterException;

    T encode(GeometryCollection geometry) throws GeometryConverterException;

    T encode(Point geometry) throws GeometryConverterException;

    T encode(LineString geometry) throws GeometryConverterException;

    T encode(Polygon geometry) throws GeometryConverterException;

    T encode(MultiLineString geometry) throws GeometryConverterException;

    T encode(MultiPoint geometry) throws GeometryConverterException;

    T encode(MultiPolygon geometry) throws GeometryConverterException;
}
