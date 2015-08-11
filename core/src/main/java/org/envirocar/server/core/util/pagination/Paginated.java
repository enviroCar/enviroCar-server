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
package org.envirocar.server.core.util.pagination;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class Paginated<T> {
    private final Optional<Pagination> current;
    private final Optional<Pagination> last;
    private final Optional<Pagination> first;
    private final Optional<Pagination> prev;
    private final Optional<Pagination> next;
    private final long elements;

    public Paginated(Pagination current, long elements) {
        this.current = Optional.fromNullable(current);
        this.elements = elements;

        if (this.current.isPresent()) {
            this.last = this.current.get().last(this.elements);
            this.first = this.current.get().first(this.elements);
            this.prev = this.current.get().previous(this.elements);
            this.next = this.current.get().next(this.elements);
        } else {
            Optional<Pagination> absent = Optional.absent();
            this.last = absent;
            this.first = absent;
            this.prev = absent;
            this.next = absent;
        }

    }

    public Optional<Pagination> getLast() {
        return last;
    }

    public boolean hasLast() {
        return this.last.isPresent();
    }

    public Optional<Pagination> getNext() {
        return this.next;
    }

    public boolean hasNext() {
        return this.next.isPresent();
    }

    public Optional<Pagination> getCurrent() {
        return this.current;
    }

    public Optional<Pagination> getPrevious() {
        return this.prev;
    }

    public boolean hasPrevious() {
        return this.prev.isPresent();
    }

    public Optional<Pagination> getFirst() {
        return this.first;
    }

    public boolean hasFirst() {
        return this.first.isPresent();
    }

    public boolean isPaginated() {
        return this.current.isPresent();
    }

    public long getTotalCount() {
        return this.elements;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("first", getFirst().orNull())
                .add("previous", getPrevious().orNull())
                .add("current", getCurrent().orNull())
                .add("next", getNext().orNull())
                .add("last", getLast().orNull())
                .toString();
    }
}
