/**
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

import com.github.jmkgreen.morphia.Datastore;
import com.google.inject.Inject;

import io.car.server.core.Statistic;
import io.car.server.core.Statistics;
import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.TrackDao;
import io.car.server.core.dao.UserDao;
import io.car.server.core.db.StatisticsDao;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.mongo.entity.MongoMeasurement;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;

/**
 *
 * @author jan
 */
public class MongoStatisticsDao implements StatisticsDao{

    private final MongoUserDao userDao;
    private final MongoMeasurementDao measurementDao;
    private final MongoTrackDao trackDao;
    private final Datastore db;

    @Inject
    public MongoStatisticsDao(Datastore db, UserDao userDao, MeasurementDao measurementDao, TrackDao trackDao) {
        this.userDao = (MongoUserDao) userDao;
        this.measurementDao = (MongoMeasurementDao) measurementDao;
        this.trackDao = (MongoTrackDao) trackDao;
        this.db = db;
    }
    
    @Override
    public long getNumberOfTracks() {
        return this.trackDao.count();
    }

    @Override
    public long getNumberOfMeasurements() {
        return this.measurementDao.count();
    }

    @Override
    public long getNumberOfMeasurements(User user) {
        return this.measurementDao.count(this.measurementDao.createQuery()
                .field(MongoMeasurement.USER).equal((MongoUser) user));
    }

    @Override
    public long getNumberOfMeasurements(Track track) {
        return this.measurementDao.count(this.measurementDao.createQuery()
                .field(MongoMeasurement.TRACK).equal((MongoTrack) track));
    }

    @Override
    public Statistics getStatistics(Track track) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistics getStatistics(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistics getStatistics() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistic getStatistics(Track track, String phenomenon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistic getStatistics(User user, String phenomenon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistic getStatistics(String phenomenon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistic getStatistics(Track track, Phenomenons phenomenon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistic getStatistics(User user, Phenomenons phenomenon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistic getStatistics(Phenomenons phenomenon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
