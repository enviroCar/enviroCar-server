/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import org.envirocar.server.core.util.pagination.Paginated;
import org.envirocar.server.core.util.pagination.Pagination;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UpCastingIterable<T> extends Paginated<T> implements Iterable<T> {
    private final Iterable<? extends T> delegate;
    private List<T> items = null;

    public UpCastingIterable(Builder<?, ?, T> builder) {
        super(builder.getPagination(), builder.getElements());
        this.delegate = Preconditions.checkNotNull(builder.getDelegate());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        if (items == null) {
            items = new LinkedList<T>();
            return new Iterator<T>() {
                private final Iterator<? extends T> it = delegate.iterator();

                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public T next() {
                    T t = it.next();
                    items.add(t);
                    return t;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } else {
            return items.iterator();
        }
    }

    public abstract static class Builder<B extends Builder<B, I, T>, I extends UpCastingIterable<T>, T> {
        private Iterable<? extends T> delegate;
        private Pagination pagination;
        private long elements;

        public Builder(Iterable<? extends T> delegate) {
            this.delegate = delegate;
        }

        @SuppressWarnings("unchecked")
        public B withPagination(Pagination pagination) {
            this.pagination = pagination;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B withElements(long elements) {
            this.elements = elements;
            return (B) this;
        }

        protected Iterable<? extends T> getDelegate() {
            return delegate;
        }

        protected Pagination getPagination() {
            return pagination;
        }

        protected long getElements() {
            return elements;
        }

        public abstract I build();
    }

    @Override
    public String toString() {
        return Joiner.on(", ").appendTo(new StringBuilder()
                .append(getClass().getSimpleName())
                .append('['), iterator()).append(']').toString();
    }
}
