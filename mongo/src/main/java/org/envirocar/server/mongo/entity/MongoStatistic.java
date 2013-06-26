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
package org.envirocar.server.mongo.entity;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.statistics.Statistic;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Property;

/**
 * TODO JavaDoc
 *
 * @author jan
 */
@Embedded
public class MongoStatistic implements Statistic {
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String MEAN = "mean";
    public static final String USERS = "users";
    public static final String MEASUREMENTS = "measurements";
    public static final String TRACKS = "tracks";
    public static final String PHENOMENON = "phenomenon";
    public static final String SENSORS = "sensors";
    @Embedded
    private MongoPhenomenon phenomenon;
    @Property(TRACKS)
    private long tracks;
    @Property(USERS)
    private long users;
    @Property(MEASUREMENTS)
    private long measurements;
    @Property(SENSORS)
    private long sensors;
    @Property(MEAN)
    private double mean;
    @Property(MIN)
    private double min;
    @Property(MAX)
    private double max;

    @Override
    public Phenomenon getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(Phenomenon phenomenon) {
        this.phenomenon = (MongoPhenomenon) phenomenon;
    }

    @Override
    public long getMeasurements() {
        return measurements;
    }

    public void setMeasurements(long measurements) {
        this.measurements = measurements;
    }

    @Override
    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    @Override
    public long getTracks() {
        return tracks;
    }

    public void setTracks(long tracks) {
        this.tracks = tracks;
    }

    @Override
    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    @Override
    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    @Override
    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public long getSensors() {
        return sensors;
    }

    public void setSensors(long sensors) {
        this.sensors = sensors;
    }
}
