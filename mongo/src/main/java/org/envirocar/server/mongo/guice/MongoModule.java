/*
 * Copyright (C) 2013-2022 The enviroCar project
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
import com.mongodb.DB;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.Mapper;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.activities.ActivityFactory;
import org.envirocar.server.core.activities.GroupActivity;
import org.envirocar.server.core.activities.TrackActivity;
import org.envirocar.server.core.activities.UserActivity;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.activities.MongoActivity;
import org.envirocar.server.mongo.activities.MongoGroupActivity;
import org.envirocar.server.mongo.activities.MongoTrackActivity;
import org.envirocar.server.mongo.activities.MongoUserActivity;
import org.envirocar.server.mongo.entity.MongoFueling;
import org.envirocar.server.mongo.entity.MongoGroup;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoPrivacyStatement;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.envirocar.server.mongo.entity.MongoTermsOfUseInstance;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.statistics.NewTrackListener;
import org.envirocar.server.mongo.statistics.StatisticsUpdateScheduler;

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
                        .implement(TermsOfUseInstance.class, MongoTermsOfUseInstance.class)
                        .implement(PrivacyStatement.class, MongoPrivacyStatement.class)
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
    public Mapper mapper(Morphia morphia) {
        return morphia.getMapper();
    }

    @Provides
    public DB db(Datastore datastore) {
        return datastore.getDB();
    }
}
