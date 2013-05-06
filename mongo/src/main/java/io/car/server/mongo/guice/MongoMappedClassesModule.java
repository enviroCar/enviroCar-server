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
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoGroup;
import io.car.server.mongo.entity.MongoMeasurement;
import io.car.server.mongo.entity.MongoSubscriber;
import io.car.server.mongo.entity.MongoSubscription;
import io.car.server.mongo.entity.MongoSubscriptionFilter;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoMappedClassesModule extends AbstractModule {

    @Override
    @SuppressWarnings("rawtypes")
    protected void configure() {
        Multibinder<Class<?>> mb = Multibinder.newSetBinder(binder(), new TypeLiteral<Class<?>>() {},
                                                            Names.named(MongoDB.MAPPED_CLASSES));
        mb.addBinding().toInstance(MongoUser.class);
        mb.addBinding().toInstance(MongoGroup.class);

        mb.addBinding().toInstance(MongoSubscriber.class);
        mb.addBinding().toInstance(MongoSubscription.class);
        mb.addBinding().toInstance(MongoSubscriptionFilter.class);

        mb.addBinding().toInstance(MongoTrack.class);
        mb.addBinding().toInstance(MongoMeasurement.class);
        
    }
}
