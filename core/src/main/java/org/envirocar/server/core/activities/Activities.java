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
package org.envirocar.server.core.activities;

import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.core.util.UpCastingIterable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Activities extends UpCastingIterable<Activity> {
    public static Activities.ActivitiesBuilder from(
            Iterable<? extends Activity> delegate) {
        return new ActivitiesBuilder(delegate);
    }

    protected Activities(Iterable<? extends Activity> delegate,
                         Pagination pagination, long elements) {
        super(delegate, pagination, elements);
    }

    public static class ActivitiesBuilder {
        private Iterable<? extends Activity> delegate;
        private Pagination pagination;
        private long elements;

        public ActivitiesBuilder(Iterable<? extends Activity> delegate) {
            this.delegate = delegate;
        }

        public ActivitiesBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public ActivitiesBuilder withElements(long elements) {
            this.elements = elements;
            return this;
        }

        public Activities build() {
            return new Activities(delegate, pagination, elements);
        }
    }
}
