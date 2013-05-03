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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import io.car.server.core.EntityFactory;
import io.car.server.core.Group;
import io.car.server.core.User;
import io.car.server.core.db.GroupDao;
import io.car.server.core.db.UserDao;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoModule extends AbstractModule {
    private static final Logger log = LoggerFactory.getLogger(MongoModule.class);
    @Override
    protected void configure() {
        log.debug("Installing MongoModule");
        install(new FactoryModuleBuilder()
                .implement(User.class, MongoUser.class)
                .implement(Group.class, MongoGroup.class)
                .build(EntityFactory.class));
        install(new MongoConnectionModule());
        bind(MongoDB.class);
        bind(UserDao.class).to(MongoUserDao.class);
        bind(GroupDao.class).to(MongoGroupDao.class);
    }

    @Provides
    public Datastore datastore(MongoDB mongoDB) {
        return mongoDB.getDatastore();
    }

    @Provides
    public Morphia morphia(MongoDB mongoDB) {
        return mongoDB.getMorphia();
    }

    @Provides
    public MongoClient mongoClient(MongoDB mongoDB) {
        return mongoDB.getMongoClient();
    }

    @Provides
    public Mongo mongo(MongoDB mongoDB) {
        return mongoClient(mongoDB);
    }

    @Provides
    public Mapper mapper(Morphia morphia) {
        return morphia.getMapper();
    }

    @Provides
    public DB db(Datastore datastore) {
        return datastore.getDB();
    }
}
