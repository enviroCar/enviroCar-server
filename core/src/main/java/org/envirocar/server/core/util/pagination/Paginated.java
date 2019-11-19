/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.core.util.pagination;

import java.util.Optional;

public interface Paginated {
    Optional<Pagination> getCurrent();

    default Optional<Pagination> getLastPage() {
        return getCurrent().flatMap(p -> p.last(getTotalCount()));
    }

    default Optional<Pagination> getNextPage() {
        return getCurrent().flatMap(p -> p.next(getTotalCount()));
    }

    default Optional<Pagination> getPreviousPage() {
        return getCurrent().flatMap(p -> p.previous(getTotalCount()));
    }

    default Optional<Pagination> getFirstPage() {
        return getCurrent().flatMap(p -> p.first(getTotalCount()));
    }

    default boolean isPaginated() {
        return getCurrent().isPresent();
    }

    long getTotalCount();
}
