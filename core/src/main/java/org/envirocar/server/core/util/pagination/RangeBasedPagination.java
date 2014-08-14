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

import com.google.common.base.Objects;
import com.google.common.base.Optional;

public class RangeBasedPagination implements Pagination {
    private final long begin;
    private final long end;
    private final long size;
    private final long page;
    private final boolean paginated;
    private final boolean isFirstChunk;
    private final boolean isPreviousFirstChunk;

    public RangeBasedPagination(long begin, long end) {
        if (end < 0 || end < begin) {
            throw new IllegalArgumentException();
        }
        this.begin = begin;
        this.end = begin + Math.min(end - begin, MAX_PAGE_SIZE - 1);
        this.size = this.end - this.begin + 1;
        this.paginated = this.begin % this.size == 0L;
        this.isFirstChunk = this.begin <= 0L;
        this.isPreviousFirstChunk = this.begin - this.size <= 0;
        this.page = this.paginated ? this.begin/this.size + 1 : -1;
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

    protected boolean isLastChunk(long elements) {
        return this.end >= elements - 1;
    }


    private boolean isNextChunkLast(long elements) {
        return this.end + this.size >= elements - 1;
    }

    @Override
    public Optional<Pagination> first(long elements) {
        if (this.isFirstChunk || !this.paginated) {
            return Optional.absent();
        }
        return optionalRange(0, this.size - 1);
    }

    @Override
    public Optional<Pagination> previous(long elements) {
        if (this.isFirstChunk || this.isPreviousFirstChunk || !this.paginated) {
            return Optional.absent();
        }
        return optionalRange(this.begin - this.size, this.begin - 1);
    }

    @Override
    public Optional<Pagination> next(long elements) {
        if (isLastChunk(elements) || isNextChunkLast(elements) || !this.paginated) {
            return Optional.absent();
        }
        return optionalRange(this.end + 1, this.end + this.size);
    }

    @Override
    public Optional<Pagination> last(long elements) {
        if (isLastChunk(elements) || !this.paginated) {
            return Optional.absent();
        }
        long start = elements % this.size == 0L
                             ? elements - this.size
                             : (long) Math.floor(elements/this.size) * this.size;
        return optionalRange(start, elements - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RangeBasedPagination) {
            RangeBasedPagination that = (RangeBasedPagination) obj;
            return this.getBegin() == that.getBegin() &&
                   this.getEnd() == that.getEnd();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.begin, this.end);
    }

    @Override
    public String toString() {
        return String.format("RangeBasedPagination(%d-%d)", this.begin, this.end);
    }

    protected static Optional<Pagination> optionalRange(long begin, long end) {
        return Optional.<Pagination>of(new RangeBasedPagination(begin, end));
    }
}
