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
package org.envirocar.server.core.util;

import org.envirocar.server.core.util.pagination.AbstractPaginated;
import org.envirocar.server.core.util.pagination.PaginatedIterable;
import org.envirocar.server.core.util.pagination.Pagination;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class PaginatedIterableImpl<T> extends AbstractPaginated implements PaginatedIterable<T> {
    private final Iterable<? extends T> delegate;

    public PaginatedIterableImpl(Builder<?, ?, T> builder) {
        super(builder.getPagination(), builder.getElements());
        this.delegate = Objects.requireNonNull(builder.getDelegate());
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final Iterator<? extends T> it = delegate.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public T next() {
                return it.next();
            }
        };
    }

    public abstract static class Builder<B extends Builder<B, I, T>, I extends PaginatedIterableImpl<T>, T> {
        private final Iterable<? extends T> delegate;
        private Pagination pagination;
        private long elements;

        public Builder(CloseableIterator<? extends T> delegate) {
            try {
                List<T> a = new ArrayList<T>();
                while (delegate.hasNext()) {
                    final T next = delegate.next();
                    a.add(next);
                }
                this.delegate = a;
            } finally {
                delegate.close();
            }
        }

        public Builder(Iterable<? extends T> delegate) {
            this.delegate = delegate;
        }

        public B withPagination(Pagination pagination) {
            this.pagination = pagination;
            return self();
        }

        public B withElements(long elements) {
            this.elements = elements;
            return self();
        }

        @SuppressWarnings("unchecked")
        private B self() {
            return (B) this;
        }

        private Iterable<? extends T> getDelegate() {
            return delegate;
        }

        private Pagination getPagination() {
            return pagination;
        }

        private long getElements() {
            return elements;
        }

        public abstract I build();
    }
}
