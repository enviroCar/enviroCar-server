/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Property;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.statistics.Statistic;

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
        return this.phenomenon;
    }

    public void setPhenomenon(Phenomenon phenomenon) {
        this.phenomenon = (MongoPhenomenon) phenomenon;
    }

    @Override
    public long getMeasurements() {
        return this.measurements;
    }

    public void setMeasurements(long measurements) {
        this.measurements = measurements;
    }

    @Override
    public long getUsers() {
        return this.users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    @Override
    public long getTracks() {
        return this.tracks;
    }

    public void setTracks(long tracks) {
        this.tracks = tracks;
    }

    @Override
    public double getMean() {
        return this.mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    @Override
    public double getMin() {
        return this.min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    @Override
    public double getMax() {
        return this.max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public long getSensors() {
        return this.sensors;
    }

    public void setSensors(long sensors) {
        this.sensors = sensors;
    }

    public static MongoStatistic empty(Phenomenon phenomenon) {
        MongoStatistic mongoStatistic = new MongoStatistic();
        mongoStatistic.setPhenomenon(phenomenon);
        mongoStatistic.setMax(0);
        mongoStatistic.setMean(0);
        mongoStatistic.setMin(0);
        mongoStatistic.setMeasurements(0);
        mongoStatistic.setSensors(0);
        mongoStatistic.setTracks(0);
        mongoStatistic.setUsers(0);
        return mongoStatistic;
    }
}
