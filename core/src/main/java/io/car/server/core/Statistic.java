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
