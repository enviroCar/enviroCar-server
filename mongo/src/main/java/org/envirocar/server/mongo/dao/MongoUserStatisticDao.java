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
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.core.util.pagination.PageBasedPagination;
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
    private final DataService dataService;

    @Inject
    public MongoUserStatisticDao(MongoDB mongoDB, DataService dataService) {
        super(MongoUserStatistic.class, mongoDB);
        this.dao = new BasicDAO<>(
                MongoUserStatistic.class, mongoDB.getDatastore());
        this.mongoDB = mongoDB;
        this.dataService = dataService;
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

        // get track measurements:
        Measurements values = dataService
                .getMeasurements(new MeasurementFilter(t));

        if (results.size() > 1) {
            // TODO: err... wtf?
            // error: more than 1 userstatistic available with identical user identifier
        } else if (results.size() == 1) {
            // previous userstatistic exists --> update them:
            MongoUserStatistic previous = (MongoUserStatistic) results.curr();
            MongoUserStatistic v = usu.removeTrackStatistic(previous, t, values);
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
            // TODO: err... wtf?
            // error: more than 1 userstatistic available with identical user identifier
        } else if (results.size() == 1) {
            // previous userstatistic exists --> update them:
            // get track measurements:
            Measurements values = dataService
                    .getMeasurements(new MeasurementFilter(t));
            MongoUserStatistic previous = (MongoUserStatistic) results.curr();
            MongoUserStatistic v = usu.addTrackStatistic(previous, t, values);
            v.setUser(previous.getUser());
            this.dao.save(v);
        } else {
            // no previous userstatistic exists --> calculate for all usertracks and insert new one:
            MongoUserStatistic previous = new MongoUserStatistic();
            
            previous.setDistance(0);
            previous.setDuration(0);
            previous.setDistanceAbove130kmh(0);
            previous.setDurationAbove130kmh(0);
            previous.setDistanceBelow60kmh(0);
            previous.setDurationBelow60kmh(0);
            previous.setDistanceNaN(0);
            previous.setDurationNaN(0);
            previous.setUser(t.getUser());
            previous.setTrackSummaries(new TrackSummaries());
            Tracks tracks = dataService.getTracks(
                    new TrackFilter(
                            t.getUser(),
                            new PageBasedPagination(10000,1)
                    )
            );
            Iterable<Track> userTracks = Tracks.from(tracks).build();
            for (Track track : userTracks)  {
                // get all measurments of this track:
                Measurements values = dataService
                    .getMeasurements(new MeasurementFilter(track));
                previous = usu.addTrackStatistic(previous, t, values);
            }

            // update mongoUS by this track:
            // get track measurements:
            Measurements values = dataService
                    .getMeasurements(new MeasurementFilter(t));
            previous = usu.addTrackStatistic(previous, t, values);

            // persistiere
            this.dao.save(previous);
        }
    }

}
