/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.envirocar.server.mongo.util;

import org.envirocar.server.core.TemporalFilter;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.query.FieldEnd;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MorphiaUtils {
    private MorphiaUtils() {
    }

    public static void temporalFilter(
            FieldEnd<?> thisBegin,
            FieldEnd<?> thisEnd,
            TemporalFilter tf) {
        final DateTime other = tf.getInstant();
        final DateTime otherEnd = tf.getEnd();
        final DateTime otherBegin = tf.getBegin();
        switch (tf.getOperator()) {
            case after:
                if (tf.isInstant()) {
                    thisBegin.greaterThan(other);
                } else {
                    thisBegin.greaterThan(otherEnd);
                }
                break;
            case before:
                if (tf.isInstant()) {
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
                if (tf.isInstant()) {
                    thisBegin.equal(other);
                } else {
                    thisBegin.equal(otherBegin);
                    thisEnd.greaterThan(otherEnd);
                }
                break;
            case contains:
                if (tf.isInstant()) {
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
                if (tf.isInstant()) {
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
                throw new IllegalArgumentException(
                        "unsupported temporal operator: " +
                        tf.getOperator());
        }
    }

    public static void temporalFilter(
            FieldEnd<?> time,
            TemporalFilter tf) {
        final DateTime instant = tf.getInstant();
        final DateTime otherEnd = tf.getEnd();
        final DateTime otherBegin = tf.getBegin();
        switch (tf.getOperator()) {
            case after:
                if (tf.isInstant()) {
                    time.greaterThan(instant);
                } else {
                    time.greaterThan(otherEnd);
                }
                break;
            case before:
                if (tf.isInstant()) {
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
                throw new IllegalArgumentException(
                        "unsupported temporal operator: " +
                        tf.getOperator());
        }
    }
}
