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
package org.envirocar.server.rest.schema;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import org.envirocar.server.core.entities.Gender;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoUserStatistic;
import org.envirocar.server.mongo.dao.MongoUserStatisticDao;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;



/**
 *
 */
public class UserStatisticsTest {

    private MongoUser user;
    private MongoTrack track;
    private MongoMeasurement measurement1;
    private MongoMeasurement measurement2;
    private MongoMeasurement measurement3;
    
    
    @Before
    public void initialize() {
        user = new MongoUser();
        user.setGender(Gender.MALE);
        user.setName("max mustermann");
        
        track = new MongoTrack();
        DateTime begin = new DateTime(2016,7,23,8,53,5);
        track.setBegin(begin);
        DateTime end = new DateTime(2016,7,23,8,53,15);
        track.setEnd(end);
        track.setIdentifier("58122cd0e4b01fb1c099aa85");
        track.setLength(100);
        
        track.setUser(user);
        
        // Measurement 1:
        measurement1 = new MongoMeasurement();
        measurement1.setIdentifier("58122cd0e4b01fb1c099aa86");
        Geometry m1geo = new Geometry();
        m1geo.getFactory().
        GeometryFactory gf = new GeometryFactory();
        Geometry g1 = gf.createPoint(new Coordinate(7.656558, 51.936485));
        measurement1.setGeometry(g1);
        measurement1.setTime(begin);
        MongoMeasurementValue mv = new MongoMeasurementValue();
        mv.setValue(10);
        MongoPhenomenon mp = new MongoPhenomenon();
        mp.setName("Speed");
        mp.setUnit("km/h");
        mv.setPhenomenon(mp);
        measurement1.addValue(mv);
        measurement1.setTrack(track);
        
        // Measurement 2:
        measurement2 = new MongoMeasurement();
        measurement2.setIdentifier("58122cd0e4b01fb1c099aa87");
        Geometry m2geo = new Geometry();
        m2geo.getFactory().
        GeometryFactory gf2 = new GeometryFactory();
        Geometry g2 = gf2.createPoint(new Coordinate(7.653232, 51.938678));
        measurement2.setGeometry(g2);
        DateTime time = new DateTime(begin);
        time.plusSeconds(5)
        measurement2.setTime(time);
        MongoMeasurementValue mv2 = new MongoMeasurementValue();
        mv2.setValue(100);
        MongoPhenomenon mp2 = new MongoPhenomenon();
        mp2.setName("Speed");
        mp2.setUnit("km/h");
        mv2.setPhenomenon(mp2);
        measurement2.addValue(mv2);
        measurement2.setTrack(track);
        
        // Measurement 3:
        measurement3 = new MongoMeasurement();
        measurement3.setIdentifier("58122cd0e4b01fb1c099aa88");
        Geometry m3geo = new Geometry();
        m3geo.getFactory().
        GeometryFactory gf3 = new GeometryFactory();
        Geometry g3 = gf3.createPoint(new Coordinate(7.649863, 51.941178));
        measurement3.setGeometry(g3);
        DateTime time = new DateTime(begin);
        time.plusSeconds(10)
        measurement3.setTime(time);
        MongoMeasurementValue mv3 = new MongoMeasurementValue();
        mv3.setValue(133);
        MongoPhenomenon mp3 = new MongoPhenomenon();
        mp3.setName("Speed");
        mp3.setUnit("km/h");
        mv3.setPhenomenon(mp3);
        measurement3.addValue(mv3);
        measurement3.setTrack(track);
    }
    
    @Test
    public void test1() {
        MongoUserStatisticDao musd;
        UserStatisticFilter usf = new UserStatisticFilter(user);
        
        // test calculated userstatistics after onTrackCreation
        MongoUserStatistic mus = new MongoUserStatistic();
        mus.setUser(user);
        mus.setDistance(0);
        mus.setDistanceAbove130kmh(0);
        mus.setDistanceBelow60kmh(0);
        mus.setDistanceNaN(0);
        mus.setDuration(0);
        mus.setDurationAbove130kmh(0);
        mus.setDurationBelow60kmh(0);
        mus.setDurationNaN(0);
        TrackSummaries ts = new TrackSummaries();
        ts.setTrackSummaries(new ArrayList<TrackSummary>());
        mus.setTrackSummaries(ts);
        
        UserStatistic result = musd.updateStatisticsOnNewTrack(mus,track);
    }

}
