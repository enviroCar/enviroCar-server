/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import dev.morphia.Datastore;
import org.envirocar.server.core.dao.StatisticsDao;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.StatisticsFilter;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.envirocar.server.mongo.entity.MongoStatistic;
import org.envirocar.server.mongo.entity.MongoStatisticKey;
import org.envirocar.server.mongo.entity.MongoStatistics;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.statistics.StatisticsUpdateScheduler;
import org.envirocar.server.mongo.util.MongoUtils;
import org.envirocar.server.mongo.util.Ops;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * TODO JavaDoc
 *
 * @author jan
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoStatisticsDao implements StatisticsDao {

    public static final String ID = "_id";
    private static final String PHENOMENON_NAME_PATH = MongoUtils.path(MongoMeasurement.PHENOMENONS,
                                                                       MongoMeasurementValue.PHENOMENON,
                                                                       MongoPhenomenon.NAME);
    private static final String PHENOMENONS_VALUE = MongoUtils.valueOf(MongoMeasurement.PHENOMENONS);
    private static final String PHENOMENONS_VALUE_VALUE = MongoUtils.valueOf(MongoMeasurement.PHENOMENONS,
                                                                             MongoMeasurementValue.VALUE);
    private static final String PHENOMENON_NAME_VALUE = MongoUtils.valueOf(PHENOMENON_NAME_PATH);
    private static final String TRACK_VALUE = MongoUtils.valueOf(MongoMeasurement.TRACK);
    private static final String USER_VALUE = MongoUtils.valueOf(MongoMeasurement.USER);
    private static final String SENSOR_ID_PATH = MongoUtils.path(MongoMeasurement.SENSOR,
                                                                 MongoSensor.ID);
    private static final String SENSOR_ID_VALUE = MongoUtils.valueOf(SENSOR_ID_PATH);
    private static final String SENSOR_VALUE = MongoUtils.valueOf(MongoMeasurement.SENSOR);
    private final MongoDB mongoDB;
    private final Datastore datastore;
    private MongoPhenomenonDao phenomenonDao;
    private final StatisticsUpdateScheduler scheduler;

    private final Function<StatisticsFilter, MongoStatistics> calculateFunction
            = t -> calculateAndSaveStatistics(t, key(t));

    @Inject
    public MongoStatisticsDao(MongoDB mongoDB, StatisticsUpdateScheduler scheduler) {
        this.mongoDB = mongoDB;
        this.scheduler = scheduler;
        this.datastore = mongoDB.getDatastore();
    }

    @Override
    public Statistics getStatistics(StatisticsFilter request) {
        return new Statistics(getStatistics1(request).getStatistics());
    }

    @Override
    public Statistic getStatistic(StatisticsFilter request,
                                  Phenomenon phenomenon) {
        return getStatistics1(request).getStatistic(phenomenon);
    }

    private MongoStatistics getStatistics1(StatisticsFilter request) {
        MongoStatisticKey key = key(request);

        MongoStatistics v = getStatistics(key);

        if (v == null) {
            if (!request.hasSensor() && !request.hasTrack() && !request.hasUser()) {
                // overall stats
                this.scheduler.updateStatistics(request, key, calculateFunction, true);
                v = getStatistics(key);
            } else if (!request.hasSensor() && !request.hasTrack() && request.hasUser()) {
                // user stats
                this.scheduler.updateStatistics(request, key, calculateFunction, true);
                v = getStatistics(key);
            } else {
                v = calculateAndSaveStatistics(request, key);
            }
        }
        return v;
    }

    private MongoStatistics getStatistics(MongoStatisticKey key) {
        return datastore.createQuery(MongoStatistics.class).field(MongoStatistics.KEY).equal(key).first();
    }

    private MongoStatistics calculateAndSaveStatistics(StatisticsFilter request, MongoStatisticKey key) {
        Iterable<DBObject> aggregate = aggregate(matches(request), project(), unwind(), group());
        List<MongoStatistic> statistics = parseStatistics(aggregate);
        MongoStatistics v = new MongoStatistics(key, statistics);
        datastore.save(v);
        return v;
    }

    private MongoStatisticKey key(StatisticsFilter request) {
        MongoTrack track = (MongoTrack) request.getTrack();
        MongoUser user = (MongoUser) request.getUser();
        MongoSensor sensor = (MongoSensor) request.getSensor();
        return new MongoStatisticKey(track, user, sensor);
    }

    private Iterable<DBObject> aggregate(DBObject... ops) {
        return aggregate(Arrays.asList(ops));
    }

    private Iterable<DBObject> aggregate(List<DBObject> ops) {
        DBCollection collection = datastore.getCollection(MongoMeasurement.class);
        try (Cursor cursor = collection.aggregate(ops, AggregationOptions.builder().build())) {
            LinkedList<DBObject> list = new LinkedList<>();
            cursor.forEachRemaining(list::add);
            return list;
        }
    }

    private List<MongoStatistic> parseStatistics(Iterable<DBObject> results) {
        List<MongoStatistic> l = Lists.newLinkedList();
        for (DBObject o : results) {
            l.add(parseStatistic(o));
        }
        return l;
    }

    private MongoStatistic parseStatistic(DBObject result) {
        String phenomenonName = (String) result.get(ID);
        Phenomenon phenomenon = this.phenomenonDao.get(phenomenonName);
        MongoStatistic stat = new MongoStatistic();
        stat.setPhenomenon(phenomenon);
        stat.setMeasurements(MongoUtils.asLong(result, MongoStatistic.MEASUREMENTS));
        stat.setTracks(MongoUtils.length(result, MongoStatistic.TRACKS));
        stat.setUsers(MongoUtils.length(result, MongoStatistic.USERS));
        stat.setSensors(MongoUtils.length(result, MongoStatistic.SENSORS));
        stat.setMax(MongoUtils.asDouble(result, MongoStatistic.MAX));
        stat.setMin(MongoUtils.asDouble(result, MongoStatistic.MIN));
        stat.setMean(MongoUtils.asDouble(result, MongoStatistic.MEAN));
        return stat;
    }

    private DBObject matches(StatisticsFilter request) {
        BasicDBObjectBuilder b = new BasicDBObjectBuilder();
        BasicDBObjectBuilder match = b.push(Ops.MATCH);
        if (request.hasTrack()) {
            DBRef track = mongoDB.ref(request.getTrack());
            match.add(MongoMeasurement.TRACK, track);
        }
        if (request.hasUser()) {
            DBRef user = mongoDB.ref(request.getUser());
            match.add(MongoMeasurement.USER, user);
        }
        if (request.hasSensor()) {
            MongoSensor sensor = (MongoSensor) request.getSensor();
            match.add(SENSOR_ID_PATH, sensor.getId());
        }
        return b.get();
    }

    private DBObject group() {
        BasicDBObject fields = new BasicDBObject();
        fields.put(ID, PHENOMENON_NAME_VALUE);
        fields.put(MongoStatistic.MEAN, MongoUtils.avg(PHENOMENONS_VALUE_VALUE));
        fields.put(MongoStatistic.MIN, MongoUtils.min(PHENOMENONS_VALUE_VALUE));
        fields.put(MongoStatistic.MAX, MongoUtils.max(PHENOMENONS_VALUE_VALUE));
        fields.put(MongoStatistic.MEASUREMENTS, MongoUtils.count());
        fields.put(MongoStatistic.TRACKS, MongoUtils.addToSet(TRACK_VALUE));
        fields.put(MongoStatistic.USERS, MongoUtils.addToSet(USER_VALUE));
        fields.put(MongoStatistic.SENSORS, MongoUtils.addToSet(SENSOR_VALUE));
        return MongoUtils.group(fields);
    }

    private DBObject unwind() {
        return MongoUtils.unwind(PHENOMENONS_VALUE);
    }

    private DBObject project() {
        BasicDBObject fields = new BasicDBObject();
        fields.put(MongoMeasurement.IDENTIFIER, 0);
        fields.put(MongoMeasurement.PHENOMENONS, 1);
        fields.put(MongoMeasurement.TRACK, 1);
        fields.put(MongoMeasurement.USER, 1);
        fields.put(MongoMeasurement.SENSOR, SENSOR_ID_VALUE);
        return MongoUtils.project(fields);
    }

    @Inject
    public void setPhenomenonDao(MongoPhenomenonDao phenomenonDao) {
        this.phenomenonDao = phenomenonDao;
    }

    @Override
    public void updateStatisticsOnNewTrack(Track t) {
        StatisticsFilter allFilter = new StatisticsFilter();
        MongoStatisticKey allKey = key(allFilter);
        this.scheduler.updateStatistics(allFilter, allKey, calculateFunction, false);

        StatisticsFilter userFilter = new StatisticsFilter(t.getUser());
        MongoStatisticKey userKey = key(userFilter);
        this.scheduler.updateStatistics(userFilter, userKey, calculateFunction, false);
    }

}
