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

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface GeoJSONConstants {
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
    String FEATURE_COLLECTION_TYPE = "FeatureCollection";
    String FEATURE_TYPE = "Feature";
    String FEATURES_KEY = "features";
    String PROPERTIES_KEY = "properties";
}
