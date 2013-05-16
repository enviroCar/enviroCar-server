/**
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
package io.car.server.core;

/**
 *
 * @author jan
 */
public class Statistic {

    private String phenomenon;
    private int measurements;
    private double mean;
    private double min;
    private double max;

    public String getPhenomenon() {
        return phenomenon;
    }

    public Statistic setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
        return this;
    }

    public int getMeasurements() {
        return measurements;
    }

    public Statistic setMeasurements(int measurements) {
        this.measurements = measurements;
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
