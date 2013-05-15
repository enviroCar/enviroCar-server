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
package io.car.server.rest.guice;

import static com.github.fge.jsonschema.util.JsonLoader.fromResource;

import java.io.IOException;

import org.codehaus.jettison.json.JSONObject;

import com.github.fge.jsonschema.cfg.LoadingConfiguration;
import com.github.fge.jsonschema.cfg.LoadingConfigurationBuilder;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import io.car.server.rest.JSONSchemaValidator;
import io.car.server.rest.Validator;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JerseyValidationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonSchemaFactory.class).toProvider(JsonSchemaFactoryProvider.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<Validator<JSONObject>>() {}).to(JSONSchemaValidator.class);
    }

    private class JsonSchemaFactoryProvider implements Provider<JsonSchemaFactory> {
        private LoadingConfigurationBuilder cfg;

        @Override
        public JsonSchemaFactory get() {
            cfg = LoadingConfiguration.newBuilder();
            load("/schema/group.create.json");
            load("/schema/group.json");
            load("/schema/group.modify.json");
            load("/schema/groups.json");
            load("/schema/measurement.create.json");
            load("/schema/measurement.json");
            load("/schema/measurements.json");
            load("/schema/phenomenon.create.json");
            load("/schema/phenomenon.json");
            load("/schema/phenomenon.modify.json");
            load("/schema/phenomenons.json");
            load("/schema/root.json");
            load("/schema/sensor.create.json");
            load("/schema/sensor.json");
            load("/schema/sensor.modify.json");
            load("/schema/sensors.json");
            load("/schema/track.create.json");
            load("/schema/track.json");
            load("/schema/track.modify.json");
            load("/schema/tracks.json");
            load("/schema/user.create.json");
            load("/schema/user.json");
            load("/schema/user.modify.json");
            load("/schema/user.ref.json");
            load("/schema/users.json");
            return JsonSchemaFactory.newBuilder()
                    .setLoadingConfiguration(cfg.freeze()).freeze();
        }

        private void load(String schema) {
            try {
                cfg.preloadSchema(fromResource(schema));
            } catch (IOException ex) {
                throw new ProvisionException("Error loading " + schema, ex);
            }
        }
    }
    
}
