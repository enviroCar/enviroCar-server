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
package io.car.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import io.car.server.rest.validation.JSONSchemaResourceFilterFactory;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JSONSchemaModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<String> mb = Multibinder.newSetBinder(
                binder(), new TypeLiteral<String>() {
        }, Names.named(JSONSchemaFactoryProvider.SCHEMAS));
        mb.addBinding().toInstance("/schema/definitions.json");
        mb.addBinding().toInstance("/schema/geometry.json");
        mb.addBinding().toInstance("/schema/group.create.json");
        mb.addBinding().toInstance("/schema/group.json");
        mb.addBinding().toInstance("/schema/group.modify.json");
        mb.addBinding().toInstance("/schema/groups.json");
        mb.addBinding().toInstance("/schema/measurement.create.json");
        mb.addBinding().toInstance("/schema/measurement.json");
        mb.addBinding().toInstance("/schema/measurements.json");
        mb.addBinding().toInstance("/schema/phenomenon.create.json");
        mb.addBinding().toInstance("/schema/phenomenon.json");
        mb.addBinding().toInstance("/schema/phenomenon.modify.json");
        mb.addBinding().toInstance("/schema/phenomenons.json");
        mb.addBinding().toInstance("/schema/root.json");
        mb.addBinding().toInstance("/schema/sensor.create.json");
        mb.addBinding().toInstance("/schema/sensor.json");
        mb.addBinding().toInstance("/schema/sensors.json");
        mb.addBinding().toInstance("/schema/track.create.json");
        mb.addBinding().toInstance("/schema/track.json");
        mb.addBinding().toInstance("/schema/track.modify.json");
        mb.addBinding().toInstance("/schema/tracks.json");
        mb.addBinding().toInstance("/schema/user.create.json");
        mb.addBinding().toInstance("/schema/user.json");
        mb.addBinding().toInstance("/schema/user.modify.json");
        mb.addBinding().toInstance("/schema/user.ref.json");
        mb.addBinding().toInstance("/schema/users.json");
        mb.addBinding().toInstance("/schema/statistics.json");
        mb.addBinding().toInstance("/schema/statistic.json");
        mb.addBinding().toInstance("/schema/activity.json");
        mb.addBinding().toInstance("/schema/activities.json");

        bindConstant().annotatedWith(Names
                .named(JSONSchemaResourceFilterFactory.VALIDATE_REQUESTS))
                .to(true);
        bindConstant().annotatedWith(Names
                .named(JSONSchemaResourceFilterFactory.VALIDATE_RESPONSES))
                .to(false);
    }
}
