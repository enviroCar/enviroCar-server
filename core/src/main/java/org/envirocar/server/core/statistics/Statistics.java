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
package org.envirocar.server.core.statistics;

import java.util.Iterator;
import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author jan
 */
public class Statistics implements Iterable<Statistic> {
    private final Iterable<? extends Statistic> delegate;

    public Statistics(Iterable<? extends Statistic> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public Iterator<Statistic> iterator() {
        return new Iterator<Statistic>() {
            final Iterator<? extends Statistic> it = delegate.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Statistic next() {
                return it.next();
            }
        };
    }
}
