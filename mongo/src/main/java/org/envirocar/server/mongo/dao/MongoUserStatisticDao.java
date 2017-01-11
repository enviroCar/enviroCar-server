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

import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.envirocar.server.mongo.entity.MongoUserStatistic;

import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.envirocar.server.mongo.util.UserStatisticUtils;

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
        UserStatisticUtils usu = new UserStatisticUtils();
        
        // get current user's userstatistic:
        final Datastore ds = this.mongoDB.getDatastore();
        final DBCollection userstats = ds.getCollection(MongoUserStatistic.class);
        BasicDBObjectBuilder q = new BasicDBObjectBuilder();
        q.add(MongoUserStatistic.USER, ref(t.getUser()));
        DBCursor results = userstats.find(
                q.get()
        );

        if (results.size() > 1) {
            // TODO: catch this error:
            // error: more than 1 userstatistic available with identical user identifier
        } else if (results.size() == 1) {
            // previous userstatistic exists --> update them:
            MongoUserStatistic previous = (MongoUserStatistic) results.curr();
            MongoUserStatistic v = usu.removeTrackStatistic(previous, t);
            v.setUser(previous.getUser());
            this.dao.save(v);
        } else {
            // TODO: catch this error:
            // no previous userstatistic exists --> error/ do nothing:
        }
    }


    @Override
    public void updateStatisticsOnNewTrack(Track t) {
        UserStatisticUtils usu = new UserStatisticUtils();
            
        // get current user's userstatistic:
        final Datastore ds = this.mongoDB.getDatastore();
        final DBCollection userstats = ds.getCollection(MongoUserStatistic.class);
        BasicDBObjectBuilder q = new BasicDBObjectBuilder();
        q.add(MongoUserStatistic.USER, ref(t.getUser()));
        DBCursor results = userstats.find(
                q.get()
        );

        if (results.size() > 1) {
            // error: more than 1 userstatistic available with identical user identifier
        } else if (results.size() == 1) {
            // previous userstatistic exists --> update them:
            MongoUserStatistic previous = (MongoUserStatistic) results.curr();
            MongoUserStatistic v = usu.addTrackStatistic(previous, t);
            v.setUser(previous.getUser());
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

}
