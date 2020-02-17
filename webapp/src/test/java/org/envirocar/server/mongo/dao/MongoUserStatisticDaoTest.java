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
package org.envirocar.server.mongo.dao;

import com.google.inject.Inject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.envirocar.server.EnviroCarServer;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.dao.TrackDao;
import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class MongoUserStatisticDaoTest {
    @ClassRule
    public static EnviroCarServer server = new EnviroCarServer();
    @Inject
    private MongoUserStatisticDao dao;
    @Inject
    private UserDao userDao;
    @Inject
    private PhenomenonDao phenomenonDao;
    @Inject
    private MeasurementDao measurementDao;
    @Inject
    private TrackDao trackDao;
    @Inject
    private EntityFactory entityFactory;
    @Inject
    private GeometryFactory gf;
    private User user;
    private Track track;
    private Phenomenon phenomenon;

    @After
    public void after() {
        deleteTrack();
        deleteUser();
    }

    private void deleteUser() {
        userDao.delete(user, false);
    }

    private void deleteTrack() {
        trackDao.delete(track);
    }

    @Before
    public void before() {
        server.getInjector().injectMembers(this);
        user = createUser();
        track = createTrack();
        phenomenon = createPhenomenon();
        createMeasurements();

    }

    @Test
    public void test() {
        UserStatistic userStatistic = dao.get(new UserStatisticFilter(user));

        assertThat(userStatistic, is(not(nullValue())));
        assertThat(userStatistic.getNumTracks(), is(1));
        assertThat(userStatistic.getTrackSummaries().size(), is(1));
        assertThat(userStatistic.getDistance(), is(greaterThan(0.0)));
        assertThat(userStatistic.getDuration(), is(greaterThan(0.0)));

        userStatistic = dao.get(new UserStatisticFilter(user));

        assertThat(userStatistic, is(not(nullValue())));
        assertThat(userStatistic.getNumTracks(), is(1));
        assertThat(userStatistic.getTrackSummaries().size(), is(1));
        assertThat(userStatistic.getDistance(), is(greaterThan(0.0)));
        assertThat(userStatistic.getDuration(), is(greaterThan(0.0)));
    }

    private void createMeasurements() {
        // Measurement 1:
        Measurement measurement1 = entityFactory.createMeasurement();
        measurement1.setGeometry(gf.createPoint(new Coordinate(7.656558, 51.936485)));
        measurement1.setTime(new DateTime(2016, 7, 23, 8, 53, 5));
        measurement1.addValue(createValue(10.0));
        measurement1.setTrack(track);
        measurementDao.save(measurement1);

        // Measurement 2:
        Measurement measurement2 = entityFactory.createMeasurement();
        measurement2.setGeometry(gf.createPoint(new Coordinate(7.653232, 51.938678)));
        measurement2.setTime(new DateTime(2016, 7, 23, 8, 53, 10));
        measurement2.addValue(createValue(100.0));
        measurement2.setTrack(track);
        measurementDao.save(measurement2);

        // Measurement 3:
        Measurement measurement3 = entityFactory.createMeasurement();
        measurement3.setGeometry(gf.createPoint(new Coordinate(7.649863, 51.941178)));
        measurement3.setTime(new DateTime(2016, 7, 23, 8, 53, 15));
        measurement3.addValue(createValue(133.0));
        measurement3.setTrack(track);
        measurementDao.save(measurement3);
    }

    private User createUser() {
        User user = entityFactory.createUser();
        user.setName("test-user");
        userDao.save(user);
        return user;
    }

    private Track createTrack() {
        Track track = entityFactory.createTrack();
        track.setBegin(DateTime.now().minus(Duration.standardMinutes(10)));
        track.setEnd(DateTime.now().minus(Duration.standardMinutes(10)));
        track.setUser(user);
        trackDao.save(track);
        return track;
    }

    private MeasurementValue createValue(double v) {
        MeasurementValue measurementValue = new MongoMeasurementValue();
        measurementValue.setValue(v);
        measurementValue.setPhenomenon(phenomenon);
        return measurementValue;
    }

    private Phenomenon createPhenomenon() {
        Phenomenon phenomenon = entityFactory.createPhenomenon();
        phenomenon.setName("Speed");
        phenomenon.setUnit("km/h");
        phenomenonDao.create(phenomenon);
        return phenomenon;
    }
}
