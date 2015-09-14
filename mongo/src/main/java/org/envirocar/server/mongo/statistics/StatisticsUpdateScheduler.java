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
package org.envirocar.server.mongo.statistics;

import com.google.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import org.envirocar.server.core.filter.StatisticsFilter;
import org.envirocar.server.mongo.entity.MongoStatisticKey;
import org.envirocar.server.mongo.entity.MongoStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class StatisticsUpdateScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsUpdateScheduler.class);

    private final Object keyStatisticMutex = new Object();
    private Set<MongoStatisticKey> currentKeyStatisticUpdates = new HashSet<>();
    private Set<MongoStatisticKey> updatePendingForKey = new HashSet<>();


    public void updateStatistics(StatisticsFilter filter, MongoStatisticKey key,
            Function<StatisticsFilter, MongoStatistics> calculator, boolean waitForResult) {

        synchronized (this.keyStatisticMutex) {
            if (this.currentKeyStatisticUpdates.contains(key)) {
                /*
                schedule another update
                */
                updatePendingForKey.add(key);
                if (waitForResult) {
                    try {
                        this.keyStatisticMutex.wait();
                    } catch (InterruptedException ex) {
                        LOGGER.warn("interrupted wail waiting for statistsics", ex);
                    }
                }
                return;
            }
            else {
                this.currentKeyStatisticUpdates.add(key);
            }
        }

        LOGGER.info(String.format("starting statistics update for key %s", key));
        calculator.apply(filter);
        LOGGER.info(String.format("finished statistics update for key %s !", key));

        synchronized (this.keyStatisticMutex) {
            this.keyStatisticMutex.notifyAll();
            this.currentKeyStatisticUpdates.remove(key);

            if (updatePendingForKey.contains(key)) {
                updatePendingForKey.remove(key);

                /*
                another update is pending, start it
                */
                if (waitForResult) {
                    /*
                    the thread is waiting, do not use it again and spawn a new
                    */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updateStatistics(filter, key, calculator, false);
                        }
                    }).start();
                }
                else {
                    updateStatistics(filter, key, calculator, false);
                }
            }
        }
    }


}
