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

import com.google.common.base.MoreObjects;

import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractPaginated implements Paginated {
    private final Pagination current;
    private final long elements;

    public AbstractPaginated(Pagination current, long elements) {
        this.current = current;
        this.elements = elements;
    }

    @Override
    public Optional<Pagination> getCurrent() {
        return Optional.ofNullable(this.current);
    }

    @Override
    public long getTotalCount() {
        return this.elements;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("first", getFirstPage().orElse(null))
                          .add("previous", getPreviousPage().orElse(null))
                          .add("current", getCurrent().orElse(null))
                          .add("next", getNextPage().orElse(null))
                          .add("last", getLastPage().orElse(null))
                          .toString();
    }
}
