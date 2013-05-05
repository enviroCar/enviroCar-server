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

import io.car.server.core.db.GroupDao;
import io.car.server.core.db.TrackDao;
import io.car.server.core.db.UserDao;
import io.car.server.mongo.MongoGroupDao;
import io.car.server.mongo.MongoTrackDao;
import io.car.server.mongo.MongoUserDao;

/**
 * TODO JavaDoc
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoDaoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserDao.class).to(MongoUserDao.class);
        bind(GroupDao.class).to(MongoGroupDao.class);
        bind(TrackDao.class).to(MongoTrackDao.class);
    }
}
