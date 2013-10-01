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

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public enum TemporalFilterOperator {
    after(true, true, true, true),
    before(true, true, true, true),
    begins(true, false, true, false),
    begunBy(true, true, false, false),
    contains(true, true, false, false),
    during(true, false, true, false),
    endedBy(true, true, false, false),
    ends(true, false, true, false),
    equals(true, false, false, true),
    meets(true, false, false, false),
    metBy(true, false, false, false),
    overlapped(true, false, false, false),
    overlaps(true, false, false, false);
    public static final DateTimeFormatter DATE_TIME_NO_MILLIS =
            ISODateTimeFormat.dateTimeNoMillis();
    private final boolean intervalFilteredByInterval;
    private final boolean intervalFilteredByInstant;
    private final boolean instantfilteredByInterval;
    private final boolean instantFilteredByInstant;

    private TemporalFilterOperator(boolean intervalFilteredByInterval,
                           boolean intervalFilteredByInstant,
                           boolean instantfilteredByInterval,
                           boolean instantFilteredByInstant) {
        this.intervalFilteredByInterval = intervalFilteredByInterval;
        this.intervalFilteredByInstant = intervalFilteredByInstant;
        this.instantfilteredByInterval = instantfilteredByInterval;
        this.instantFilteredByInstant = instantFilteredByInstant;
    }

    public boolean supportsIntervalFilteredByInterval() {
        return intervalFilteredByInterval;
    }

    public boolean supportsIntervalFilteredByInstant() {
        return intervalFilteredByInstant;
    }

    public boolean supportsInstantFilteredByInterval() {
        return instantfilteredByInterval;
    }

    public boolean supportsInstantFilteredByInstant() {
        return instantFilteredByInstant;
    }

    public TemporalFilter parseFilterForInstant(String param) {
        if (param.indexOf(',') < 0) {
            if (!supportsInstantFilteredByInstant()) {
                throw new IllegalArgumentException();
            }
        } else if (!supportsInstantFilteredByInterval()) {
            throw new IllegalArgumentException();
        }
        return parseTemporalFilter(param);
    }

    public TemporalFilter parseFilterForInterval(String param) {
        if (param.indexOf(',') < 0) {
            if (!supportsIntervalFilteredByInstant()) {
                throw new IllegalArgumentException();
            }
        } else if (!supportsIntervalFilteredByInterval()) {
            throw new IllegalArgumentException();
        }
        return parseTemporalFilter(param);
    }

    protected TemporalFilter parseTemporalFilter(String param) {
        if (param.indexOf(',') < 0) {
            return new TemporalFilter(this,
                    DATE_TIME_NO_MILLIS.parseDateTime(param));
        } else {
            String[] split = param.split(",");
            if (split.length > 2) {
                throw new IllegalArgumentException();
            }
            return new TemporalFilter(this,
                    DATE_TIME_NO_MILLIS.parseDateTime(split[0]),
                    DATE_TIME_NO_MILLIS.parseDateTime(split[1]));
        }
    }

    
}
