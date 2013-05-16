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
import java.util.Set;

import com.github.fge.jsonschema.cfg.LoadingConfiguration;
import com.github.fge.jsonschema.cfg.LoadingConfigurationBuilder;
import com.github.fge.jsonschema.exceptions.unchecked.ProcessingError;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.util.JsonLoader;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.name.Named;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JSONSchemaFactoryProvider implements Provider<JsonSchemaFactory> {
    public static final String SCHEMAS = "schemas";
    private Set<String> schemas;

    @Inject
    public JSONSchemaFactoryProvider(@Named(SCHEMAS) Set<String> schemaResources) {
        this.schemas = schemaResources;
    }

    @Override
    public JsonSchemaFactory get() {
        return JsonSchemaFactory.newBuilder().setLoadingConfiguration(loadingConfiguration()).freeze();
    }

    private LoadingConfiguration loadingConfiguration() {
        LoadingConfigurationBuilder cfgb = LoadingConfiguration.newBuilder();
        for (String schema : schemas) {
            try {
                cfgb.preloadSchema(JsonLoader.fromResource(schema));
            } catch (IOException ex) {
                throw new ProvisionException("Error loading " + schema, ex);
            } catch (ProcessingError ex) {
                throw new ProvisionException("Error loading " + schema, ex);
            }
        }
        return cfgb.freeze();
    }
}
