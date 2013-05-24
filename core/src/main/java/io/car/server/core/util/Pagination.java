/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.core.util;

import com.google.common.base.Objects;
/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Pagination {
    private int size;
    private int page;

    public Pagination(int size, int page) {
        this.size = size <= 0 ? 100 : size;
        this.page = page <= 0 ? 0 : page;
    }

    public int getSize() {
        return size;
    }

    public int getPage() {
        return page;
    }

    public int getOffset() {
        return page * size;
    }

    public int getLimit() {
        return size;
    }

    public int getFirst() {
        return getOffset() + 1;
    }

    public int getLast() {
        return getFirst() + getSize();
    }

    public Pagination next(int elements) {
        return (getLast() < elements)
               ? new Pagination(getSize(), getPage() + 1)
               : null;
    }

    public Pagination previous() {
        return (getFirst() >= 0)
               ? new Pagination(getSize(), getPage() - 1)
               : null;
    }

    public Pagination last(int elements) {
        return new Pagination(getSize(), elements / getSize());
    }

    public Pagination first() {
        return new Pagination(getSize(), 0);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("first", getFirst())
                .add("last", getLast())
                .toString();
    }
}
