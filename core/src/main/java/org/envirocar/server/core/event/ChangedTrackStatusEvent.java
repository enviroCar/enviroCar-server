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
package org.envirocar.server.core.event;

import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.entities.User;

public class ChangedTrackStatusEvent extends ChangedTrackEvent {
    private final TrackStatus before;

    public ChangedTrackStatusEvent(User user, Track track, TrackStatus before) {
        super(user, track);
        this.before = before;
    }

    public TrackStatus getAfter() {
        return getTrack().getStatus();
    }

    public TrackStatus getBefore() {
        return this.before;
    }

    public boolean matches(TrackStatus before, TrackStatus after) {
        return (before == null || before == getBefore()) && (after == null || after == getAfter());
    }
}
