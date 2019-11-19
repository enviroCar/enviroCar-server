/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.envirocar.server.core.activities.*;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.core.guice.ResourceShutdownListener;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.MongoShutdownListener;
import org.envirocar.server.mongo.activities.MongoActivity;
import org.envirocar.server.mongo.activities.MongoGroupActivity;
import org.envirocar.server.mongo.activities.MongoTrackActivity;
import org.envirocar.server.mongo.activities.MongoUserActivity;
import org.envirocar.server.mongo.entity.*;
import org.envirocar.server.mongo.statistics.NewTrackListener;
import org.envirocar.server.mongo.statistics.StatisticsUpdateScheduler;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.Mapper;

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
                .implement(Fueling.class, MongoFueling.class)
                .build(EntityFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Activity.class, MongoActivity.class)
                .implement(GroupActivity.class, MongoGroupActivity.class)
                .implement(TrackActivity.class, MongoTrackActivity.class)
                .implement(UserActivity.class, MongoUserActivity.class)
                .build(ActivityFactory.class));
        bind(MongoDB.class);
        bind(StatisticsUpdateScheduler.class).asEagerSingleton();
        bind(NewTrackListener.class).asEagerSingleton();

        Multibinder<ResourceShutdownListener> binder = Multibinder.newSetBinder(binder(), ResourceShutdownListener.class);
        binder.addBinding().to(MongoShutdownListener.class);
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
