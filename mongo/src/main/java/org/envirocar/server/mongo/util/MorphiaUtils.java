/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import dev.morphia.query.FieldEnd;
import org.envirocar.server.core.TemporalFilter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MorphiaUtils {
    private MorphiaUtils() {
    }

    public static void temporalFilter(FieldEnd<?> begin, FieldEnd<?> end, TemporalFilter tf) {
        switch (tf.getOperator()) {
            case after:
                if (tf.isInstant()) {
                    begin.greaterThan(tf.getInstant());
                } else {
                    begin.greaterThan(tf.getEnd());
                }
                break;
            case before:
                if (tf.isInstant()) {
                    end.lessThan(tf.getInstant());
                } else {
                    end.lessThan(tf.getBegin());
                }
                break;
            case begins:
                begin.equal(tf.getBegin());
                end.lessThan(tf.getEnd());
                break;
            case begunBy:
                if (tf.isInstant()) {
                    begin.equal(tf.getInstant());
                } else {
                    begin.equal(tf.getBegin());
                    end.greaterThan(tf.getEnd());
                }
                break;
            case contains:
                if (tf.isInstant()) {
                    begin.lessThan(tf.getInstant());
                    end.greaterThan(tf.getInstant());
                } else {
                    begin.lessThan(tf.getBegin());
                    end.greaterThan(tf.getEnd());
                }
                break;
            case during:
                begin.greaterThan(tf.getBegin());
                end.lessThan(tf.getEnd());
                break;
            case endedBy:
                if (tf.isInstant()) {
                    end.equal(tf.getInstant());
                } else {
                    begin.lessThan(tf.getBegin());
                    end.equal(tf.getEnd());
                }
                break;
            case ends:
                begin.greaterThan(tf.getBegin());
                end.equal(tf.getEnd());
                break;
            case equals:
                begin.equal(tf.getBegin());
                end.equal(tf.getEnd());
                break;
            case meets:
                end.equal(tf.getBegin());
                break;
            case metBy:
                begin.equal(tf.getEnd());
                break;
            case overlapped:
                begin.greaterThan(tf.getBegin());
                begin.lessThan(tf.getEnd());
                end.greaterThan(tf.getEnd());
                break;
            case overlaps:
                begin.lessThan(tf.getBegin());
                end.greaterThan(tf.getBegin());
                end.lessThan(tf.getEnd());
                break;
            default:
                throw new IllegalArgumentException("unsupported temporal operator: " + tf.getOperator());
        }
    }

    public static void temporalFilter(FieldEnd<?> time, TemporalFilter tf) {
        switch (tf.getOperator()) {
            case after:
                if (tf.isInstant()) {
                    time.greaterThan(tf.getInstant());
                } else {
                    time.greaterThan(tf.getEnd());
                }
                break;
            case before:
                if (tf.isInstant()) {
                    time.lessThan(tf.getInstant());
                } else {
                    time.lessThan(tf.getBegin());
                }
                break;
            case begins:
                time.equal(tf.getBegin());
                break;
            case ends:
                time.equal(tf.getEnd());
                break;
            case during:
                time.lessThan(tf.getEnd());
                time.greaterThan(tf.getBegin());
                break;
            case equals:
                time.equal(tf.getInstant());
                break;
            default:
                throw new IllegalArgumentException("unsupported temporal operator: " + tf.getOperator());
        }
    }
}
