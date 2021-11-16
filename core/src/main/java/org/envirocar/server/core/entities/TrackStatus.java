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
package org.envirocar.server.core.entities;

public enum TrackStatus {
    FINISHED("finished"),
    ONGOING("ongoing");

    private final String value;

    TrackStatus(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static TrackStatus fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        for (TrackStatus status : TrackStatus.values()) {
            if (status.value().equalsIgnoreCase(string)) {
                return status;
            }
        }
        throw new IllegalArgumentException("unsupported TrackStatus: " + string);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
