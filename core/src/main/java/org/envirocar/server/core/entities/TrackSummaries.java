/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Maurin Radtke <Maurin.Radtke@uni-muenster.de>
 */
public class TrackSummaries {

    private List<TrackSummary> tracklist;

    public TrackSummaries() {
        this.tracklist = new ArrayList<>();
    }

    public TrackSummaries getTrackSummaries() {
        return this;
    }

    public List<TrackSummary> getTrackSummaryList() {
        return this.tracklist;
    }

    public void setTrackSummariesList(List<TrackSummary> trackSummaries) {
        this.tracklist = trackSummaries == null ? new ArrayList<>() : trackSummaries;
    }

    public void addTrackSummary(TrackSummary trackSummary) {
        this.tracklist.add(trackSummary);
    }

    public boolean hasTrackSummaries() {

        return !this.tracklist.isEmpty();
    }

}
