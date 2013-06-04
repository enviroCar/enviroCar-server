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
package io.car.server.mongo.dao;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

import io.car.server.core.dao.StatisticsDao;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.statistics.Statistic;
import io.car.server.core.statistics.Statistics;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoEntityBase;
import io.car.server.mongo.entity.MongoMeasurement;
import io.car.server.mongo.entity.MongoMeasurementValue;
import io.car.server.mongo.entity.MongoPhenomenon;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;
import io.car.server.mongo.util.Ops;

/**
 * @author jan
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoStatisticsDao implements StatisticsDao {
    public static final String ID_KEY = Mapper.ID_KEY;
    public static final String AVG_KEY = "avg";
    public static final String MIN_KEY = "min";
    public static final String MAX_KEY = "max";
    public static final String MEASUREMENTS_KEY = "measurements";
    public static final String TRACKS_KEY = "tracks";
    public static final String USERS_KEY = "users";
    private final MongoDB mongoDB;
    @Inject
    public MongoStatisticsDao(MongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }

    protected static String valueOf(String property) {
        return "$" + property;
    }

    protected static String path(String first, String second, String... paths) {
        return Joiner.on(".").join(first, second, (Object[]) paths);
    }

    @Override
    public Statistics getStatisticsForTrack(Track track) {
        return parseStatistics(aggregate(matchesTrack(track),
                                         project(),
                                         unwind(),
                                         group()).results());
    }

    @Override
    public Statistics getStatisticsForUser(User user) {
        return parseStatistics(aggregate(matchesUser(user),
                                         project(),
                                         unwind(),
                                         group()).results());
    }

    @Override
    public Statistics getStatistics() {
        return parseStatistics(aggregate(project(),
                                         unwind(),
                                         group()).results());
    }

    @Override
    public Statistic getStatisticsForTrack(Track track, Phenomenon phenomenon) {
        return parseStatistic(aggregate(matchesTrack(track),
                                        project(),
                                        unwind(),
                                        matchesPhenomenon(phenomenon),
                                        group()).results());
    }

    @Override
    public Statistic getStatisticsForUser(User user, Phenomenon phenomenon) {
        return parseStatistic(aggregate(matchesUser(user),
                                        project(),
                                        unwind(),
                                        matchesPhenomenon(phenomenon),
                                        group()).results());
    }

    @Override
    public Statistic getStatistics(Phenomenon phenomenon) {
        return parseStatistic(aggregate(project(),
                                        unwind(),
                                        matchesPhenomenon(phenomenon),
                                        group()).results());
    }

    @Override
    public Statistics getStatisticsForTrack(Track track, Phenomenons phenomenons) {
        return parseStatistics(aggregate(matchesTrack(track),
                                         project(),
                                         unwind(),
                                         matchesPhenomenon(phenomenons),
                                         group()).results());
}

    @Override
    public Statistics getStatisticsForUser(User user, Phenomenons phenomenons) {
        return parseStatistics(aggregate(matchesUser(user),
                                         project(),
                                         unwind(),
                                         matchesPhenomenon(phenomenons),
                                         group()).results());
}

    @Override
    public Statistics getStatistics(Phenomenons phenomenons) {
        return parseStatistics(aggregate(project(),
                                         unwind(),
                                         matchesPhenomenon(phenomenons),
                                         group()).results());
    }

    protected AggregationOutput aggregate(DBObject firstOp,
                                          DBObject... additionalOps) {
        AggregationOutput result = mongoDB.getDatastore()
                .getCollection(MongoMeasurement.class)
                .aggregate(firstOp, additionalOps);
        result.getCommandResult().throwOnError();
        return result;
}

    protected Statistic parseStatistic(Iterable<DBObject> results) {
        Statistics statistics = parseStatistics(results);
        Iterator<Statistic> it = statistics.iterator();
        return it.hasNext() ? it.next() : null;
    }

    protected Statistics parseStatistics(Iterable<DBObject> results) {
        List<Statistic> l = Lists.newLinkedList();
        for (DBObject o : results) {
            l.add(parseStatistic(o));
        }
        return Statistics.from(l).build();
    }

    protected Statistic parseStatistic(DBObject result) {
        DBObject phenDbo = ((DBRef) result.get(ID_KEY)).fetch();
        Phenomenon phenomenon = (Phenomenon) mongoDB.getMapper()
                .fromDBObject(MongoPhenomenon.class, phenDbo,
                              mongoDB.getMapper().createEntityCache());
        long numberOfMeasurements = ((Number) result.get(MEASUREMENTS_KEY))
                .longValue();
        List<?> tracks = (List<?>) result.get(TRACKS_KEY);
        List<?> users = (List<?>) result.get(USERS_KEY);
        double max = ((Number) result.get(MAX_KEY)).doubleValue();
        double min = ((Number) result.get(MIN_KEY)).doubleValue();
        double mean = ((Number) result.get(AVG_KEY)).doubleValue();
        return new Statistic()
                .setPhenomenon(phenomenon)
                .setMeasurements(numberOfMeasurements)
                .setTracks(tracks.size())
                .setUsers(users.size())
                .setMax(max).setMin(min).setMean(mean);
    }

    protected DBObject group() {
        BasicDBObject fields = new BasicDBObject();
        String value = valueOf(path(MongoMeasurement.PHENOMENONS,
                                    MongoMeasurementValue.VALUE));
        fields.put(ID_KEY, valueOf(path(MongoMeasurement.PHENOMENONS,
                                        MongoMeasurementValue.PHENOMENON)));
        fields.put(AVG_KEY, new BasicDBObject(Ops.AVG, value));
        fields.put(MIN_KEY, new BasicDBObject(Ops.MIN, value));
        fields.put(MAX_KEY, new BasicDBObject(Ops.MAX, value));
        fields.put(MEASUREMENTS_KEY, new BasicDBObject(Ops.SUM, 1));
        fields.put(TRACKS_KEY, new BasicDBObject(Ops.ADD_TO_SET,
                                                 valueOf(MongoMeasurement.TRACK)));
        fields.put(USERS_KEY, new BasicDBObject(Ops.ADD_TO_SET,
                                                valueOf(MongoMeasurement.USER)));
        return new BasicDBObject(Ops.GROUP, fields);
    }

    protected DBObject unwind() {
        return new BasicDBObject(Ops.UNWIND, valueOf(MongoMeasurement.PHENOMENONS));
    }

    protected DBObject project() {
        BasicDBObject fields = new BasicDBObject();
        fields.put(MongoMeasurement.IDENTIFIER, 0);
        fields.put(MongoMeasurement.PHENOMENONS, 1);
        fields.put(MongoMeasurement.TRACK, 1);
        fields.put(MongoMeasurement.USER, 1);
        return new BasicDBObject(Ops.PROJECT, fields);
    }

    protected DBObject matchesUser(User user) {
        DBRef ref = mongoDB.getMapper().keyToRef(mongoDB.getMapper()
                .getKey((MongoUser) user));
        return new BasicDBObject(Ops.MATCH, new BasicDBObject(MongoMeasurement.USER, ref));
    }

    protected DBObject matchesTrack(String track) {
        return matchesTrack(new Key<MongoTrack>(MongoTrack.class, new ObjectId(track)));
    }

    protected DBObject matchesTrack(Track track) {
        return matchesTrack(mongoDB.getMapper().getKey((MongoTrack) track));
    }

    protected DBObject matchesTrack(Key<MongoTrack> track) {
        DBRef ref = mongoDB.getMapper().keyToRef(track);
        return new BasicDBObject(Ops.MATCH, new BasicDBObject(MongoMeasurement.TRACK, ref));
    }

    protected DBObject matchesPhenomenon(Phenomenon phenomenon) {
        Key<MongoPhenomenon> key = mongoDB.getMapper()
                .getKey(((MongoPhenomenon) phenomenon));
        DBRef ref = mongoDB.getMapper().keyToRef(key);
        return new BasicDBObject(Ops.MATCH,
                                 new BasicDBObject(path(MongoMeasurement.PHENOMENONS,
                                                        MongoMeasurementValue.PHENOMENON), ref));
    }

    protected DBObject matchesPhenomenon(Phenomenons phenomenons) {
        BasicDBList refs = new BasicDBList();
        for (Phenomenon phenomenon : phenomenons) {
            Key<MongoPhenomenon> key = mongoDB.getMapper()
                    .getKey(((MongoPhenomenon) phenomenon));
            refs.add(mongoDB.getMapper().keyToRef(key));
        }
        BasicDBObject in = new BasicDBObject(Ops.IN, refs);
        return new BasicDBObject(Ops.MATCH,
                                 new BasicDBObject(path(MongoMeasurement.PHENOMENONS,
                                                        MongoMeasurementValue.PHENOMENON), in));
    }

    protected <T extends MongoEntityBase> DBRef toRef(T o) {
        DBRef ref = mongoDB.getMapper().keyToRef(mongoDB.getMapper().getKey(o));
        return ref;
    }
}
