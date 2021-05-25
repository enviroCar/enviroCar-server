/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server.mongo.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.mongo.entity.MongoTrackSummary;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoUserStatistic;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Maurin Radtke (maurin.radtke@uni-muenster.de)
 */
public class TrackStatisticImplTest {

    private Track track;
    private List<Measurement> measurements;
    private static final double TOLERANCE_KM = 0.01;        // 1 meter
    private static final double TOLERANCE_HOUR = 0.000276;  // 1 second
    private static final GeometryFactory gf = new GeometryFactory();

    @Before
    public void initialize() {

        track = new MongoTrack();
        track.setBegin(new DateTime(2016, 7, 23, 8, 53, 5));
        track.setEnd(new DateTime(2016, 7, 23, 8, 53, 15));
        track.setIdentifier("58122cd0e4b01fb1c099aa85");
        track.setLength(100);

        // Measurement 1:
        MongoMeasurement measurement1 = new MongoMeasurement();
        measurement1.setIdentifier("58122cd0e4b01fb1c099aa86");
        measurement1.setGeometry(gf.createPoint(new Coordinate(7.656558, 51.936485)));
        measurement1.setTime(new DateTime(2016, 7, 23, 8, 53, 5));
        measurement1.addValue(createValue(10.0));

        // Measurement 2:
        MongoMeasurement measurement2 = new MongoMeasurement();
        measurement2.setIdentifier("58122cd0e4b01fb1c099aa87");
        measurement2.setGeometry(gf.createPoint(new Coordinate(7.653232, 51.938678)));
        measurement2.setTime(new DateTime(2016, 7, 23, 8, 53, 10));
        measurement2.addValue(createValue(100.0));

        // Measurement 3:
        MongoMeasurement measurement3 = new MongoMeasurement();
        measurement3.setIdentifier("58122cd0e4b01fb1c099aa88");
        measurement3.setGeometry(gf.createPoint(new Coordinate(7.649863, 51.941178)));
        measurement3.setTime(new DateTime(2016, 7, 23, 8, 53, 15));
        measurement3.addValue(createValue(133.0));

        measurements = new ArrayList<>(4);
        measurements.add(measurement1);
        measurements.add(measurement2);
        measurements.add(measurement3);
    }

    private MeasurementValue createValue(double v) {
        MeasurementValue measurementValue = new MongoMeasurementValue();
        measurementValue.setValue(v);
        measurementValue.setPhenomenon(createSpeed());
        return measurementValue;
    }

    private MongoPhenomenon createSpeed() {
        MongoPhenomenon phenomenon = new MongoPhenomenon();
        phenomenon.setName("Speed");
        phenomenon.setUnit("km/h");
        return phenomenon;
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

        add(mus);

        // test results:
        assertThat(mus.getDistance() - (0.3338 + 0.3614) < TOLERANCE_KM, is(true));
        assertThat(mus.getDuration() - (2 * 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceBelow60kmh() - 0.3338 < TOLERANCE_KM, is(true));
        assertThat(mus.getDurationBelow60kmh() - 0.00138 < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceAbove130kmh() > 0, is(false));
        assertThat(mus.getDurationAbove130kmh() > 0, is(false));
        assertThat(mus.getDistanceNaN() > 0, is(false));
        assertThat(mus.getDurationNaN() > 0, is(false));

        // test tracksummary:
        assertThat(mus.getTrackSummaries().size() == 1, is(true));
        TrackSummary first = mus.getTrackSummaries().get(0);
        assertThat(first.getIdentifier(), is(track.getIdentifier()));
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
        TrackSummary trackSummary = new MongoTrackSummary(track.getIdentifier());
        GeometryFactory gf = new GeometryFactory();
        Geometry start = gf.createPoint(new Coordinate(7.656558, 51.936485));
        trackSummary.setStartPosition(start);
        Geometry end = gf.createPoint(new Coordinate(7.649863, 51.941178));
        trackSummary.setEndPosition(end);
        mus.addTrackSummary(trackSummary);

        remove(mus);

        // test results:
        assertThat(mus.getDistance() - 0 < TOLERANCE_KM, is(true));
        assertThat(mus.getDuration() - 0 < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceBelow60kmh() - 0 < TOLERANCE_KM, is(true));
        assertThat(mus.getDurationBelow60kmh() - 0 < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceAbove130kmh() - 0 < TOLERANCE_KM, is(true));
        assertThat(mus.getDurationAbove130kmh() - 0 < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceNaN() - 0 < TOLERANCE_KM, is(true));
        assertThat(mus.getDurationNaN() - 0 < TOLERANCE_HOUR, is(true));
        // test tracksummary:
        assertThat(mus.getTrackSummaries().size(), is(0));
    }

    @Test
    public void testAddTrackOnNonEmptyUserStats() {
        // Measurement 4:
        MongoMeasurement measurement4 = new MongoMeasurement();
        measurement4.setIdentifier("58122cd0e4b01fb1c099aa88");
        measurement4.setGeometry(gf.createPoint(new Coordinate(7.63134, 51.937123)));
        measurement4.setTime(new DateTime(2016, 7, 23, 8, 53, 20));
        measurement4.addValue(createValue(113));

        measurements.add(measurement4);

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

        TrackSummary trackSummary = new MongoTrackSummary("identifier-123-abc-testtrack");
        Geometry start = gf.createPoint(new Coordinate(7.392763, 52.391734));
        trackSummary.setStartPosition(start);
        Geometry end = gf.createPoint(new Coordinate(7.634724, 51.827649));
        trackSummary.setEndPosition(end);
        mus.addTrackSummary(trackSummary);

        add(mus);
        // test results:
        assertThat(mus.getDistance() - (145.2 + 0.3338 + 0.3614 + 1.347) < TOLERANCE_KM, is(true));
        assertThat(mus.getDuration() - (4.6 + 3 * 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceBelow60kmh() - (53.2 + 0.3338) < TOLERANCE_KM, is(true));
        assertThat(mus.getDurationBelow60kmh() - (1.15 + 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceAbove130kmh() - (23.1 + 1.347) < TOLERANCE_KM, is(true));
        assertThat(mus.getDurationAbove130kmh() - (0.165 + 0.00138) < TOLERANCE_HOUR, is(true));
        assertThat(mus.getDistanceNaN() - 4.6 < TOLERANCE_KM, is(true));
        assertThat(mus.getDurationNaN() - 0.3 < TOLERANCE_HOUR, is(true));

        // test tracksummary:
        assertThat(mus.getTrackSummaries().size(), is(2));
        TrackSummary first = mus.getTrackSummaries().get(0);
        TrackSummary second = mus.getTrackSummaries().get(1);

        assertThat(first.getIdentifier(), is(trackSummary.getIdentifier()));
        assertThat(second.getIdentifier(), is(track.getIdentifier()));
    }

    private void remove(MongoUserStatistic mus) {
        final TrackStatisticImpl trackStatistic = new TrackStatisticImpl(track, measurements);
        trackStatistic.negate();
        trackStatistic.addTo(mus);
        mus.removeTrackSummary(track.getIdentifier());
    }

    private void add(MongoUserStatistic mus) {
        final TrackStatisticImpl trackStatistic = new TrackStatisticImpl(track, measurements);
        trackStatistic.addTo(mus);
        mus.addTrackSummary(trackStatistic.getSummary());
    }

}
