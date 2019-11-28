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
package org.envirocar.server.mongo.util;

import org.envirocar.server.core.TemporalFilter;
import org.joda.time.DateTime;
import dev.morphia.query.FieldEnd;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MorphiaUtils {
    private MorphiaUtils() {
    }

    public static void temporalFilter(FieldEnd<?> thisBegin, FieldEnd<?> thisEnd, TemporalFilter temporalFilter) {
        final DateTime other = temporalFilter.getInstant();
        final DateTime otherEnd = temporalFilter.getEnd();
        final DateTime otherBegin = temporalFilter.getBegin();
        switch (temporalFilter.getOperator()) {
            case after:
                if (temporalFilter.isInstant()) {
                    thisBegin.greaterThan(other);
                } else {
                    thisBegin.greaterThan(otherEnd);
                }
                break;
            case before:
                if (temporalFilter.isInstant()) {
                    thisEnd.lessThan(other);
                } else {
                    thisEnd.lessThan(otherBegin);
                }
                break;
            case begins:
                thisBegin.equal(otherBegin);
                thisEnd.lessThan(otherEnd);
                break;
            case begunBy:
                if (temporalFilter.isInstant()) {
                    thisBegin.equal(other);
                } else {
                    thisBegin.equal(otherBegin);
                    thisEnd.greaterThan(otherEnd);
                }
                break;
            case contains:
                if (temporalFilter.isInstant()) {
                    thisBegin.lessThan(other);
                    thisEnd.greaterThan(other);
                } else {
                    thisBegin.lessThan(otherBegin);
                    thisEnd.greaterThan(otherEnd);
                }
                break;
            case during:
                thisBegin.greaterThan(otherBegin);
                thisEnd.lessThan(otherEnd);
                break;
            case endedBy:
                if (temporalFilter.isInstant()) {
                    thisEnd.equal(other);
                } else {
                    thisBegin.lessThan(otherBegin);
                    thisEnd.equal(otherEnd);
                }
                break;
            case ends:
                thisBegin.greaterThan(otherBegin);
                thisEnd.equal(otherEnd);
                break;
            case equals:
                thisBegin.equal(otherBegin);
                thisEnd.equal(otherEnd);
                break;
            case meets:
                thisEnd.equal(otherBegin);
                break;
            case metBy:
                thisBegin.equal(otherEnd);
                break;
            case overlapped:
                thisBegin.greaterThan(otherBegin);
                thisBegin.lessThan(otherEnd);
                thisEnd.greaterThan(otherEnd);
                break;
            case overlaps:
                thisBegin.lessThan(otherBegin);
                thisEnd.greaterThan(otherBegin);
                thisEnd.lessThan(otherEnd);
                break;
            default:
                throw new IllegalArgumentException("unsupported temporal operator: " + temporalFilter.getOperator());
        }
    }

    public static void temporalFilter(FieldEnd<?> time, TemporalFilter temporalFilter) {
        final DateTime instant = temporalFilter.getInstant();
        final DateTime otherEnd = temporalFilter.getEnd();
        final DateTime otherBegin = temporalFilter.getBegin();
        switch (temporalFilter.getOperator()) {
            case after:
                if (temporalFilter.isInstant()) {
                    time.greaterThan(instant);
                } else {
                    time.greaterThan(otherEnd);
                }
                break;
            case before:
                if (temporalFilter.isInstant()) {
                    time.lessThan(instant);
                } else {
                    time.lessThan(otherBegin);
                }
                break;
            case begins:
                time.equal(otherBegin);
                break;
            case ends:
                time.equal(otherEnd);
                break;
            case during:
                time.lessThan(otherEnd);
                time.greaterThan(otherBegin);
                break;
            case equals:
                time.equal(instant);
                break;
            default:
                throw new IllegalArgumentException("unsupported temporal operator: " + temporalFilter.getOperator());
        }
    }
}
