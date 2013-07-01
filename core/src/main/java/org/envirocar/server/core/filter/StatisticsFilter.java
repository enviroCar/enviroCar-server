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
package org.envirocar.server.core.filter;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class StatisticsFilter {
    private final User user;
    private final Track track;
    private final Sensor sensor;

    public StatisticsFilter(User user, Track track, Sensor sensor) {
        this.user = user;
        this.track = track;
        this.sensor = sensor;
    }

    public StatisticsFilter(User user) {
        this(user, null, null);
    }

    public StatisticsFilter(Track track) {
        this(null, track, null);
    }

    public StatisticsFilter(Sensor sensor) {
        this(null, null, sensor);
    }

    public StatisticsFilter() {
        this(null, null, null);
    }

    public User getUser() {
        return user;
    }

    public boolean hasUser() {
        return this.user != null;
    }

    public Track getTrack() {
        return track;
    }

    public boolean hasTrack() {
        return this.track != null;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public boolean hasSensor() {
        return this.sensor != null;
    }
}
