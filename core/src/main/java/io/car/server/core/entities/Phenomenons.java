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
package io.car.server.core.entities;

import io.car.server.core.util.Pagination;
import io.car.server.core.util.UpCastingIterable;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Phenomenons extends UpCastingIterable<Phenomenon> {
    public static PhenomenonsBuilder from(
            Iterable<? extends Phenomenon> delegate) {
        return new PhenomenonsBuilder(delegate);
    }

    protected Phenomenons(Iterable<? extends Phenomenon> delegate,
                          Pagination pagination, int elements) {
        super(delegate, pagination, elements);
    }

    public static class PhenomenonsBuilder {
        private Iterable<? extends Phenomenon> delegate;
        private Pagination pagination;
        private int elements;

        public PhenomenonsBuilder(Iterable<? extends Phenomenon> delegate) {
            this.delegate = delegate;
        }

        public PhenomenonsBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public PhenomenonsBuilder withElements(int elements) {
            this.elements = elements;
            return this;
        }

        public Phenomenons build() {
            return new Phenomenons(delegate, pagination, elements);
        }
    }
}
