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
package org.envirocar.server.core;

import java.util.Collections;
import java.util.List;

import org.envirocar.server.core.exception.ValidationException;

import com.google.common.base.Preconditions;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * class for representing spatial filter
 *
 * @author staschc
 *
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
     * @param operator
     * @param geom
     * @param params
     */
    public SpatialFilter(SpatialFilterOperator operator,
                         Geometry geom,
                         List<Double> params) {
        switch (operator) {
            case BBOX:
                if (!(geom instanceof Polygon)) {
                    throw new ValidationException("BBOX requires a Polygon");
                }
                break;
            case NEARPOINT:
                if (!(geom instanceof Point)) {
                    throw new ValidationException("NEARPOINT requires a point!");
                }
                break;
        }
        this.operator = Preconditions.checkNotNull(operator);
        this.geom = Preconditions.checkNotNull(geom);
        this.params = params == null ? Collections.emptyList() : params;
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
     *
     */
    public enum SpatialFilterOperator {
        BBOX,
        NEARPOINT
    }

    public static SpatialFilter bbox(Polygon polygon) {
        return new SpatialFilter(SpatialFilterOperator.BBOX, polygon, null);
    }

    public static SpatialFilter nearPoint(Point point, double distance) {
        return new SpatialFilter(SpatialFilterOperator.NEARPOINT, point,
                Collections.singletonList(distance));
    }

}
