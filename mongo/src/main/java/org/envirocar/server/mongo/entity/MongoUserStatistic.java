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
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.server.mongo.entity;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import org.bson.types.ObjectId;

import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.UserStatistic;

/**
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
@Entity("userstatistic")
public class MongoUserStatistic extends MongoEntityBase implements UserStatistic {

    public static final String DIST_TOTAL = "distance";
    public static final String DURA_TOTAL = "duration";
    public static final String DIST_BELOW60KMH = "distanceBelow60kmh";
    public static final String DURA_BELOW60KMH = "durationBelow60kmh";
    public static final String DIST_ABOVE130KMH = "distanceAbove130kmh";
    public static final String DURA_ABOVE130KMH = "durationAbove130kmh";
    public static final String DIST_NAN = "distanceNaN";
    public static final String DURA_NAN = "durationNaN";
    public static final String TRACKSUMMARIES = "trackSummaries";
    public static final String USER = "user";

    @Id
    private ObjectId id = new ObjectId();
    @Property(USER)
    private Key<MongoUser> user;
    @Transient
    private MongoUser _user;
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

    @Override
    public User getUser() {
        if (this._user == null) {
            this._user = getMongoDB().deref(MongoUser.class, this.user);
        }
        return this._user;
    }

    @Override
    public void setUser(User user) {
        this._user = (MongoUser) user;
        this.user = getMongoDB().key(this._user);
    }

    @Override
    public boolean hasUser() {
        return getUser() != null;
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

}
