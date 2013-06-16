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
package io.car.server.core.filter;

import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.util.Pagination;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MeasurementFilter {
    private final Track track;
    private final User user;
    private final Geometry geometry;
    private final Pagination pagination;

    public MeasurementFilter(Track t, User u, Geometry g, Pagination p) {
        this.track = t;
        this.user = u;
        this.geometry = g;
        this.pagination = p;
    }

    public MeasurementFilter(Geometry g, Pagination p) {
        this(null, null, g, p);
    }

    public MeasurementFilter(Track t, Geometry g, Pagination p) {
        this(t, null, g, p);
    }

    public MeasurementFilter(User u, Geometry g, Pagination p) {
        this(null, u, g, p);
    }

    public MeasurementFilter(Track t, Pagination p) {
        this(t, null, null, p);
    }

    public MeasurementFilter(User u, Pagination p) {
        this(null, u, null, p);
    }

    public MeasurementFilter(Track t) {
        this(t, null, null, null);
    }

    public Track getTrack() {
        return track;
    }

    public boolean hasTrack() {
        return track != null;
    }

    public User getUser() {
        return user;
    }

    public boolean hasUser() {
        return user != null;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public boolean hasGeometry() {
        return geometry != null;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public boolean hasPagination() {
        return pagination != null;
    }
}
