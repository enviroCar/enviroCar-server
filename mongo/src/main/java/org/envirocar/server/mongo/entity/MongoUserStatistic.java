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
package org.envirocar.server.mongo.entity;

import com.google.common.base.Objects;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.entities.UserStatistic;
import org.joda.time.DateTime;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.mapping.Mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
@Entity("userstatistic")
public class MongoUserStatistic implements UserStatistic {
    public static final String ID = Mapper.ID_KEY;
    public static final String CREATED = "created";
    public static final String DISTANCE_TOTAL = "distance";
    public static final String DURATION_TOTAL = "duration";
    public static final String DISTANCE_BELOW_60KMH = "distanceBelow60kmh";
    public static final String DURATION_BELOW_60KMH = "durationBelow60kmh";
    public static final String DISTANCE_ABOVE_130KMH = "distanceAbove130kmh";
    public static final String DURATION_ABOVE_130KMH = "durationAbove130kmh";
    public static final String DISTANCE_NAN = "distanceNaN";
    public static final String DURATION_NAN = "durationNaN";
    public static final String TRACK_SUMMARIES = "trackSummaries";
    public static final String NUM_TRACKS = "numTracks";

    @Id
    @Embedded
    private MongoUserStatisticKey key;
    @Indexed
    @Embedded(CREATED)
    private final DateTime created = new DateTime();
    @Property(DISTANCE_TOTAL)
    private double distance;
    @Property(DURATION_TOTAL)
    private double duration;
    @Property(DISTANCE_BELOW_60KMH)
    private double distanceBelow60kmh;
    @Property(DURATION_BELOW_60KMH)
    private double durationBelow60kmh;
    @Property(DISTANCE_ABOVE_130KMH)
    private double distanceAbove130kmh;
    @Property(DURATION_ABOVE_130KMH)
    private double durationAbove130kmh;
    @Property(DISTANCE_NAN)
    private double distanceNaN;
    @Property(DURATION_NAN)
    private double durationNaN;
    @Embedded(TRACK_SUMMARIES)
    private List<MongoTrackSummary> trackSummaries = new ArrayList<>();
    @Property(NUM_TRACKS)
    private int numTracks;

    public MongoUserStatistic() {
    }

    public MongoUserStatistic(MongoUserStatisticKey key) {
        this.key = key;
    }

    public DateTime getCreated() {
        return this.created;
    }

    public MongoUserStatisticKey getKey() {
        return this.key;
    }

    public void setKey(MongoUserStatisticKey key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoUserStatistic other = (MongoUserStatistic) obj;
        return Objects.equal(this.key, other.key);
    }

    @Override
    public int getNumTracks() {
        return numTracks;
    }

    @Override
    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    @Override
    public double getDistance() {
        return this.distance;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public double getDistanceBelow60kmh() {
        return this.distanceBelow60kmh;
    }

    @Override
    public void setDistanceBelow60kmh(double distanceBelow60kmh) {
        this.distanceBelow60kmh = distanceBelow60kmh;
    }

    @Override
    public double getDurationBelow60kmh() {
        return this.durationBelow60kmh;
    }

    @Override
    public void setDurationBelow60kmh(double durationBelow60kmh) {
        this.durationBelow60kmh = durationBelow60kmh;
    }

    @Override
    public double getDistanceAbove130kmh() {
        return this.distanceAbove130kmh;
    }

    @Override
    public void setDistanceAbove130kmh(double distanceAbove130kmh) {
        this.distanceAbove130kmh = distanceAbove130kmh;
    }

    @Override
    public double getDurationAbove130kmh() {
        return this.durationAbove130kmh;
    }

    @Override
    public void setDurationAbove130kmh(double durationAbove130kmh) {
        this.durationAbove130kmh = durationAbove130kmh;
    }

    @Override
    public double getDistanceNaN() {
        return this.distanceNaN;
    }

    @Override
    public void setDistanceNaN(double distance) {
        this.distanceNaN = distance;
    }

    @Override
    public double getDurationNaN() {
        return this.durationNaN;
    }

    @Override
    public void setDurationNaN(double duration) {
        this.durationNaN = duration;
    }

    @Override
    public List<TrackSummary> getTrackSummaries() {
        return Collections.unmodifiableList(trackSummaries);
    }

    @Override
    public void setTrackSummaries(List<TrackSummary> trackSummaries) {
        this.trackSummaries = Optional.ofNullable(trackSummaries).orElseGet(Collections::emptyList).stream()
                                      .map(MongoTrackSummary.class::cast).collect(toList());
    }

    @Override
    public void addTrackSummary(TrackSummary trackSummary) {
        this.trackSummaries.add((MongoTrackSummary) trackSummary);
    }

    @Override
    public void removeTrackSummary(String id) {
        this.trackSummaries.removeIf(s -> s.getIdentifier().equals(id));
    }

}
