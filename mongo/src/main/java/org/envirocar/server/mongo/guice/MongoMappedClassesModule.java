/*
 * Copyright (C) 2013-2021 The enviroCar project
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
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.activities.MongoActivity;
import org.envirocar.server.mongo.activities.MongoGroupActivity;
import org.envirocar.server.mongo.activities.MongoTrackActivity;
import org.envirocar.server.mongo.activities.MongoUserActivity;
import org.envirocar.server.mongo.entity.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoMappedClassesModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Class<?>> mb = Multibinder.newSetBinder(
                binder(), new TypeLiteral<Class<?>>() {
                }, Names.named(MongoDB.MAPPED_CLASSES));
        mb.addBinding().toInstance(MongoUser.class);
        mb.addBinding().toInstance(MongoGroup.class);
        mb.addBinding().toInstance(MongoTrack.class);
        mb.addBinding().toInstance(MongoMeasurement.class);
        mb.addBinding().toInstance(MongoPhenomenon.class);
        mb.addBinding().toInstance(MongoSensor.class);
        mb.addBinding().toInstance(MongoActivity.class);
        mb.addBinding().toInstance(MongoGroupActivity.class);
        mb.addBinding().toInstance(MongoTrackActivity.class);
        mb.addBinding().toInstance(MongoUserActivity.class);
        mb.addBinding().toInstance(MongoStatistics.class);
        mb.addBinding().toInstance(MongoStatistic.class);
        mb.addBinding().toInstance(MongoStatisticKey.class);
        mb.addBinding().toInstance(MongoTermsOfUseInstance.class);
        mb.addBinding().toInstance(MongoAnnouncement.class);
        mb.addBinding().toInstance(MongoBadge.class);
        mb.addBinding().toInstance(MongoPasswordReset.class);
        mb.addBinding().toInstance(MongoFueling.class);
        mb.addBinding().toInstance(MongoUserStatistic.class);
        mb.addBinding().toInstance(MongoPrivacyStatement.class);
    }
}
