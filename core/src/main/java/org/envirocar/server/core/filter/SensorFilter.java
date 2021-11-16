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

import java.util.Collections;
import java.util.Set;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.pagination.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class SensorFilter {
    private final String type;
    private final Set<PropertyFilter> filter;
    private final Pagination pagination;
    private final User user;

    public SensorFilter(String type,
                        User user,
                        Set<PropertyFilter> filter,
                        Pagination pagination) {
        this.type = type;
        this.filter = filter;
        this.pagination = pagination;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean hasUser() {
        return this.user != null;
    }

    public String getType() {
        return type;
    }

    public boolean hasType() {
        return type != null && !type.isEmpty();
    }

    public Set<PropertyFilter> getFilters() {
        return filter != null ? Collections.unmodifiableSet(filter) : null;
    }

    public boolean hasFilters() {
        return filter != null && !filter.isEmpty();
    }

    public Pagination getPagination() {
        return pagination;
    }

    public boolean hasPagination() {
        return pagination != null;
    }
}
