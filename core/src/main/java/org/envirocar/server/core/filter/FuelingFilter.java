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

import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.pagination.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class FuelingFilter {

    private final User user;
    private final TemporalFilter temporalFilter;
    private final Pagination pagination;

    public FuelingFilter(User u, TemporalFilter tf, Pagination p) {
        this.user = u;
        this.pagination = p;
        this.temporalFilter = tf;
    }

    public FuelingFilter(User u, Pagination p) {
        this(u, null, p);
    }

    public User getUser() {
        return user;
    }

    public boolean hasUser() {
        return user != null;
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
