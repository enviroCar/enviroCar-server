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
package org.envirocar.server.core;

import org.joda.time.DateTime;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class TemporalFilter {
    private final TemporalFilterOperator operator;
    private final DateTime begin;
    private final DateTime end;

    public TemporalFilter(TemporalFilterOperator operator,
                          DateTime begin,
                          DateTime end) {
        this.operator = Preconditions.checkNotNull(operator);
        this.begin = Preconditions.checkNotNull(begin);
        this.end = end;
    }

    public TemporalFilter(TemporalFilterOperator operator, DateTime instant) {
        this(operator, instant, null);
    }

    public TemporalFilterOperator getOperator() {
        return operator;
    }

    public DateTime getInstant() {
        return begin;
    }

    public DateTime getBegin() {
        return begin;
    }

    public DateTime getEnd() {
        return end;
    }

    public boolean isInstant() {
        return end == null;
    }

    public boolean isInterval() {
        return end != null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(TemporalFilter.class)
                .add("op", getOperator())
                .add("begin", getBegin())
                .add("end", getEnd())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOperator(), getBegin(), getEnd());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TemporalFilter) {
            TemporalFilter other = (TemporalFilter) obj;
            return Objects.equal(getOperator(), other.getOperator()) &&
                   Objects.equal(getBegin(), other.getBegin()) &&
                   Objects.equal(getEnd(), other.getEnd());
        }
        return false;
    }
}
