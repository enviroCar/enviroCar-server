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

import java.io.IOException;

import com.github.fge.jsonschema.cfg.LoadingConfiguration;
import com.github.fge.jsonschema.cfg.LoadingConfigurationBuilder;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.util.JsonLoader;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JsonSchemaFactoryProvider implements Provider<JsonSchemaFactory> {


    @Override
    public JsonSchemaFactory get() {
        return JsonSchemaFactory.newBuilder()
                .setLoadingConfiguration(load(LoadingConfiguration.newBuilder()).freeze())
                .freeze();
    }

    private void load(LoadingConfigurationBuilder cfg, String schema) {
        try {
            cfg.preloadSchema(JsonLoader.fromResource(schema));
        } catch (IOException ex) {
            throw new ProvisionException("Error loading " + schema, ex);
        }
    }

    protected LoadingConfigurationBuilder load(LoadingConfigurationBuilder cfg) {
        load(cfg, "/schema/group.create.json");
        load(cfg, "/schema/group.json");
        load(cfg, "/schema/group.modify.json");
        load(cfg, "/schema/groups.json");
        load(cfg, "/schema/measurement.create.json");
        load(cfg, "/schema/measurement.json");
        load(cfg, "/schema/measurements.json");
        load(cfg, "/schema/phenomenon.create.json");
        load(cfg, "/schema/phenomenon.json");
        load(cfg, "/schema/phenomenon.modify.json");
        load(cfg, "/schema/phenomenons.json");
        load(cfg, "/schema/root.json");
        load(cfg, "/schema/sensor.create.json");
        load(cfg, "/schema/sensor.json");
        load(cfg, "/schema/sensor.modify.json");
        load(cfg, "/schema/sensors.json");
        load(cfg, "/schema/track.create.json");
        load(cfg, "/schema/track.json");
        load(cfg, "/schema/track.modify.json");
        load(cfg, "/schema/tracks.json");
        load(cfg, "/schema/user.create.json");
        load(cfg, "/schema/user.json");
        load(cfg, "/schema/user.modify.json");
        load(cfg, "/schema/user.ref.json");
        load(cfg, "/schema/users.json");
        return cfg;
    }

}
