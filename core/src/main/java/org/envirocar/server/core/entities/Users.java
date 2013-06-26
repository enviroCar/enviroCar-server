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
package org.envirocar.server.core.entities;

import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.core.util.UpCastingIterable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Users extends UpCastingIterable<User> {
    public static UsersBuilder from(
            Iterable<? extends User> delegate) {
        return new UsersBuilder(delegate);
    }

    protected Users(Iterable<? extends User> delegate,
                    Pagination pagination, long elements) {
        super(delegate, pagination, elements);
    }

    public static class UsersBuilder {
        private Iterable<? extends User> delegate;
        private Pagination pagination;
        private long elements;

        public UsersBuilder(Iterable<? extends User> delegate) {
            this.delegate = delegate;
        }

        public UsersBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public UsersBuilder withElements(long elements) {
            this.elements = elements;
            return this;
        }

        public Users build() {
            return new Users(delegate, pagination, elements);
        }
    }
}
