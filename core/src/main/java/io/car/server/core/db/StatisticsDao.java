/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.core.db;

import io.car.server.core.Statistic;
import io.car.server.core.Statistics;
import io.car.server.core.Track;
import io.car.server.core.User;

/**
 *
 * @author jan
 */
public interface StatisticsDao {
    
    long getNumberOfTracks();
    
    long getNumberOfMeasurements();
    
    long getNumberOfMeasurements(User user);
    
    long getNumberOfMeasurements(Track track);
    
    Statistics getStatistics(Track track);
    
    Statistics getStatistics(User user);
    
    Statistics getStatistics();
    
    Statistic getStatistics(Track track, String phenomenon);
    
    Statistic getStatistics(User user, String phenomenon);
    
    Statistic getStatistics(String phenomenon);
    
    Statistic getStatistics(Track track, Iterable<String> phenomenon);
    
    Statistic getStatistics(User user, Iterable<String> phenomenon);
    
    Statistic getStatistics(Iterable<String> phenomenon);
}
