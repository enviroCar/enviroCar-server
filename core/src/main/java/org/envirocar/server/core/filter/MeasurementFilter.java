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
package org.envirocar.server.core.filter;

import org.envirocar.server.core.SpatialFilter;
import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.pagination.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MeasurementFilter {
    private final Track track;
    private final User user;
    private final SpatialFilter spatialFilter;
    private final Pagination pagination;
    private final TemporalFilter temporalFilter;

    public MeasurementFilter(Track t, User u, SpatialFilter sf,
                             TemporalFilter tf, Pagination p) {
        this.track = t;
        this.user = u;
        this.spatialFilter = sf;
        this.pagination = p;
        this.temporalFilter = tf;
    }

    public MeasurementFilter(Track t, User u, SpatialFilter g, Pagination p) {
        this(t, u, g, null, p);
    }

    public MeasurementFilter(SpatialFilter g, Pagination p) {
        this(null, null, g, null, p);
    }

    public MeasurementFilter(Track t, SpatialFilter g, Pagination p) {
        this(t, null, g, null, p);
    }

    public MeasurementFilter(User u, SpatialFilter g, Pagination p) {
        this(null, u, g, null, p);
    }

    public MeasurementFilter(Track t, Pagination p) {
        this(t, null, null, null, p);
    }

    public MeasurementFilter(User u, Pagination p) {
        this(null, u, null, null, p);
    }

    public MeasurementFilter(Track t) {
        this(t, null, null, null, null);
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

    public SpatialFilter getSpatialFilter() {
        return spatialFilter;
    }

    public boolean hasSpatialFilter() {
        return spatialFilter != null;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public boolean hasPagination() {
        return pagination != null;
    }

    public TemporalFilter getTemporalFilter() {
        return temporalFilter;
    }

    public boolean hasTemporalFilter() {
        return temporalFilter != null;
    }
}
