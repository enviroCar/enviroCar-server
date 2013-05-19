/*
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
package io.car.server.core.util;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import io.car.server.core.exception.GeometryConverterException;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface GeometryConverter<T> {
    Geometry decode(T json) throws GeometryConverterException;

    GeometryCollection decodeGeometryCollection(T json) throws
            GeometryConverterException;

    Point decodePoint(T json) throws GeometryConverterException;

    LineString decodeLineString(T json) throws GeometryConverterException;

    Polygon decodePolygon(T json) throws GeometryConverterException;

    MultiPoint decodeMultiPoint(T json) throws GeometryConverterException;

    MultiLineString decodeMultiLineString(T json) throws
            GeometryConverterException;

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
