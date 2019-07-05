/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.cfg.ValidationConfigurationBuilder;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.ref.JsonRef;
import com.github.fge.jsonschema.format.draftv3.DateAttribute;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.library.Library;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JsonSchemaFactoryProvider implements Provider<JsonSchemaFactory> {

    @Override
    public JsonSchemaFactory get() {
        return JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(validationConfiguration())
                .freeze();
    }

    private ValidationConfiguration validationConfiguration() {
        Library modifiedV4 = DraftV4Library.get().thaw()
                .addFormatAttribute("date", DateAttribute.getInstance())
                .freeze();
        ValidationConfigurationBuilder vcb = ValidationConfiguration.newBuilder();
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
    private void setLibrary(ValidationConfigurationBuilder vcb, JsonRef ref, Library lib) {
        try {
            Field librariesField = ValidationConfigurationBuilder.class.getDeclaredField("libraries");
            librariesField.setAccessible(true);
            final Map<JsonRef, Library> libraries = (Map<JsonRef, Library>) librariesField.get(vcb);
            libraries.put(ref, lib);
            librariesField.setAccessible(false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setDefaultLibrary(ValidationConfigurationBuilder vcb, Library lib) {
        try {
            Field defaultLibraryField = ValidationConfigurationBuilder.class.getDeclaredField("defaultLibrary");

            defaultLibraryField.setAccessible(true);
            defaultLibraryField.set(vcb, lib);
            defaultLibraryField.setAccessible(false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
