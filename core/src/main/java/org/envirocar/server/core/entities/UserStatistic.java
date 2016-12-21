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

/**
 * Entity to represent a UserStatistic
 *
 * @author Maurin Radtke <Maurin.Radtke@uni-muenster.de>
 */
public interface UserStatistic extends BaseEntity {
    
    User getUser();
    
    void setUser(User user);
    
    boolean hasUser();
    
    double getDistance();
    
    void setDistance(double distance);
    
    double getDuration();
    
    void setDuration(double duration);
    
    double getDistanceBelow60kmh();
    
    void setDistanceBelow60kmh(double distance);
    
    double getDurationBelow60kmh();
    
    void setDurationBelow60kmh(double duration);
    
    double getDistanceAbove130kmh();
    
    void setDistanceAbove130kmh(double distance);
    
    double getDurationAbove130kmh();
    
    void setDurationAbove130kmh(double duration);
    
    double getDistanceNaN();
    
    void setDistanceNaN(double distance);
    
    double getDurationNaN();
    
    void setDurationNaN(double duration);
    
    TrackSummaries getTrackSummaries();
    
    void setTrackSummaries(TrackSummaries trackSummaries);
    
    boolean hasTrackSummaries();
}
