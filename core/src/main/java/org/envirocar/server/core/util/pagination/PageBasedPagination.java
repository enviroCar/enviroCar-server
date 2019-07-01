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
package org.envirocar.server.core.util.pagination;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

public class PageBasedPagination implements Pagination {
	private final long size;
    private final long page;
    private final long begin;
    private final long end;

    public PageBasedPagination() {
        this(0, 0);
    }

    public PageBasedPagination(long size, long page) {
        this.size = size <= 0 ? 100 : Math.min(size, MAX_PAGE_SIZE);
        this.page = page <= 0 ? 1 : page;
        this.begin = (this.page - 1) * this.size;
        this.end = this.size + this.begin;
    }

    @Override
    public long getLimit() {
        return this.size;
    }

    @Override
    public long getBegin() {
        return this.begin;
    }

    @Override
    public long getEnd() {
        return this.end;
    }

    @Override
    public long getPage() {
        return this.page;
    }

    @Override
    public Optional<Pagination> first(long elements) {
        if (page == 1 || page > lastPage(elements)) {
            return Optional.absent();
        } else {
            return Optional.of(new PageBasedPagination(size, 0));
        }
    }

    @Override
    public Optional<Pagination> previous(long elements) {
        if (page <= 2 || page > lastPage(elements)) {
            return Optional.absent();
        } else {
            return Optional.of(new PageBasedPagination(size, page -
                                                                         1));
        }
    }

    @Override
    public Optional<Pagination> next(long elements) {
        long lastPage = lastPage(elements);
        if (page >= (lastPage - 1)) {
            return Optional.absent();
        } else {
            return Optional.of(new PageBasedPagination(size, page +
                                                                         1));
        }
    }

    @Override
    public Optional<Pagination> last(long elements) {
        long lastPage = lastPage(elements);
        if (page == lastPage) {
            return Optional.absent();
        } else {
            return Optional.of(new PageBasedPagination(size, lastPage));
        }
    }

    protected long lastPage(long elements) {
        if (elements == 0) { return 1; }
        long p = elements / size;
        return (elements % size) == 0 ? p : p + 1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.size, this.page);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PageBasedPagination) {
            PageBasedPagination that = (PageBasedPagination) obj;
            return this.getPage() == that.getPage() &&
                   this.getLimit() == that.getLimit();
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("size", this.size)
                .add("page", this.page)
                .toString();
    }

}
