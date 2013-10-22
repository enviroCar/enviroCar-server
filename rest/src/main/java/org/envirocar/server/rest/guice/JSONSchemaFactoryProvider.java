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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.LoadingConfiguration;
import com.github.fge.jsonschema.cfg.LoadingConfigurationBuilder;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.cfg.ValidationConfigurationBuilder;
import com.github.fge.jsonschema.exceptions.unchecked.ProcessingError;
import com.github.fge.jsonschema.format.draftv3.DateAttribute;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.library.Library;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.ref.JsonRef;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.name.Named;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JSONSchemaFactoryProvider implements Provider<JsonSchemaFactory> {
    public static final String SCHEMAS = "schemas";
    private final LoadingCache<String, JsonNode> cache;
    private final Set<String> schemas;

    @Inject
    public JSONSchemaFactoryProvider(@Named(SCHEMAS) Set<String> schemaResources,
                                     LoadingCache<String, JsonNode> cache) {
        this.schemas = schemaResources;
        this.cache = cache;
    }

    @Override
    public JsonSchemaFactory get() {
        return JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(validationConfiguration())
                .setLoadingConfiguration(loadingConfiguration())
                .freeze();
    }

    private LoadingConfiguration loadingConfiguration() {
        LoadingConfigurationBuilder cfgb = LoadingConfiguration.newBuilder();
        for (String schema : schemas) {
            try {
                cfgb.preloadSchema(cache.get(schema));
            } catch (ProcessingError ex) {
                throw new ProvisionException("Error loading " + schema, ex);
            } catch (ExecutionException ex) {
                throw new ProvisionException("Error loading " + schema, ex);
            }
        }
        return cfgb.freeze();
    }

    protected ValidationConfiguration validationConfiguration() {
        Library modifiedV4 = DraftV4Library.get().thaw()
                .addFormatAttribute("date", DateAttribute.getInstance())
                .freeze();
        ValidationConfigurationBuilder vcb = ValidationConfiguration
                .newBuilder();
        JsonRef ref = JsonRef.fromURI(SchemaVersion.DRAFTV4.getLocation());
        /*
         * FIXME ugliest hack ever. consider to duplicate the v4 schema under a
         * different URI and to adjust the schema files with that URI
         */
        setLibrary(vcb, ref, modifiedV4);
        setDefaultLibrary(vcb, modifiedV4);
        return vcb.freeze();
    }

    @SuppressWarnings("unchecked")
    protected void setLibrary(ValidationConfigurationBuilder vcb,
                              JsonRef ref, Library lib) {
        try {
            Field librariesField = ValidationConfigurationBuilder.class
                    .getDeclaredField("libraries");
            librariesField.setAccessible(true);
            final Map<JsonRef, Library> libraries =
                    (Map<JsonRef, Library>) librariesField.get(vcb);
            libraries.put(ref, lib);
            librariesField.setAccessible(false);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void setDefaultLibrary(ValidationConfigurationBuilder vcb,
                                     Library lib) {
        try {
            Field defaultLibraryField = ValidationConfigurationBuilder.class
                    .getDeclaredField("defaultLibrary");
            defaultLibraryField.setAccessible(true);
            defaultLibraryField.set(vcb, lib);
            defaultLibraryField.setAccessible(false);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
