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
package org.envirocar.server.core;

import org.locationtech.jts.geom.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * class for representing spatial filter
 *
 * @author staschc
 */
public class SpatialFilter {

    /**
     * operator of the spatial filter.
     */
    private final SpatialFilterOperator operator;

    /**
     * generic list for keeping additional filter params.
     */
    private final List<Double> params;

    /**
     * geometry used in the spatial filter.
     */
    private final Geometry geom;

    /**
     * generic constructor for spatial filters
     *
     * @param operator the {@link SpatialFilterOperator}
     * @param geom     The {@link Geometry}
     * @param params   The parameters
     */
    public SpatialFilter(SpatialFilterOperator operator, Geometry geom, List<Double> params) {
        this.operator = Objects.requireNonNull(operator);
        this.geom = Optional.ofNullable(geom).map(g -> createGeometry(operator, geom))
                .orElseThrow(NullPointerException::new);
        this.params = Optional.ofNullable(params).orElseGet(Collections::emptyList);
    }


    public SpatialFilterOperator getOperator() {
        return this.operator;
    }

    public List<Double> getParams() {
        return Collections.unmodifiableList(this.params);
    }

    public Geometry getGeom() {
        return this.geom;
    }

    /**
     * enum for keeping the operator that is used in the spatial filter
     */
    public enum SpatialFilterOperator {
        BBOX,
        NEARPOINT
    }

    public static SpatialFilter bbox(Geometry polygon) {
        return new SpatialFilter(SpatialFilterOperator.BBOX, polygon, null);
    }

    public static SpatialFilter nearPoint(Point point, double distance) {
        return new SpatialFilter(SpatialFilterOperator.NEARPOINT, point, Collections.singletonList(distance));
    }

    private static Geometry createGeometry(SpatialFilterOperator operator, Geometry geom) {
        switch (operator) {
            case BBOX:
                return getBoundingBox(geom);
            case NEARPOINT:
                return geom.getCentroid();
            default:
                return geom;
        }
    }

    private static Geometry getBoundingBox(Geometry geom) {
        Envelope envelope = geom.getEnvelopeInternal();
        Coordinate[] coordinates = {
                new Coordinate(envelope.getMinX(), envelope.getMinY()),
                new Coordinate(envelope.getMinX(), envelope.getMaxY()),
                new Coordinate(envelope.getMaxX(), envelope.getMaxY()),
                new Coordinate(envelope.getMaxX(), envelope.getMinY()),
                new Coordinate(envelope.getMinX(), envelope.getMinY())
        };
        GeometryFactory factory = geom.getFactory();
        return factory.createPolygon(factory.createLinearRing(coordinates), null);
    }

}
