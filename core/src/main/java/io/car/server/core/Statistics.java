/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.core;

import io.car.server.core.util.UpCastingIterable;

/**
 *
 * @author jan
 */
public class Statistics extends UpCastingIterable<Statistic>{

    public Statistics(Iterable<? extends Statistic> delegate) {
        super(delegate);
    }
}
