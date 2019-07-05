/*
 * Copyright (C) 2013-2018 The enviroCar project
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
import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import java.util.List;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoUserStatistic;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.mongo.util.UserStatisticUtils;
import org.envirocar.server.rest.GuiceRunner;
import org.envirocar.server.rest.TrackWithMeasurments;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Maurin Radtke (maurin.radtke@uni-muenster.de)
 */
@RunWith(GuiceRunner.class)
public class UserStatisticsTest {

    Track track;
    UserStatistic userStatistic;
    TrackWithMeasurments twm;
    final double TOLERANCE_KM = 0.01;        // 1 meter
    final double TOLERANCE_HOUR = 0.000276;  // 1 second

    @Before
    public void initialize() {

        track = new MongoTrack();
        DateTime begin = new DateTime(2016, 7, 23, 8, 53, 5);
        track.setBegin(begin);
        DateTime end = new DateTime(2016, 7, 23, 8, 53, 15);
        track.setEnd(end);
        track.setIdentifier("58122cd0e4b01fb1c099aa85");
        track.setLength(100);

        twm = new TrackWithMeasurments(track);

        // Measurement 1:
        MongoMeasurement measurement1 = new MongoMeasurement();
        measurement1.setIdentifier("58122cd0e4b01fb1c099aa86");
        GeometryFactory gf = new GeometryFactory();
        Geometry g1 = gf.createPoint(new Coordinate(7.656558, 51.936485));
        measurement1.setGeometry(g1);
        measurement1.setTime(begin);
        MeasurementValue mv = new MongoMeasurementValue();
        mv.setValue(10.0);
        MongoPhenomenon mp = new MongoPhenomenon();
        mp.setName("Speed");
        mp.setUnit("km/h");
        mv.setPhenomenon(mp);
        measurement1.addValue(mv);

        // Measurement 2:
        MongoMeasurement measurement2 = new MongoMeasurement();
        measurement2.setIdentifier("58122cd0e4b01fb1c099aa87");
        GeometryFactory gf2 = new GeometryFactory();
        Geometry g2 = gf2.createPoint(new Coordinate(7.653232, 51.938678));
        measurement2.setGeometry(g2);
        DateTime time2 = new DateTime(2016, 7, 23, 8, 53, 10);
        measurement2.setTime(time2);
        MeasurementValue mv2 = new MongoMeasurementValue();
        mv2.setValue(100.0);
        MongoPhenomenon mp2 = new MongoPhenomenon();
        mp2.setName("Speed");
        mp2.setUnit("km/h");
        mv2.setPhenomenon(mp2);
        measurement2.addValue(mv2);

        // Measurement 3:
        MongoMeasurement measurement3 = new MongoMeasurement();
        measurement3.setIdentifier("58122cd0e4b01fb1c099aa88");
        GeometryFactory gf3 = new GeometryFactory();
        Geometry g3 = gf3.createPoint(new Coordinate(7.649863, 51.941178));
        measurement3.setGeometry(g3);
        DateTime time3 = new DateTime(2016, 7, 23, 8, 53, 15);
        measurement3.setTime(time3);
        MeasurementValue mv3 = new MongoMeasurementValue();
        mv3.setValue(133.0);
        MongoPhenomenon mp3 = new MongoPhenomenon();
        mp3.setName("Speed");
        mp3.setUnit("km/h");
        mv3.setPhenomenon(mp3);
        measurement3.addValue(mv3);

        twm.addMeasurement(measurement1);
        twm.addMeasurement(measurement2);
        twm.addMeasurement(measurement3);
    }

    @Test
    public void testAddTrackOnEmptyUserStats() {
        // test calculated userstatistics after onTrackCreation
        MongoUserStatistic mus = new MongoUserStatistic();
        mus.setDistance(0);
        mus.setDistanceAbove130kmh(0);
        mus.setDistanceBelow60kmh(0);
        mus.setDistanceNaN(0);
        mus.setDuration(0);
        mus.setDurationAbove130kmh(0);
        mus.setDurationBelow60kmh(0);
        mus.setDurationNaN(0);
        TrackSummaries ts = new TrackSummaries();
        ts.setTrackSummariesList(new ArrayList<>());
        mus.setTrackSummaries(ts);

        List<Measurement> list = twm.getMeasurements();
        Measurements measures = Measurements.from(list).build();

        MongoUserStatistic result = new UserStatisticUtils().addTrackStatistic(mus, twm, measures);

        // test results:
        assertThat(result.getDistance() - (0.3338 + 0.3614) < TOLERANCE_KM, is(true));
        assertThat(result.getDuration() - (2 * 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceBelow60kmh() - 0.3338 < TOLERANCE_KM, is(true));
        assertThat(result.getDurationBelow60kmh() - 0.00138 < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceAbove130kmh() > 0, is(false));
        assertThat(result.getDurationAbove130kmh() > 0, is(false));
        assertThat(result.getDistanceNaN() > 0, is(false));
        assertThat(result.getDurationNaN() > 0, is(false));

        // test tracksummary:
        assertThat(result.getTrackSummaries().getTrackSummaryList().size() == 1, is(true));
        TrackSummary first = result.getTrackSummaries().getTrackSummaryList().get(0);
        assertThat(first.getIdentifier().equals("58122cd0e4b01fb1c099aa85"), is(true));
    }

    @Test
    public void testRemoveTrackOnNonEmptyUserStats() {
        // test calculated userstatistics after onTrackDeletion
        MongoUserStatistic mus = new MongoUserStatistic();
        mus.setDistance(0.3338 + 0.3614);
        mus.setDuration(2 * 0.00138);
        mus.setDistanceAbove130kmh(0);
        mus.setDurationAbove130kmh(0);
        mus.setDistanceBelow60kmh(0.3338);
        mus.setDurationBelow60kmh(0.00138);
        mus.setDistanceNaN(0);
        mus.setDurationNaN(0);

        TrackSummaries ts = new TrackSummaries();
        ts.setTrackSummariesList(new ArrayList<>());
        TrackSummary trackSummary = new TrackSummary();
        trackSummary.setIdentifier("58122cd0e4b01fb1c099aa85");
        GeometryFactory gf = new GeometryFactory();
        Geometry start = gf.createPoint(new Coordinate(7.656558, 51.936485));
        trackSummary.setStartPosition(start);
        Geometry end = gf.createPoint(new Coordinate(7.649863, 51.941178));
        trackSummary.setEndPosition(end);
        ts.addTrackSummary(trackSummary);

        mus.setTrackSummaries(ts);

        List<Measurement> list = twm.getMeasurements();
        Measurements measures = Measurements.from(list).build();
        MongoUserStatistic result = new UserStatisticUtils().removeTrackStatistic(mus, twm, measures);

        // test results:
        assertThat(result.getDistance() - 0 < TOLERANCE_KM, is(true));
        assertThat(result.getDuration() - 0 < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceBelow60kmh() - 0 < TOLERANCE_KM, is(true));
        assertThat(result.getDurationBelow60kmh() - 0 < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceAbove130kmh() - 0 < TOLERANCE_KM, is(true));
        assertThat(result.getDurationAbove130kmh() - 0 < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceNaN() - 0 < TOLERANCE_KM, is(true));
        assertThat(result.getDurationNaN() - 0 < TOLERANCE_HOUR, is(true));
        // test tracksummary:
        assertThat(result.getTrackSummaries().getTrackSummaryList().isEmpty(), is(true));
    }

    @Test
    public void testAddTrackOnNonEmptyUserStats() {
        // Measurement 4:
        MongoMeasurement measurement4 = new MongoMeasurement();
        measurement4.setIdentifier("58122cd0e4b01fb1c099aa88");
        GeometryFactory gf3 = new GeometryFactory();
        Geometry g3 = gf3.createPoint(new Coordinate(7.63134, 51.937123));
        measurement4.setGeometry(g3);
        DateTime time4 = new DateTime(2016, 7, 23, 8, 53, 20);
        measurement4.setTime(time4);
        MeasurementValue mv3 = new MongoMeasurementValue();
        mv3.setValue(113);
        MongoPhenomenon mp3 = new MongoPhenomenon();
        mp3.setName("Speed");
        mp3.setUnit("km/h");
        mv3.setPhenomenon(mp3);
        measurement4.addValue(mv3);

        twm.addMeasurement(measurement4);

        // test calculated userstatistics after onTrackCreation
        MongoUserStatistic mus = new MongoUserStatistic();
        mus.setDistance(145.2);
        mus.setDistanceAbove130kmh(23.1);
        mus.setDistanceBelow60kmh(53.2);
        mus.setDistanceNaN(0.7);
        mus.setDuration(4.6);
        mus.setDurationAbove130kmh(0.165);
        mus.setDurationBelow60kmh(1.15);
        mus.setDurationNaN(0.3);
        TrackSummaries ts = new TrackSummaries();
        ts.setTrackSummariesList(new ArrayList<>());

        TrackSummary trackSummary = new TrackSummary();
        trackSummary.setIdentifier("identifier-123-abc-testtrack");
        GeometryFactory gf = new GeometryFactory();
        Geometry start = gf.createPoint(new Coordinate(7.392763, 52.391734));
        trackSummary.setStartPosition(start);
        Geometry end = gf.createPoint(new Coordinate(7.634724, 51.827649));
        trackSummary.setEndPosition(end);
        ts.addTrackSummary(trackSummary);

        mus.setTrackSummaries(ts);
        
        List<Measurement> list = twm.getMeasurements();
        Measurements measures = Measurements.from(list).build();
        MongoUserStatistic result = new UserStatisticUtils().addTrackStatistic(mus, twm, measures);

        // test results:
        assertThat(result.getDistance() - (145.2 + 0.3338 + 0.3614 + 1.347) < TOLERANCE_KM, is(true));
        assertThat(result.getDuration() - (4.6 + 3 * 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceBelow60kmh() - (53.2 + 0.3338) < TOLERANCE_KM, is(true));
        assertThat(result.getDurationBelow60kmh() - (1.15 + 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceAbove130kmh() - (23.1 + 1.347) < TOLERANCE_KM, is(true));
        assertThat(result.getDurationAbove130kmh() - (0.165 + 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(result.getDistanceNaN() - 4.6 < TOLERANCE_KM, is(true));
        assertThat(result.getDurationNaN() - 0.3 < TOLERANCE_HOUR, is(true));

        // test tracksummary:
        TrackSummary first = result.getTrackSummaries().getTrackSummaryList().get(0);
        TrackSummary second = result.getTrackSummaries().getTrackSummaryList().get(1);

        assertThat(result.getTrackSummaries().getTrackSummaryList().size() == 2, is(true));
        assertThat(first.getIdentifier().equals("identifier-123-abc-testtrack"), is(true));
        assertThat(second.getIdentifier().equals("58122cd0e4b01fb1c099aa85"), is(true));
    }

}
