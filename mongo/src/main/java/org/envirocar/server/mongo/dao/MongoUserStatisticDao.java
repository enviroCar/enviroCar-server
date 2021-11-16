/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.mongo.dao;

import com.google.inject.Inject;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.dao.BasicDAO;
import dev.morphia.query.UpdateOperations;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoTrackSummary;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.entity.MongoUserStatistic;
import org.envirocar.server.mongo.entity.MongoUserStatisticKey;
import org.envirocar.server.mongo.util.TrackStatistic;
import org.envirocar.server.mongo.util.TrackStatisticImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class MongoUserStatisticDao implements UserStatisticDao {
    private final BasicDAO<MongoUserStatistic, MongoUserStatisticKey> dao;
    private final DataService dataService;
    private final MongoDB mongoDB;
    private final Map<MongoUserStatisticKey, CompletableFuture<MongoUserStatistic>> updates = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ExecutorService executor = Executors.newWorkStealingPool();

    @Inject
    public MongoUserStatisticDao(MongoDB mongoDB, DataService dataService) {
        this.mongoDB = mongoDB;
        this.dao = new BasicDAO<>(MongoUserStatistic.class, mongoDB.getDatastore());
        this.dataService = dataService;

    }

    @Override
    public long getCount() {
        return this.dao.count();
    }

    @Override
    public UserStatistic get(UserStatisticFilter request) {

        MongoUserStatisticKey key = key(request.getUser());
        MongoUserStatistic result = this.dao.get(key);
        if (result != null) {
            return result;
        }
        try {
            return getFuture(request).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<MongoUserStatistic> getFuture(UserStatisticFilter request) {
        this.lock.writeLock().lock();
        try {
            return this.updates.computeIfAbsent(key(request.getUser()), this::createFuture);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private CompletableFuture<MongoUserStatistic> createFuture(MongoUserStatisticKey key) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return createUserStatistic(key);
            } finally {
                this.lock.writeLock().lock();
                try {
                    this.updates.remove(key);
                } finally {
                    this.lock.writeLock().unlock();
                }
            }
        }, this.executor);
    }

    private MongoUserStatistic createUserStatistic(MongoUserStatisticKey key) {
        MongoUserStatistic userStatistic = new MongoUserStatistic(key);
        for (Track track : getTracks(key)) {
            TrackStatistic trackStatistic = new TrackStatisticImpl(track, getMeasurements(track));
            userStatistic.incNumTracks();
            userStatistic.addTrackSummary(trackStatistic.getSummary());
            if (trackStatistic.isValid()) {
                trackStatistic.addTo(userStatistic);
            }
        }
        this.dao.save(userStatistic);
        return userStatistic;
    }

    @Override
    public void updateStatisticsOnTrackDeletion(Track track, Measurements measurements) {
        MongoUserStatisticKey key = key(track.getUser());
        this.lock.readLock().lock();
        try {
            if (this.updates.containsKey(key)) {
                this.updates.get(key).thenAcceptAsync(s -> updateOnDeletion(track));
                return;
            }
        } finally {
            this.lock.readLock().unlock();
        }
        updateOnDeletion(track);
    }

    private void updateOnDeletion(Track track) {
        // update now
        Datastore datastore = this.mongoDB.getDatastore();

        UpdateOperations<MongoUserStatistic> ops = datastore.createUpdateOperations(MongoUserStatistic.class);
        ops.dec(MongoUserStatistic.NUM_TRACKS);
        ops.removeAll(MongoUserStatistic.TRACK_SUMMARIES, new MongoTrackSummary(track.getIdentifier()));
        TrackStatistic stats = new TrackStatisticImpl(track, getMeasurements(track));
        if (stats.isValid()) {
            ops.dec(MongoUserStatistic.DISTANCE_ABOVE_130KMH, stats.getDistanceAbove130());
            ops.dec(MongoUserStatistic.DISTANCE_BELOW_60KMH, stats.getDistanceBelow60());
            ops.dec(MongoUserStatistic.DISTANCE_NAN, stats.getDistanceNaN());
            ops.dec(MongoUserStatistic.DISTANCE_TOTAL, stats.getDistance());
            ops.dec(MongoUserStatistic.DURATION_ABOVE_130KMH, stats.getDurationAbove130());
            ops.dec(MongoUserStatistic.DURATION_BELOW_60KMH, stats.getDurationBelow60());
            ops.dec(MongoUserStatistic.DURATION_NAN, stats.getDurationNaN());
            ops.dec(MongoUserStatistic.DURATION_TOTAL, stats.getDuration());
        }

        datastore.update(datastore.createQuery(MongoUserStatistic.class)
                                  .field(MongoUserStatistic.ID)
                                  .equal(new MongoUserStatisticKey(getKey(track.getUser())))
                                  .field(MongoUserStatistic.TRACK_SUMMARIES)
                                  .elemMatch(datastore.createQuery(MongoTrackSummary.class)
                                                      .field(MongoTrackSummary.IDENTIFIER)
                                                      .equal(track.getIdentifier())), ops);
    }

    @Override
    public void updateStatisticsOnNewTrack(Track track) {

        MongoUserStatisticKey key = key(track.getUser());
        this.lock.readLock().lock();
        try {
            if (this.updates.containsKey(key)) {
                this.updates.get(key).thenAcceptAsync(s -> updateOnInsertion(key, track));
                return;
            }
        } finally {
            this.lock.readLock().unlock();
        }
        updateOnInsertion(key, track);
    }

    private void updateOnInsertion(MongoUserStatisticKey key, Track track) {
        Datastore datastore = this.mongoDB.getDatastore();

        UpdateOperations<MongoUserStatistic> ops = this.dao.createUpdateOperations();
        TrackStatistic stats = new TrackStatisticImpl(track, getMeasurements(track));
        ops.inc(MongoUserStatistic.NUM_TRACKS);
        ops.push(MongoUserStatistic.TRACK_SUMMARIES, stats.getSummary());

        if (stats.isValid()) {
            ops.inc(MongoUserStatistic.DISTANCE_ABOVE_130KMH, stats.getDistanceAbove130());
            ops.inc(MongoUserStatistic.DISTANCE_BELOW_60KMH, stats.getDistanceBelow60());
            ops.inc(MongoUserStatistic.DISTANCE_NAN, stats.getDistanceNaN());
            ops.inc(MongoUserStatistic.DISTANCE_TOTAL, stats.getDistance());
            ops.inc(MongoUserStatistic.DURATION_ABOVE_130KMH, stats.getDurationAbove130());
            ops.inc(MongoUserStatistic.DURATION_BELOW_60KMH, stats.getDurationBelow60());
            ops.inc(MongoUserStatistic.DURATION_NAN, stats.getDurationNaN());
            ops.inc(MongoUserStatistic.DURATION_TOTAL, stats.getDuration());

        }
        datastore.update(datastore.createQuery(MongoUserStatistic.class)
                                  .field(MongoUserStatistic.ID).equal(key)
                                  .field(MongoUserStatistic.TRACK_SUMMARIES)
                                  .not().elemMatch(datastore.createQuery(MongoTrackSummary.class)
                                                            .field(MongoTrackSummary.IDENTIFIER)
                                                            .equal(track.getIdentifier())), ops);
    }

    private MongoUserStatisticKey key(User user) {
        return new MongoUserStatisticKey(getKey(user));
    }

    private Key<MongoUser> getKey(User user) {
        return this.mongoDB.key((MongoUser) user);
    }

    private Tracks getTracks(MongoUserStatisticKey key) {
        MongoUser user = new MongoUser();
        user.setName((String) key.getUser().getId());
        return this.dataService.getTracks(new TrackFilter(user));
    }

    private Measurements getMeasurements(Track track) {
        return this.dataService.getMeasurements(new MeasurementFilter(track));
    }

}
