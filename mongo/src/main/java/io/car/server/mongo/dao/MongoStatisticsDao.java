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
package io.car.server.mongo.dao;

import java.util.List;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.AggregationOutput;
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
import io.car.server.mongo.entity.MongoStatistic;
import io.car.server.mongo.entity.MongoStatisticKey;
import io.car.server.mongo.entity.MongoStatistics;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;
import io.car.server.mongo.util.MongoUtils;

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
    public Statistics getStatisticsForTrack(Track track) {
        return Statistics.from(getStatistics1(track).getStatistics()).build();
    }

    @Override
    public Statistics getStatisticsForUser(User user) {
        return Statistics.from(getStatistics1(user).getStatistics()).build();
    }

    @Override
    public Statistics getStatistics() {
        return Statistics.from(getStatistics1().getStatistics()).build();
    }

    @Override
    public Statistic getStatisticsForTrack(Track track, Phenomenon phenomenon) {
        return getStatistics1(track).getStatistic(phenomenon);
    }

    @Override
    public Statistic getStatisticsForUser(User user, Phenomenon phenomenon) {
        return getStatistics1(user).getStatistic(phenomenon);
    }

    @Override
    public Statistic getStatistics(Phenomenon phenomenon) {
        return getStatistics1().getStatistic(phenomenon);
    }

    @Override
    public Statistics getStatisticsForTrack(Track track, Phenomenons phens) {
        return Statistics.from(getStatistics1(track).getStatistics(phens))
                .build();
    }

    @Override
    public Statistics getStatisticsForUser(User user, Phenomenons phens) {
        return Statistics.from(getStatistics1(user).getStatistics(phens))
                .build();
    }

    @Override
    public Statistics getStatistics(Phenomenons phens) {
        return Statistics.from(getStatistics1().getStatistics(phens)).build();
    }

    private MongoStatisticKey key(Track track, User user) {
        return new MongoStatisticKey(mongoDB.key((MongoTrack) track),
                                     mongoDB.key((MongoUser) user));
    }

    private MongoStatistics getStatistics1(Track track) {
        MongoStatisticKey key = key(track, null);
        MongoStatistics value = dao.get(key);
        if (value == null) {
            List<MongoStatistic> statistics = parseStatistics(
                    aggregate(matchesTrack(track), project(), unwind(), group())
                    .results());

            value = new MongoStatistics(key, statistics);
            dao.save(value);
        }
        return value;
    }

    private MongoStatistics getStatistics1(User user) {
        MongoStatisticKey key = key(null, user);
        MongoStatistics value = dao.get(key);
        if (value == null) {
            List<MongoStatistic> statistics =
                    parseStatistics(aggregate(matchesUser(user),
                                              project(),
                                              unwind(),
                                              group()).results());

            value = new MongoStatistics(key, statistics);
            dao.save(value);
        }
        return value;
    }

    private MongoStatistics getStatistics1() {
        MongoStatisticKey key = key(null, null);
        MongoStatistics value = dao.get(key);
        if (value == null) {
            List<MongoStatistic> statistics =
                    parseStatistics(aggregate(project(),
                                              unwind(),
                                              group()).results());

            value = new MongoStatistics(key, statistics);
            dao.save(value);
        }
        return value;
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
        Phenomenon phenomenon = this.phenomenonDao.get(phenomenonName);;
        MongoStatistic stat = new MongoStatistic();
        stat.setPhenomenon(phenomenon);
        stat.setMeasurements(MongoUtils
                .asLong(result, MongoStatistic.MEASUREMENTS));
        stat.setTracks(MongoUtils.length(result, MongoStatistic.TRACKS));
        stat.setUsers(MongoUtils.length(result, MongoStatistic.USERS));
        stat.setMax(MongoUtils.asDouble(result, MongoStatistic.MAX));
        stat.setMin(MongoUtils.asDouble(result, MongoStatistic.MIN));
        stat.setMean(MongoUtils.asDouble(result, MongoStatistic.MEAN));
        return stat;
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
        return MongoUtils.project(fields);
    }

    private DBObject matchesUser(User user) {
        DBRef ref = mongoDB.getMapper().keyToRef(mongoDB.getMapper()
                .getKey((MongoUser) user));
        return MongoUtils.match(MongoMeasurement.USER, ref);
    }

    private DBObject matchesTrack(Track track) {
        return matchesTrack(mongoDB.getMapper().getKey((MongoTrack) track));
    }

    private DBObject matchesTrack(Key<MongoTrack> track) {
        return MongoUtils.match(MongoMeasurement.TRACK,
                                mongoDB.getMapper().keyToRef(track));
    }

    private <T extends MongoEntityBase> DBRef toRef(T o) {
        return mongoDB.getMapper().keyToRef(mongoDB.getMapper().getKey(o));
    }

    @Inject
    public void setPhenomenonDao(MongoPhenomenonDao phenomenonDao) {
        this.phenomenonDao = phenomenonDao;
    }
}
