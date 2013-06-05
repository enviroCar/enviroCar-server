/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.mongo.entity;

import io.car.server.core.entities.Phenomenon;
import io.car.server.core.statistics.Statistic;

/**
 *
 * @author jan
 */
public class MongoStatistic implements Statistic {
    private Phenomenon phenomenon;
    private long tracks;
    private long users;
    private long measurements;
    private double mean;
    private double min;
    private double max;

    @Override
    public Phenomenon getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(Phenomenon phenomenon) {
        this.phenomenon = phenomenon;
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
}
