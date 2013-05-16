/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.mongo.dao;

import com.github.jmkgreen.morphia.Datastore;
import com.google.inject.Inject;
import com.mongodb.DBCollection;

import io.car.server.core.Statistic;
import io.car.server.core.Statistics;
import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.TrackDao;
import io.car.server.core.dao.UserDao;
import io.car.server.core.db.StatisticsDao;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getNumberOfMeasurements(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getNumberOfMeasurements(Track track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistics getStatistics(Track track) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistics getStatistics(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistics getStatistics() {
        final DBCollection col = this.measurementDao.getCollection();
//        MongoMeasurement.PHENOMENONS
//        col.group(null, null, null, null)
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistic getStatistics(Track track, String phenomenon) {
//        db.mapReduce(MapreduceType.MERGE, null, null, null)
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistic getStatistics(User user, String phenomenon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistic getStatistics(String phenomenon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistic getStatistics(Track track, Iterable<String> phenomenon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistic getStatistics(User user, Iterable<String> phenomenon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statistic getStatistics(Iterable<String> phenomenon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
