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
package org.envirocar.server.rest.schema;

import java.io.IOException;
import java.util.Set;

import org.envirocar.server.rest.guice.JSONSchemaFactoryProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.util.JsonLoader;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@RunWith(GuiceRunner.class)
public class SchemaValidationTest {
    public static final String SCHEMA_URI =
            SchemaVersion.DRAFTV4.getLocation().toASCIIString();
    @Inject
    @Named(JSONSchemaFactoryProvider.SCHEMAS)
    private Set<String> schemas;
    @Rule
    public final ValidationRule validation = new ValidationRule();
    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void validate() {
        for (String schema : schemas) {
            try {
                JsonNode json = JsonLoader.fromResource(schema);
                errors.checkThat(json, validation.validInstanceOf(SCHEMA_URI));
            } catch (IOException ex) {
                errors.addError(ex);
            }
        }
    }
}
