/*
 * Copyright (C) 2013 The enviroCar project
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

import static org.envirocar.server.rest.validation.JSONSchemaResourceFilterFactory.VALIDATE_REQUESTS;
import static org.envirocar.server.rest.validation.JSONSchemaResourceFilterFactory.VALIDATE_RESPONSES;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.util.JsonLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyValidationModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<String> mb = Multibinder
                .newSetBinder(binder(), String.class,
                              Names.named(JSONSchemaFactoryProvider.SCHEMAS));
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
        mb.addBinding().toInstance("/schema/terms-of-use.json");
        mb.addBinding().toInstance("/schema/terms-of-use-instance.json");
        mb.addBinding().toInstance("/schema/announcement.json");
        mb.addBinding().toInstance("/schema/announcements.json");
        bindConstant().annotatedWith(Names.named(VALIDATE_REQUESTS)).to(true);
        bindConstant().annotatedWith(Names.named(VALIDATE_RESPONSES)).to(true);
        bind(JsonSchemaFactory.class).
                toProvider(JSONSchemaFactoryProvider.class)
                .in(Scopes.SINGLETON);
    }

    @Provides
    public LoadingCache<String, JsonNode> schemaCache() {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, JsonNode>() {
            @Override
            public JsonNode load(String key) throws IOException {
                return JsonLoader.fromResource(key);
            }
        });
    }
}
