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

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.mapping.Mapper;

import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.UserStatistic;

import com.google.common.base.Objects;

import org.joda.time.DateTime;

/**
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
@Entity("userstatistic")
public class MongoUserStatistic implements UserStatistic {

    public static final int METER_TOLERANCE = 1000;    
    public static final int SECOND_TOLERANCE = 10000;

    public static final String KEY = Mapper.ID_KEY;
    public static final String CREATED = "created";
    public static final String DIST_TOTAL = "distance";
    public static final String DURA_TOTAL = "duration";
    public static final String DIST_BELOW60KMH = "distanceBelow60kmh";
    public static final String DURA_BELOW60KMH = "durationBelow60kmh";
    public static final String DIST_ABOVE130KMH = "distanceAbove130kmh";
    public static final String DURA_ABOVE130KMH = "durationAbove130kmh";
    public static final String DIST_NAN = "distanceNaN";
    public static final String DURA_NAN = "durationNaN";
    public static final String TRACKSUMMARIES = "trackSummaries";

    @Id
    @Embedded
    private MongoUserStatisticKey key;
    @Indexed
    @Embedded(CREATED)
    private DateTime created = new DateTime();
    @Property(DIST_TOTAL)
    private double distance;
    @Property(DURA_TOTAL)
    private double duration;
    @Property(DIST_BELOW60KMH)
    private double distanceBelow60kmh;
    @Property(DURA_BELOW60KMH)
    private double durationBelow60kmh;
    @Property(DIST_ABOVE130KMH)
    private double distanceAbove130kmh;
    @Property(DURA_ABOVE130KMH)
    private double durationAbove130kmh;
    @Property(DIST_NAN)
    private double distanceNaN;
    @Property(DURA_NAN)
    private double durationNaN;
    @Property(TRACKSUMMARIES)
    private TrackSummaries trackSummaries;

    public MongoUserStatistic() {
    }

    public MongoUserStatistic(MongoUserStatisticKey key) {
        this.key = key;
        this.trackSummaries = new TrackSummaries();
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
    public double getDistance() {
        return this.distance;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = round_by_meters(distance);
    }

    @Override
    public double getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(double duration) {
        this.duration = round_by_seconds(duration);
    }

    @Override
    public double getDistanceBelow60kmh() {
        return this.distanceBelow60kmh;
    }

    @Override
    public void setDistanceBelow60kmh(double distanceBelow60kmh) {
        this.distanceBelow60kmh = round_by_meters(distanceBelow60kmh);
    }

    @Override
    public double getDurationBelow60kmh() {
        return this.durationBelow60kmh;
    }

    @Override
    public void setDurationBelow60kmh(double durationBelow60kmh) {
        this.durationBelow60kmh = round_by_seconds(durationBelow60kmh);
    }

    @Override
    public double getDistanceAbove130kmh() {
        return this.distanceAbove130kmh;
    }

    @Override
    public void setDistanceAbove130kmh(double distanceAbove130kmh) {
        this.distanceAbove130kmh = round_by_meters(distanceAbove130kmh);
    }

    @Override
    public double getDurationAbove130kmh() {
        return this.durationAbove130kmh;
    }

    @Override
    public void setDurationAbove130kmh(double durationAbove130kmh) {
        this.durationAbove130kmh = round_by_seconds(durationAbove130kmh);
    }

    @Override
    public double getDistanceNaN() {
        return this.distanceNaN;
    }

    @Override
    public void setDistanceNaN(double distance) {
        this.distanceNaN = round_by_meters(distance);
    }

    @Override
    public double getDurationNaN() {
        return this.durationNaN;
    }

    @Override
    public void setDurationNaN(double duration) {
        this.durationNaN = round_by_seconds(duration);
    }

    @Override
    public TrackSummaries getTrackSummaries() {
        return this.trackSummaries;
    }

    @Override
    public void setTrackSummaries(TrackSummaries trackSummaries) {
        this.trackSummaries = trackSummaries;
    }

    @Override
    public boolean hasTrackSummaries() {
        return (this.trackSummaries != null);
    }
    
    private double round_by_meters(double distance){
        double rounded = Math.round(distance * METER_TOLERANCE);
        return rounded / METER_TOLERANCE;
    }
    
    private double round_by_seconds(double duration){
        double rounded = Math.round(duration * SECOND_TOLERANCE);
        return rounded / SECOND_TOLERANCE;
    }

}
