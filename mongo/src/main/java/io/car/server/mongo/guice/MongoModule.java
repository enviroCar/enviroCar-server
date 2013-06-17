/*
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

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import io.car.server.core.activities.Activity;
import io.car.server.core.activities.ActivityFactory;
import io.car.server.core.activities.GroupActivity;
import io.car.server.core.activities.TrackActivity;
import io.car.server.core.activities.UserActivity;
import io.car.server.core.entities.EntityFactory;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.MeasurementValue;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.activities.MongoActivity;
import io.car.server.mongo.activities.MongoGroupActivity;
import io.car.server.mongo.activities.MongoTrackActivity;
import io.car.server.mongo.activities.MongoUserActivity;
import io.car.server.mongo.entity.MongoGroup;
import io.car.server.mongo.entity.MongoMeasurement;
import io.car.server.mongo.entity.MongoMeasurementValue;
import io.car.server.mongo.entity.MongoPhenomenon;
import io.car.server.mongo.entity.MongoSensor;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(User.class, MongoUser.class)
                .implement(Group.class, MongoGroup.class)
                .implement(Track.class, MongoTrack.class)
                .implement(Measurement.class, MongoMeasurement.class)
                .implement(MeasurementValue.class, MongoMeasurementValue.class)
                .implement(Phenomenon.class, MongoPhenomenon.class)
                .implement(Sensor.class, MongoSensor.class)
                .build(EntityFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Activity.class, MongoActivity.class)
                .implement(GroupActivity.class, MongoGroupActivity.class)
                .implement(TrackActivity.class, MongoTrackActivity.class)
                .implement(UserActivity.class, MongoUserActivity.class)
                .build(ActivityFactory.class));
        bind(MongoDB.class);
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
