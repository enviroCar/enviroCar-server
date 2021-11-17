/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.core.filter;

import org.envirocar.server.core.SpatialFilter;
import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.pagination.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class TrackFilter {
    private final User user;
    private final SpatialFilter spatialFilter;
    private final TemporalFilter temporalFilter;
    private final Pagination pagination;
    private final TrackStatus status;

    public TrackFilter(User u, SpatialFilter sf, TemporalFilter tf, TrackStatus s, Pagination p) {
        this.user = u;
        this.spatialFilter = sf;
        this.pagination = p;
        this.temporalFilter = tf;
        this.status = s;
    }

    public TrackFilter(User u, SpatialFilter g, Pagination p) {
        this(u, g, null, null, p);
    }

    public TrackFilter(SpatialFilter g, Pagination p) {
        this(null, g, null, null, p);
    }

    public TrackFilter(User u, Pagination p) {
        this(u, null, null, null, p);
    }

    public TrackFilter(User u) {
        this(u, null, null, null, null);
    }

    public TrackFilter(User u, TrackStatus status) {
        this(u, null, null, status, null);
    }

    public TrackFilter(Pagination p) {
        this(null, null, null, null, p);
    }

    public TrackFilter() {
        this(null, null, null, null, null);
    }

    public TrackFilter(TrackStatus status) {
        this(null, null, null, status, null);
    }

    public User getUser() {
        return this.user;
    }

    public boolean hasUser() {
        return this.user != null;
    }

    public SpatialFilter getSpatialFilter() {
        return this.spatialFilter;
    }

    public boolean hasSpatialFilter() {
        return this.spatialFilter != null;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public boolean hasPagination() {
        return this.pagination != null;
    }

    public TemporalFilter getTemporalFilter() {
        return this.temporalFilter;
    }

    public boolean hasTemporalFilter() {
        return this.temporalFilter != null;
    }

    public TrackStatus getStatus() {
        return this.status;
    }

    public boolean hasStatus() {
        return this.status != null;
    }
}
