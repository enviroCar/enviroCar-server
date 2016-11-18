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

import com.github.jmkgreen.morphia.DAO;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;

import org.envirocar.server.mongo.MongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.envirocar.server.mongo.entity.MongoStatisticKey;
import org.envirocar.server.mongo.entity.MongoStatistics;
import org.envirocar.server.mongo.entity.MongoUserStatistic;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class MongoUserStatisticDao extends AbstractBasicMongoDao<ObjectId, MongoUserStatistic>
        implements UserStatisticDao {

    private static final Logger log = LoggerFactory.getLogger(MongoUserStatisticDao.class);
    private final BasicDAO<MongoUserStatistic, ObjectId> dao;

    @Inject
    public MongoUserStatisticDao(MongoDB mongoDB) {
        super(MongoUserStatistic.class, mongoDB);
        this.dao = new BasicDAO<>(
                MongoUserStatistic.class, mongoDB.getDatastore());
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
    public void updateStatisticsOnTrackDeletion(Track e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateStatisticsOnNewTrack(Track e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
