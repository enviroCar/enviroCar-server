/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.mongo.userstatistic;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.envirocar.server.mongo.entity.MongoUserStatistic;
import org.envirocar.server.mongo.entity.MongoUserStatisticKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

/**
 * TODO: This Scheduler manages updates on the UserStatistics while Tracks are added and others are deleted at same
 * time, asynchronously. TODO: The eventBus onTrackDeletion and onNewTrack is unfortunetly not managing the order of
 * these events correctly. Fix this. (errors occur if a certain created track gets deleted immediatly and the eventbus
 * trigger deletion before creation.)
 */
@Singleton
public class UserStatisticUpdateScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatisticUpdateScheduler.class);

    private final Object keyUserStatisticMutex = new Object();
    private final Set<MongoUserStatisticKey> currentKeyUserStatisticUpdates = new HashSet<>();
    private final Set<MongoUserStatisticKey> updatePendingForKey = new HashSet<>();

    public void updateUserStatistic(
            UserStatisticFilter filter,
            MongoUserStatisticKey key,
            Function<UserStatisticFilter, MongoUserStatistic> calculator,
            boolean waitForResult) {

        synchronized (this.keyUserStatisticMutex) {
            if (this.currentKeyUserStatisticUpdates.contains(key)) {
                /*
                schedule another update
                 */
                updatePendingForKey.add(key);
                if (waitForResult) {
                    try {
                        this.keyUserStatisticMutex.wait();
                    } catch (InterruptedException ex) {
                        LOGGER.warn("interrupted wail waiting for userstatistsic", ex);
                    }
                }
                return;
            } else {
                this.currentKeyUserStatisticUpdates.add(key);
            }
        }

        LOGGER.info(String.format("starting userstatistic calculation on get for key %s", key));
        calculator.apply(filter);
        LOGGER.info(String.format("finished userstatistic calculation on get for key %s !", key));

        synchronized (this.keyUserStatisticMutex) {
            this.keyUserStatisticMutex.notifyAll();
            this.currentKeyUserStatisticUpdates.remove(key);

            if (updatePendingForKey.contains(key)) {
                updatePendingForKey.remove(key);

                /*
                another update is pending, start it
                 */
                if (waitForResult) {
                    /*
                    the thread is waiting, do not use it again and spawn a new
                     */
                    new Thread(() -> {
                        updateUserStatistic(filter, key, calculator, false);
                    }).start();
                } else {
                    updateUserStatistic(filter, key, calculator, false);
                }
            }
        }
    }

    public void updateUserStatisticOnNewTrack(
            UserStatisticFilter filter,
            MongoUserStatisticKey key,
            Function<Object[], MongoUserStatistic> calculator,
            boolean waitForResult,
            Track track) {

        synchronized (this.keyUserStatisticMutex) {
            if (this.currentKeyUserStatisticUpdates.contains(key)) {
                /*
                schedule another update
                 */
                updatePendingForKey.add(key);
                if (waitForResult) {
                    try {
                        this.keyUserStatisticMutex.wait();
                    } catch (InterruptedException ex) {
                        LOGGER.warn("interrupted wail waiting for userstatistsic", ex);
                    }
                }
                return;
            } else {
                this.currentKeyUserStatisticUpdates.add(key);
            }
        }

        LOGGER.info(String.format("starting userstatistic update on New Track for key %s", key));
        Object[] obj = new Object[2];
        obj[0] = filter;
        obj[1] = track;
        calculator.apply(obj);
        LOGGER.info(String.format("finished userstatistic update on New Track for key %s", key));

        synchronized (this.keyUserStatisticMutex) {
            this.keyUserStatisticMutex.notifyAll();
            this.currentKeyUserStatisticUpdates.remove(key);

            if (updatePendingForKey.contains(key)) {
                updatePendingForKey.remove(key);

                /*
                another update is pending, start it
                 */
                if (waitForResult) {
                    /*
                    the thread is waiting, do not use it again and spawn a new
                     */
                    new Thread(() -> {
                        updateUserStatisticOnNewTrack(filter, key, calculator, false, track);
                    }).start();
                } else {
                    updateUserStatisticOnNewTrack(filter, key, calculator, false, track);
                }
            }
        }
    }

    public void updateUserStatisticOnTrackDeletion(
            UserStatisticFilter filter,
            MongoUserStatisticKey key,
            Function<Object[], MongoUserStatistic> calculator,
            boolean waitForResult,
            Track track,
            Measurements measurements) {

        synchronized (this.keyUserStatisticMutex) {
            if (this.currentKeyUserStatisticUpdates.contains(key)) {
                /*
                schedule another update
                 */
                updatePendingForKey.add(key);
                if (waitForResult) {
                    try {
                        this.keyUserStatisticMutex.wait();
                    } catch (InterruptedException ex) {
                        LOGGER.warn("interrupted wail waiting for userstatistsic", ex);
                    }
                }
                return;
            } else {
                this.currentKeyUserStatisticUpdates.add(key);
            }
        }

        LOGGER.info(String.format("starting userstatistic update on Track Deletion for key %s", key));
        Object[] obj = new Object[3];
        obj[0] = filter;
        obj[1] = track;
        obj[2] = measurements;
        calculator.apply(obj);
        LOGGER.info(String.format("finished userstatistic update on Track Deletion for key %s", key));

        synchronized (this.keyUserStatisticMutex) {
            this.keyUserStatisticMutex.notifyAll();
            this.currentKeyUserStatisticUpdates.remove(key);

            if (updatePendingForKey.contains(key)) {
                updatePendingForKey.remove(key);

                /*
                another update is pending, start it
                 */
                if (waitForResult) {
                    /*
                    the thread is waiting, do not use it again and spawn a new
                     */
                    new Thread(() -> {
                        updateUserStatisticOnTrackDeletion(filter, key, calculator, false, track, measurements);
                    }).start();
                } else {
                    updateUserStatisticOnTrackDeletion(filter, key, calculator, false, track, measurements);
                }
            }
        }
    }

}
