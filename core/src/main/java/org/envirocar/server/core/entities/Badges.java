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

public class Badges extends UpCastingIterable<Badge> {
	
	protected Badges(Iterable<? extends Badge> delegate,
			Pagination pagination, long elements) {
		super(delegate, pagination, elements);
	}
	
	public static BadgeCollectionBuilder from(
            Iterable<? extends Badge> delegate) {
        return new BadgeCollectionBuilder(delegate);
    }


    public static class BadgeCollectionBuilder {
        private Iterable<? extends Badge> delegate;
        private Pagination pagination;
        private long elements;

        public BadgeCollectionBuilder(Iterable<? extends Badge> delegate) {
            this.delegate = delegate;
        }

        public BadgeCollectionBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public BadgeCollectionBuilder withElements(long elements) {
            this.elements = elements;
            return this;
        }

        public Badges build() {
            return new Badges(delegate, pagination, elements);
        }
    }

}
