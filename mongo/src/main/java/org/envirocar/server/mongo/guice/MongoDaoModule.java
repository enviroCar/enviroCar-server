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
package org.envirocar.server.mongo.guice;

import org.envirocar.server.core.dao.ActivityDao;
import org.envirocar.server.core.dao.AnnouncementsDao;
import org.envirocar.server.core.dao.GroupDao;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.dao.StatisticsDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.dao.TrackDao;
import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.mongo.dao.MongoActivityDao;
import org.envirocar.server.mongo.dao.MongoAnnouncementsDao;
import org.envirocar.server.mongo.dao.MongoGroupDao;
import org.envirocar.server.mongo.dao.MongoMeasurementDao;
import org.envirocar.server.mongo.dao.MongoPhenomenonDao;
import org.envirocar.server.mongo.dao.MongoSensorDao;
import org.envirocar.server.mongo.dao.MongoStatisticsDao;
import org.envirocar.server.mongo.dao.MongoTermsOfUseDao;
import org.envirocar.server.mongo.dao.MongoTrackDao;
import org.envirocar.server.mongo.dao.MongoUserDao;

import com.google.inject.AbstractModule;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoDaoModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserDao.class).to(MongoUserDao.class);
        bind(GroupDao.class).to(MongoGroupDao.class);
        bind(TrackDao.class).to(MongoTrackDao.class);
        bind(MeasurementDao.class).to(MongoMeasurementDao.class);
        bind(SensorDao.class).to(MongoSensorDao.class);
        bind(StatisticsDao.class).to(MongoStatisticsDao.class);
        bind(PhenomenonDao.class).to(MongoPhenomenonDao.class);
        bind(ActivityDao.class).to(MongoActivityDao.class);
        bind(TermsOfUseDao.class).to(MongoTermsOfUseDao.class);
        bind(AnnouncementsDao.class).to(MongoAnnouncementsDao.class);
    }
}
