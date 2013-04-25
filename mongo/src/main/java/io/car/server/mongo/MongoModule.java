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
package io.car.server.mongo;

import com.google.inject.AbstractModule;

import io.car.server.core.EntityFactory;
import io.car.server.core.db.UserDao;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MongoDB.class);
        bind(UserDao.class).to(MongoUserDao.class);
        bind(EntityFactory.class).to(MongoEntityFactory.class);
    }
}
