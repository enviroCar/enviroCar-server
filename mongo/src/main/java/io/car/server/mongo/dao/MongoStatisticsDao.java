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

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

import io.car.server.core.Statistic;
import io.car.server.core.Statistics;
import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.TrackDao;
import io.car.server.core.db.StatisticsDao;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.mongo.entity.MongoBaseEntity;
import io.car.server.mongo.entity.MongoMeasurement;
import io.car.server.mongo.entity.MongoMeasurementValue;
import io.car.server.mongo.entity.MongoPhenomenon;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;
import io.car.server.mongo.util.Ops;

/**
 * @author jan
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoStatisticsDao implements StatisticsDao {
    public static final String ID_KEY = Mapper.ID_KEY;
    public static final String AVG_KEY = "avg";
    public static final String MIN_KEY = "min";
    public static final String MAX_KEY = "max";
    public static final String COUNT_KEY = "count";

    protected static String valueOf(String property) {
        return "$" + property;
    }

    protected static String path(String first, String second, String... paths) {
        return Joiner.on(".").join(first, second, (Object[]) paths);
    }
    private final MongoMeasurementDao measurements;
    private final MongoTrackDao tracks;
    private final Datastore db;
    private final Mapper mapr;

    @Inject
    public MongoStatisticsDao(Mapper mapr,
                              Datastore db,
                              MeasurementDao measurementDao,
                              TrackDao trackDao) {
        this.measurements = (MongoMeasurementDao) measurementDao;
        this.tracks = (MongoTrackDao) trackDao;
        this.db = db;
        this.mapr = mapr;
    }

    @Override
    public long getNumberOfTracks() {
        return tracks.count();
    }

    @Override
    public long getNumberOfMeasurements() {
        return measurements.count();
    }

    @Override
    public long getNumberOfMeasurements(User user) {
        return measurements.count(measurements.createQuery()
                .field(MongoMeasurement.USER).equal((MongoUser) user));
    }

    @Override
    public long getNumberOfMeasurements(Track track) {
        return measurements.count(measurements.createQuery()
                .field(MongoMeasurement.TRACK).equal((MongoTrack) track));
    }

    @Override
    public Statistics getStatistics(Track track) {
        return parseStatistics(aggregate(matches(track), project(), unwind(), group())
                .results());
    }

    @Override
    public Statistics getStatistics(User user) {
        return parseStatistics(aggregate(matches(user), project(), unwind(), group())
                .results());
    }

    @Override
    public Statistics getStatistics() {
        return parseStatistics(aggregate(project(), unwind(), group()).results());
    }

    @Override
    public Statistic getStatistics(Track track, Phenomenon phenomenon) {
        return parseStatistic(aggregate(matches(track), project(), unwind(), matches(phenomenon), group())
                .results());
    }

    @Override
    public Statistic getStatistics(User user, Phenomenon phenomenon) {
        return parseStatistic(aggregate(matches(user), project(), unwind(), matches(phenomenon), group())
                .results());
    }

    @Override
    public Statistic getStatistics(Phenomenon phenomenon) {
        return parseStatistic(aggregate(project(), unwind(), matches(phenomenon), group())
                .results());
    }

    @Override
    public Statistics getStatistics(Track track, Phenomenons phenomenons) {
        return parseStatistics(aggregate(matches(track), project(), unwind(), matches(phenomenons), group())
                .results());
    }

    @Override
    public Statistics getStatistics(User user, Phenomenons phenomenons) {
        return parseStatistics(aggregate(matches(user), project(), unwind(), matches(phenomenons), group())
                .results());
    }

    @Override
    public Statistics getStatistics(Phenomenons phenomenons) {
        return parseStatistics(aggregate(project(), unwind(), matches(phenomenons), group())
                .results());
    }

    protected AggregationOutput aggregate(DBObject firstOp,
                                          DBObject... additionalOps) {
        AggregationOutput result = measurements.getCollection()
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
        return new Statistics(l);
    }

    protected Statistic parseStatistic(DBObject result) {
        DBObject phenDbo = ((DBRef) result.get(ID_KEY)).fetch();
        Phenomenon phenomenon = (Phenomenon) mapr
                .fromDBObject(MongoPhenomenon.class, phenDbo, mapr
                .createEntityCache());
        long count = ((Number) result.get(COUNT_KEY)).longValue();
        double max = ((Number) result.get(MAX_KEY)).doubleValue();
        double min = ((Number) result.get(MIN_KEY)).doubleValue();
        double mean = ((Number) result.get(AVG_KEY)).doubleValue();
        return new Statistic().setPhenomenon(phenomenon).setMeasurements(count)
                .setMax(max).setMin(min).setMean(mean);
    }

    protected DBObject group() {
        BasicDBObject fields = new BasicDBObject();
        String value =
                valueOf(path(MongoMeasurement.PHENOMENONS, MongoMeasurementValue.VALUE));
        fields
                .put(ID_KEY, valueOf(path(MongoMeasurement.PHENOMENONS, MongoMeasurementValue.PHENOMENON)));
        fields.put(AVG_KEY, new BasicDBObject(Ops.AVG, value));
        fields.put(MIN_KEY, new BasicDBObject(Ops.MIN, value));
        fields.put(MAX_KEY, new BasicDBObject(Ops.MAX, value));
        fields.put(COUNT_KEY, new BasicDBObject(Ops.SUM, 1));
        return new BasicDBObject(Ops.GROUP, fields);
    }

    protected DBObject unwind() {
        return new BasicDBObject(Ops.UNWIND, valueOf(MongoMeasurement.PHENOMENONS));
    }

    protected DBObject project() {
        BasicDBObject fields = new BasicDBObject();
        fields.put(MongoMeasurement.ID, 0);
        fields.put(MongoMeasurement.PHENOMENONS, 1);
        return new BasicDBObject(Ops.PROJECT, fields);
    }

    protected DBObject matches(User user) {
        DBRef ref = toRef((MongoUser) user);
        return new BasicDBObject(Ops.MATCHES, new BasicDBObject(MongoMeasurement.USER, ref));
    }

    protected DBObject matches(Track track) {
        DBRef ref = toRef((MongoTrack) track);
        return new BasicDBObject(Ops.MATCHES, new BasicDBObject(MongoMeasurement.TRACK, ref));
    }

    protected DBObject matches(Phenomenon phenomenon) {
        DBRef ref = toRef((MongoPhenomenon) phenomenon);
        return new BasicDBObject(Ops.MATCHES, new BasicDBObject(path(MongoMeasurement.PHENOMENONS, MongoMeasurementValue.PHENOMENON), ref));
    }

    protected DBObject matches(Phenomenons phenomenons) {
        BasicDBList refs = new BasicDBList();
        for (Phenomenon phenomenon : phenomenons) {
            refs.add(toRef((MongoPhenomenon) phenomenon));
        }
        BasicDBObject in = new BasicDBObject(Ops.IN, refs);
        return new BasicDBObject(Ops.MATCHES, new BasicDBObject(path(MongoMeasurement.PHENOMENONS, MongoMeasurementValue.PHENOMENON), in));
    }

    protected <T extends MongoBaseEntity<T>> DBRef toRef(T o) {
        DBRef ref = mapr.keyToRef(db.getKey(o));
        return ref;
    }
}
