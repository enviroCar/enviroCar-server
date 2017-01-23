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
package org.envirocar.server.rest.encoding;

import org.bson.BSONObject;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.DataServiceImpl;
import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.FriendServiceImpl;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.GroupServiceImpl;
import org.envirocar.server.core.StatisticsService;
import org.envirocar.server.core.StatisticsServiceImpl;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.UserServiceImpl;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.activities.ActivityFactory;
import org.envirocar.server.core.activities.GroupActivity;
import org.envirocar.server.core.activities.TrackActivity;
import org.envirocar.server.core.activities.UserActivity;
import org.envirocar.server.core.dao.ActivityDao;
import org.envirocar.server.core.dao.AnnouncementsDao;
import org.envirocar.server.core.dao.BadgesDao;
import org.envirocar.server.core.dao.FuelingDao;
import org.envirocar.server.core.dao.GroupDao;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.dao.StatisticsDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.dao.TrackDao;
import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.util.BCryptPasswordEncoder;
import org.envirocar.server.core.util.GeodesicGeometryOperations;
import org.envirocar.server.core.util.GeometryConverter;
import org.envirocar.server.core.util.GeometryOperations;
import org.envirocar.server.core.util.PasswordEncoder;
import org.envirocar.server.mongo.activities.MongoActivity;
import org.envirocar.server.mongo.activities.MongoGroupActivity;
import org.envirocar.server.mongo.activities.MongoTrackActivity;
import org.envirocar.server.mongo.activities.MongoUserActivity;
import org.envirocar.server.mongo.dao.MongoActivityDao;
import org.envirocar.server.mongo.dao.MongoAnnouncementsDao;
import org.envirocar.server.mongo.dao.MongoBadgesDao;
import org.envirocar.server.mongo.dao.MongoFuelingDao;
import org.envirocar.server.mongo.dao.MongoGroupDao;
import org.envirocar.server.mongo.dao.MongoMeasurementDao;
import org.envirocar.server.mongo.dao.MongoPhenomenonDao;
import org.envirocar.server.mongo.dao.MongoSensorDao;
import org.envirocar.server.mongo.dao.MongoStatisticsDao;
import org.envirocar.server.mongo.dao.MongoTermsOfUseDao;
import org.envirocar.server.mongo.dao.MongoTrackDao;
import org.envirocar.server.mongo.dao.MongoUserDao;
import org.envirocar.server.mongo.dao.privates.MongoPasswordResetDAO;
import org.envirocar.server.mongo.dao.privates.PasswordResetDAO;
import org.envirocar.server.mongo.dao.MongoUserStatisticDao;
import org.envirocar.server.mongo.entity.MongoUserStatistic;
import org.envirocar.server.mongo.entity.MongoFueling;
import org.envirocar.server.mongo.entity.MongoGroup;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.util.GeoBSON;
import org.envirocar.server.rest.decoding.json.GeoJSONDecoder;
import org.envirocar.server.rest.decoding.json.JSONEntityDecoder;
import org.envirocar.server.rest.decoding.json.MeasurementDecoder;
import org.envirocar.server.rest.rights.AccessRights;
import org.envirocar.server.rest.rights.NonRestrictiveRights;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.CarSimilarityService;
import org.envirocar.server.core.CarSimilarityServiceImpl;
import org.envirocar.server.core.UserStatisticService;
import org.envirocar.server.core.UserStatisticServiceImpl;

public class EncodingTestModule extends AbstractModule {

	@Override
	protected void configure() {
        bind(DataService.class).to(DataServiceImpl.class);
        bind(AccessRights.class).to(NonRestrictiveRights.class);
        bind(DataService.class).to(DataServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(FriendService.class).to(FriendServiceImpl.class);
        bind(GroupService.class).to(GroupServiceImpl.class);
        bind(StatisticsService.class).to(StatisticsServiceImpl.class);
        bind(PasswordEncoder.class).to(BCryptPasswordEncoder.class);
        bind(UserStatisticService.class).to(UserStatisticServiceImpl.class);
        bind(GeometryOperations.class).to(GeodesicGeometryOperations.class);
        Multibinder.newSetBinder(binder(), TypeConverter.class);
        bind(new TypeLiteral<GeometryConverter<BSONObject>>() {
        }).to(GeoBSON.class);
        bind(new TypeLiteral<JSONEntityDecoder<Measurement>>() {
        }).to(MeasurementDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Geometry>>() {
        }).to(GeoJSONDecoder.class);
        install(new FactoryModuleBuilder()
        .implement(User.class, MongoUser.class)
        .implement(Group.class, MongoGroup.class)
        .implement(Track.class, MongoTrack.class)
        .implement(UserStatistic.class, MongoUserStatistic.class)
        .implement(Measurement.class, MongoMeasurement.class)
        .implement(MeasurementValue.class, MongoMeasurementValue.class)
        .implement(Phenomenon.class, MongoPhenomenon.class)
        .implement(Sensor.class, MongoSensor.class)
        .implement(Fueling.class, MongoFueling.class)
        .build(EntityFactory.class));
        install(new FactoryModuleBuilder()
        .implement(Activity.class, MongoActivity.class)
        .implement(GroupActivity.class, MongoGroupActivity.class)
        .implement(TrackActivity.class, MongoTrackActivity.class)
        .implement(UserActivity.class, MongoUserActivity.class)
        .build(ActivityFactory.class));
        bind(UserDao.class).to(MongoUserDao.class);
        bind(GroupDao.class).to(MongoGroupDao.class);
        bind(TrackDao.class).to(MongoTrackDao.class);
        bind(UserStatisticDao.class).to(MongoUserStatisticDao.class);
        bind(MeasurementDao.class).to(MongoMeasurementDao.class);
        bind(SensorDao.class).to(MongoSensorDao.class);
        bind(StatisticsDao.class).to(MongoStatisticsDao.class);
        bind(PhenomenonDao.class).to(MongoPhenomenonDao.class);
        bind(ActivityDao.class).to(MongoActivityDao.class);
        bind(TermsOfUseDao.class).to(MongoTermsOfUseDao.class);
        bind(AnnouncementsDao.class).to(MongoAnnouncementsDao.class);
        bind(BadgesDao.class).to(MongoBadgesDao.class);
        bind(PasswordResetDAO.class).to(MongoPasswordResetDAO.class);
        bind(FuelingDao.class).to(MongoFuelingDao.class);
        bind(CarSimilarityService.class).to(CarSimilarityServiceImpl.class);
	}


    @Provides
    @Singleton
    public DateTimeFormatter formatter() {
        return ISODateTimeFormat.dateTimeNoMillis();
    }
	
}
