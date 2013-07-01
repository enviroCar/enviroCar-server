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
package org.envirocar.server.mongo.dao;

import java.util.List;

import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

import org.envirocar.server.core.dao.StatisticsDao;

import org.envirocar.server.core.entities.Phenomenon;
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

import org.envirocar.server.mongo.util.MongoUtils;
import org.envirocar.server.mongo.util.Ops;

/**
 * TODO JavaDoc
 *
 * @author jan
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoStatisticsDao implements StatisticsDao {
    public static final String ID = Mapper.ID_KEY;
    public static final String PHENOMENON_NAME_PATH =
            MongoUtils.path(MongoMeasurement.PHENOMENONS,
                            MongoMeasurementValue.PHENOMENON,
                            MongoPhenomenon.NAME);
    public static final String PHENOMENONS_VALUE =
            MongoUtils.valueOf(MongoMeasurement.PHENOMENONS);
    public static final String PHENOMENONS_VALUE_VALUE =
            MongoUtils.valueOf(MongoMeasurement.PHENOMENONS,
                               MongoMeasurementValue.VALUE);
    public static final String PHENOMENON_NAME_VALUE =
            MongoUtils.valueOf(PHENOMENON_NAME_PATH);
    public static final String TRACK_VALUE =
            MongoUtils.valueOf(MongoMeasurement.TRACK);
    public static final String USER_VALUE =
            MongoUtils.valueOf(MongoMeasurement.USER);
    public static final String SENSOR_ID_PATH =
            MongoUtils.path(MongoMeasurement.SENSOR,
                            MongoSensor.NAME);
    public static final String SENSOR_ID_VALUE =
            MongoUtils.valueOf(SENSOR_ID_PATH);
    private static final String SENSOR_VALUE =
            MongoUtils.valueOf(MongoMeasurement.SENSOR);
    private final MongoDB mongoDB;
    private MongoPhenomenonDao phenomenonDao;
    private final BasicDAO<MongoStatistics, MongoStatisticKey> dao;

    @Inject
    public MongoStatisticsDao(MongoDB mongoDB) {
        this.mongoDB = mongoDB;
        this.dao = new BasicDAO<MongoStatistics, MongoStatisticKey>(
                MongoStatistics.class, mongoDB.getDatastore());
    }

    @Override
    public Statistics getStatistics(StatisticsFilter request) {
        return Statistics.from(getStatistics1(request).getStatistics()).build();
    }

    @Override
    public Statistic getStatistic(StatisticsFilter request,
                                  Phenomenon phenomenon) {
        return getStatistics1(request).getStatistic(phenomenon);
    }

    private MongoStatistics getStatistics1(StatisticsFilter request) {
        MongoStatisticKey key = key(request);
        MongoStatistics v = this.dao.get(key);
        if (v == null) {
            AggregationOutput aggregate = aggregate(matches(request),
                                                    project(),
                                                    unwind(),
                                                    group());
            List<MongoStatistic> statistics =
                    parseStatistics(aggregate.results());

            v = new MongoStatistics(key, statistics);
            this.dao.save(v);
        }
        return v;
    }

    private MongoStatisticKey key(StatisticsFilter request) {
        MongoTrack track = (MongoTrack) request.getTrack();
        MongoUser user = (MongoUser) request.getUser();
        MongoSensor sensor = (MongoSensor) request.getSensor();
        return new MongoStatisticKey(mongoDB.key(track),
                                     mongoDB.key(user),
                                     mongoDB.key(sensor));
    }

    private AggregationOutput aggregate(DBObject firstOp,
                                        DBObject... additionalOps) {
        AggregationOutput result = mongoDB.getDatastore()
                .getCollection(MongoMeasurement.class)
                .aggregate(firstOp, additionalOps);
        result.getCommandResult().throwOnError();
        return result;
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
        stat.setMeasurements(MongoUtils
                .asLong(result, MongoStatistic.MEASUREMENTS));
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
}
