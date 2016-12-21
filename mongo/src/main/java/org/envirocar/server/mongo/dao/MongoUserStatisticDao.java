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

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;

import org.envirocar.server.mongo.MongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.entities.UserStatisticImpl;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.envirocar.server.core.util.GeodesicGeometryOperations;
import org.envirocar.server.mongo.entity.MongoUserStatistic;

import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.MeasurementValues;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.envirocar.server.core.entities.TrackSummaries;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class MongoUserStatisticDao extends AbstractBasicMongoDao<ObjectId, MongoUserStatistic>
        implements UserStatisticDao {

    private static final Logger log = LoggerFactory.getLogger(MongoUserStatisticDao.class);
    private final BasicDAO<MongoUserStatistic, ObjectId> dao;
    private final MongoDB mongoDB;

    @Inject
    public MongoUserStatisticDao(MongoDB mongoDB) {
        super(MongoUserStatistic.class, mongoDB);
        this.dao = new BasicDAO<>(
                MongoUserStatistic.class, mongoDB.getDatastore());
        this.mongoDB = mongoDB;
    }

    @Override
    public UserStatistic getById(String identifier) {
        ObjectId oid;
        try {
            oid = new ObjectId(identifier);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return get(oid);
    }

    @Override
    public UserStatistic create(UserStatistic userStatistic) {
        MongoUserStatistic mus = (MongoUserStatistic) userStatistic;
        save(mus);
        return mus;
    }

    @Override
    public UserStatistic get(UserStatisticFilter request) {
        UserStatistic result;
        Query<MongoUserStatistic> q = q();
        if (request.hasUser()) {
            Query<MongoUserStatistic> query = q.field(MongoUserStatistic.USER).equal(key(request.getUser()));
            result = this.dao.findOne(query);
        } else {
            return null;
        }
        return result;
    }

    @Override
    public void updateStatisticsOnTrackDeletion(Track t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // this.dao.save(entity)
    }

    @Override
    public void updateStatisticsOnNewTrack(Track t) {
        GeodesicGeometryOperations ggo = new GeodesicGeometryOperations();

        // berechne statistiken vom track:
        UserStatisticImpl us = new UserStatisticImpl();
        TrackSummary ts = null;

        us.setUser(t.getUser());

        if (t instanceof Iterable) {
            Iterable<Measurement> measurementIterator = (Iterable<Measurement>) t;
            List<Measurement> list = new ArrayList();
            for (Measurement m : measurementIterator) {
                list.add(m);
            }

            // calculate userstatistic for this track:
            double total_dist = 0;
            double total_dura = 0;
            double total_dist60 = 0;
            double total_dura60 = 0;
            double total_dist130 = 0;
            double total_dura130 = 0;
            double total_distNaN = 0;
            double total_duraNaN = 0;
            for (int i = 0; i < list.size(); i++) {
                Measurement m_this = list.get(i);
                Measurement m_next = list.get(i + 1);

                // berechne dist:
                double dist = ggo.calculateDistance(
                        m_this,
                        m_next);

                // berechne dura:
                DateTime t_start = m_this.getCreationTime();
                DateTime t_end = m_next.getCreationTime();
                double dura_millis
                        = t_end.getMillis()
                        - t_start.getMillis();

                // b. fülle entsprechend ein in interval <60,>130,NaN:
                Double speed = null;
                MeasurementValues values = m_this.getValues();
                for (MeasurementValue value : values) {
                    if (value.getPhenomenon().getName().equals("Speed")) {
                        speed = (double) value.getValue();
                        break;
                    }
                }
                if (speed == null) {
                    total_distNaN += dist;
                    total_duraNaN += dura_millis;
                } else if (speed > 130) {
                    total_dist130 += dist;
                    total_dura130 += dura_millis;
                } else if (speed < 60) {
                    total_dist60 += dist;
                    total_dura60 += dura_millis;
                }
                total_dist += dist;
                total_dura += dura_millis;
            }
            // umrechnen von millis nach stunden:
            total_dura /= (60 * 60 * 1000);
            total_dura60 /= (60 * 60 * 1000);
            total_dura130 /= (60 * 60 * 1000);
            total_duraNaN /= (60 * 60 * 1000);

            // create TrackSummary object:
            // get first and last measurements geometries
            Measurement valuesStart = list.get(0);
            Measurement valuesEnd = list.get(list.size());
            ts = new TrackSummary();
            ts.setStartPosition(valuesStart.getGeometry());
            ts.setEndPosition(valuesEnd.getGeometry());
            ts.setIdentifier(t.getIdentifier());

            // d. packe in userstatistic objekt:
            us.setDistance(total_dist);
            us.setDistanceBelow60kmh(total_dist60);
            us.setDistanceAbove130kmh(total_dist130);
            us.setDistanceNaN(total_distNaN);
            us.setDuration(total_dura);
            us.setDurationBelow60kmh(total_dura60);
            us.setDurationAbove130kmh(total_dura130);
            us.setDurationNaN(total_duraNaN);

        } else {
            // track has no measurements. put empty zeros:
            us.setDistance(0);
            us.setDistanceBelow60kmh(0);
            us.setDistanceAbove130kmh(0);
            us.setDistanceNaN(0);
            us.setDuration(0);
            us.setDurationBelow60kmh(0);
            us.setDurationAbove130kmh(0);
            us.setDurationNaN(0);
        }

        // save mongouserstatistic:
        addToUserStatistics(us, ts);
    }

    private void addToUserStatistics(UserStatisticImpl userStatistic, TrackSummary trackSummary) {
        // get current user's userstatistic:
        final Datastore ds = this.mongoDB.getDatastore();
        final DBCollection userstats = ds.getCollection(MongoUserStatistic.class);
        BasicDBObjectBuilder q = new BasicDBObjectBuilder();
        q.add(MongoUserStatistic.USER, ref(userStatistic.getUser()));
        DBCursor results = userstats.find(
                q.get()
        );
        TrackSummaries ts;
        MongoUserStatistic v;

        if (results.size() > 1) {
            // error: more than 1 userstatistic available with identical user identifier
        } else if (results.size() == 1) {
            // previous userstatistic exists --> update them:
            MongoUserStatistic previous = (MongoUserStatistic) results.curr();
            v = new MongoUserStatistic();

            v.setUser(userStatistic.getUser());
            v.setDistance(
                    previous.getDistance() + userStatistic.getDistance()
            );
            v.setDistanceAbove130kmh(
                    previous.getDistanceAbove130kmh() + userStatistic.getDistanceAbove130kmh()
            );
            v.setDistanceBelow60kmh(
                    previous.getDistanceBelow60kmh() + userStatistic.getDistanceBelow60kmh()
            );
            v.setDistanceNaN(
                    previous.getDistanceNaN() + userStatistic.getDistanceNaN()
            );
            v.setDuration(
                    previous.getDuration() + userStatistic.getDuration()
            );
            v.setDurationAbove130kmh(
                    previous.getDurationAbove130kmh() + userStatistic.getDurationAbove130kmh()
            );
            v.setDurationBelow60kmh(
                    previous.getDurationBelow60kmh() + userStatistic.getDurationBelow60kmh()
            );
            v.setDurationNaN(
                    previous.getDurationNaN() + userStatistic.getDurationNaN()
            );
            if (trackSummary != null) {
                ts = previous.getTrackSummaries();
                ts.addTrackSummary(trackSummary);
                v.setTrackSummaries(ts);
            } else {
                ts = new TrackSummaries();
                ArrayList<TrackSummary> tList = new ArrayList();
                ts.setTrackSummaries(tList);
                v.setTrackSummaries(ts);
            }

            this.dao.save(v);
        } else {
            // no previous userstatistic exists --> insert new one:
            // TODO: "dann mache erstmal garnichts (Arne, 19.12.2016)"   
            // - Das Userstatistic retroffing script hätte für jeden user eine userstatistic nachrechnen müssen!

            /**
             * v = new MongoUserStatistic();
             *
             * v.setUser(userStatistic.getUser());
             * v.setDistance(userStatistic.getDistance());
             * v.setDistanceAbove130kmh(userStatistic.getDistanceAbove130kmh());
             * v.setDistanceBelow60kmh(userStatistic.getDistanceBelow60kmh());
             * v.setDistanceNaN(userStatistic.getDistanceNaN());
             * v.setDuration(userStatistic.getDuration());
             * v.setDurationAbove130kmh(userStatistic.getDurationAbove130kmh());
             * v.setDurationBelow60kmh(userStatistic.getDurationBelow60kmh());
             * v.setDurationNaN(userStatistic.getDurationNaN());
             *
             * ts = new TrackSummaries(); ts.addTrackSummary(trackSummary);
             * v.setTrackSummaries(ts);
             *
             * this.dao.save(v);
             */
        }
    }

    private void removeFromUserStatistics(UserStatisticImpl userStatistic) {

    }
}
