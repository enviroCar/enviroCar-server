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
package io.car.server.mongo.guice;

import com.google.inject.AbstractModule;

import io.car.server.core.dao.GroupDao;
import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.PhenomenonDao;
import io.car.server.core.dao.SensorDao;
import io.car.server.core.dao.TrackDao;
import io.car.server.core.dao.UserDao;
import io.car.server.mongo.dao.MongoGroupDao;
import io.car.server.mongo.dao.MongoMeasurementDao;
import io.car.server.mongo.dao.MongoPhenomenonDao;
import io.car.server.mongo.dao.MongoSensorDao;
import io.car.server.mongo.dao.MongoTrackDao;
import io.car.server.mongo.dao.MongoUserDao;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoDaoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserDao.class).to(MongoUserDao.class);
        bind(GroupDao.class).to(MongoGroupDao.class);
        bind(TrackDao.class).to(MongoTrackDao.class);
        bind(MeasurementDao.class).to(MongoMeasurementDao.class);
        bind(SensorDao.class).to(MongoSensorDao.class);
        bind(PhenomenonDao.class).to(MongoPhenomenonDao.class);
    }
}
