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
package org.envirocar.server.core.entities;

import org.envirocar.server.core.util.CloseableIterator;
import org.envirocar.server.core.util.PaginatedIterableImpl;

public class TermsOfUse extends PaginatedIterableImpl<TermsOfUseInstance> {

    protected TermsOfUse(Builder builder) {
        super(builder);
    }

    public static Builder from(CloseableIterator<? extends TermsOfUseInstance> delegate) {
        return new Builder(delegate);
    }

    public static class Builder extends PaginatedIterableImpl.Builder<Builder, TermsOfUse, TermsOfUseInstance> {

        protected Builder(CloseableIterator<? extends TermsOfUseInstance> delegate) {
            super(delegate);
        }

        @Override
        public TermsOfUse build() {
            return new TermsOfUse(this);
        }
    }

}
