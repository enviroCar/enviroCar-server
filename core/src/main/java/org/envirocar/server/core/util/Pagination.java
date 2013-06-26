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
package org.envirocar.server.core.util;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Pagination {
    private int size;
    private int page;

    public Pagination() {
        this(0, 0);
    }

    public Pagination(int size, int page) {
        this.size = (size <= 0 || size > 100) ? 100 : size;
        this.page = page <= 0 ? 1 : page;
    }

    public int getSize() {
        return size;
    }

    public int getPage() {
        return page;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

    public Pagination first(long elements) {
        if (page == 1 || page > lastPage(elements)) {
            return null;
        } else {
            return new Pagination(size, 0);
        }
    }

    public Pagination previous(long elements) {
        if (page <= 2 || page > lastPage(elements)) {
            return null;
        } else {
            return new Pagination(size, page - 1);
        }
    }

    public Pagination next(long elements) {
        int lastPage = lastPage(elements);
        if (page >= (lastPage - 1)) {
            return null;
        } else {
            return new Pagination(size, page + 1);
        }
    }

    public Pagination last(long elements) {
        int lastPage = lastPage(elements);
        if (page == lastPage) {
            return null;
        } else {
            return new Pagination(size, lastPage);
        }
    }

    protected int lastPage(long elements) {
        int p = (int) elements / size;
        return (elements % size) == 0 ? p : p + 1;
    }
}
