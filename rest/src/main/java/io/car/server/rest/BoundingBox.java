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
package io.car.server.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class BoundingBox {
    private final Coordinate lowerleft;
    private final Coordinate lowerRight;
    private final Coordinate upperRight;
    private final Coordinate upperLeft;

    public BoundingBox(double minx, double miny, double maxx, double maxy) {
        this.upperRight = new Coordinate(maxx, maxy);
        this.upperLeft = new Coordinate(minx, maxy);
        this.lowerRight = new Coordinate(maxx, miny);
        this.lowerleft = new Coordinate(minx, miny);
    }

    public Coordinate[] asCoordinates() {
        return new Coordinate[] {
            lowerLeft(),
            lowerRight(),
            upperRight(),
            upperLeft(),
            lowerLeft()
        };
    }

    public Polygon asPolygon(GeometryFactory factory) {
        return factory.createPolygon(asCoordinates());
    }

    protected Coordinate lowerLeft() {
        return lowerleft;
    }

    protected Coordinate lowerRight() {
        return lowerRight;
    }

    protected Coordinate upperRight() {
        return upperRight;
    }

    protected Coordinate upperLeft() {
        return upperLeft;
    }

    public static BoundingBox valueOf(String bbox) {
        final String[] coords = bbox.split(",");
        if (coords.length != 4) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        try {
            double minx = Double.parseDouble(coords[0]);
            double miny = Double.parseDouble(coords[1]);
            double maxx = Double.parseDouble(coords[2]);
            double maxy = Double.parseDouble(coords[3]);
            return new BoundingBox(minx, miny, maxx, maxy);
        } catch (NumberFormatException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }
}
