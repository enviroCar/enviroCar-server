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
package io.car.server.core.statistics;

import io.car.server.core.entities.Phenomenon;

/**
 *
 * @author jan
 */
public class Statistic {
    private Phenomenon phenomenon;
    private long tracks;
    private long users;
    private long measurements;
    private double mean;
    private double min;
    private double max;

    public Phenomenon getPhenomenon() {
        return phenomenon;
    }

    public Statistic setPhenomenon(Phenomenon phenomenon) {
        this.phenomenon = phenomenon;
        return this;
    }

    public long getMeasurements() {
        return measurements;
    }

    public Statistic setMeasurements(long measurements) {
        this.measurements = measurements;
        return this;
    }

    public long getUsers() {
        return users;
    }

    public Statistic setUsers(long users) {
        this.users = users;
        return this;
    }

    public long getTracks() {
        return tracks;
    }

    public Statistic setTracks(long tracks) {
        this.tracks = tracks;
        return this;
    }

    public double getMean() {
        return mean;
    }

    public Statistic setMean(double mean) {
        this.mean = mean;
        return this;
    }

    public double getMin() {
        return min;
    }

    public Statistic setMin(double min) {
        this.min = min;
        return this;
    }

    public double getMax() {
        return max;
    }

    public Statistic setMax(double max) {
        this.max = max;
        return this;
    }
}
