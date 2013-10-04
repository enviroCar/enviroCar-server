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

public class TermsOfUse extends UpCastingIterable<TermsOfUseInstance> {

	
	protected TermsOfUse(Iterable<? extends TermsOfUseInstance> delegate,
			Pagination pagination, long elements) {
		super(delegate, pagination, elements);
	}
	
	public static TermsOfUseCollectionBuilder from(
            Iterable<? extends TermsOfUseInstance> delegate) {
        return new TermsOfUseCollectionBuilder(delegate);
    }


    public static class TermsOfUseCollectionBuilder {
        private Iterable<? extends TermsOfUseInstance> delegate;
        private Pagination pagination;
        private long elements;

        public TermsOfUseCollectionBuilder(Iterable<? extends TermsOfUseInstance> delegate) {
            this.delegate = delegate;
        }

        public TermsOfUseCollectionBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public TermsOfUseCollectionBuilder withElements(long elements) {
            this.elements = elements;
            return this;
        }

        public TermsOfUse build() {
            return new TermsOfUse(delegate, pagination, elements);
        }
    }

}
