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
package org.envirocar.server.core.entities;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class UserStatisticImpl implements UserStatistic {

    private User user;
    private double distance;
    private double duration;
    private double distanceBelow60kmh;
    private double durationBelow60kmh;
    private double distanceAbove130kmh;
    private double durationAbove130kmh;
    private double distanceNaN;
    private double durationNaN;
    private TrackSummaries trackSummaries;
    
    public UserStatisticImpl(){
    }
    
    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean hasUser() {
        return (this.user != null);
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

    public void setTrackSummaries(TrackSummaries trackSummaries) {
        this.trackSummaries = trackSummaries;
    }

    @Override
    public boolean hasTrackSummaries() {
        return (this.trackSummaries != null);
    }

    @Override
    public DateTime getCreationTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasCreationTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DateTime getModificationTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasModificationTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}