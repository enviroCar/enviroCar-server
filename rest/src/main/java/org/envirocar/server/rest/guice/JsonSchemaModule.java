/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server.rest.guice;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import javax.ws.rs.core.UriInfo;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JsonSchemaModule extends AbstractModule {

    public static final String SCHEMAS = "schemas";

    @Override
    protected void configure() {

        requireBinding(JsonNodeCreator.class);
        requireBinding(UriInfo.class);

        bind(JsonSchemaFactory.class).toProvider(JsonSchemaFactoryProvider.class).in(Scopes.SINGLETON);

        Multibinder<String> mb = Multibinder.newSetBinder(binder(), String.class, Names.named(JsonSchemaModule.SCHEMAS));
        mb.addBinding().toInstance("definitions.json");
        mb.addBinding().toInstance("geometry.json");
        mb.addBinding().toInstance("group.create.json");
        mb.addBinding().toInstance("group.json");
        mb.addBinding().toInstance("group.modify.json");
        mb.addBinding().toInstance("groups.json");
        mb.addBinding().toInstance("measurement.create.json");
        mb.addBinding().toInstance("measurement.json");
        mb.addBinding().toInstance("measurements.json");
        mb.addBinding().toInstance("phenomenon.create.json");
        mb.addBinding().toInstance("phenomenon.json");
        mb.addBinding().toInstance("phenomenon.modify.json");
        mb.addBinding().toInstance("phenomenons.json");
        mb.addBinding().toInstance("root.json");
        mb.addBinding().toInstance("sensor.create.json");
        mb.addBinding().toInstance("sensor.json");
        mb.addBinding().toInstance("sensors.json");
        mb.addBinding().toInstance("track.create.json");
        mb.addBinding().toInstance("track.json");
        mb.addBinding().toInstance("track.modify.json");
        mb.addBinding().toInstance("tracks.json");
        mb.addBinding().toInstance("user.create.json");
        mb.addBinding().toInstance("user.json");
        mb.addBinding().toInstance("user.modify.json");
        mb.addBinding().toInstance("user.ref.json");
        mb.addBinding().toInstance("users.json");
        mb.addBinding().toInstance("statistics.json");
        mb.addBinding().toInstance("statistic.json");
        mb.addBinding().toInstance("activity.json");
        mb.addBinding().toInstance("activities.json");
        mb.addBinding().toInstance("terms-of-use.json");
        mb.addBinding().toInstance("terms-of-use-instance.json");
        mb.addBinding().toInstance("announcement.json");
        mb.addBinding().toInstance("announcements.json");
        mb.addBinding().toInstance("badge.json");
        mb.addBinding().toInstance("badges.json");
        mb.addBinding().toInstance("fueling.json");
        mb.addBinding().toInstance("fueling.create.json");
        mb.addBinding().toInstance("fuelings.json");
        mb.addBinding().toInstance("passwordResetRequest.json");
        mb.addBinding().toInstance("passwordResetVerification.json");
        mb.addBinding().toInstance("userStatistic.json");
        mb.addBinding().toInstance("privacy-statements.json");
        mb.addBinding().toInstance("privacy-statement.json");
        mb.addBinding().toInstance("exception.json");
    }
}
