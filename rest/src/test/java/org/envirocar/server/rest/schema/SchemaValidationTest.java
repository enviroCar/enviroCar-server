/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import com.github.fge.jsonschema.SchemaVersion;
import org.envirocar.server.rest.GuiceRunner;
import org.envirocar.server.rest.guice.JsonSchemaModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URI;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@RunWith(GuiceRunner.class)
public class SchemaValidationTest {
    private static final String SCHEMA_URI = SchemaVersion.DRAFTV4.getLocation().toASCIIString();
    @Inject
    @Named(JsonSchemaModule.SCHEMAS)
    private Set<String> schemas;
    @Inject
    private JsonSchemaUriLoader loader;
    @Rule
    public final ValidationRule validation = new ValidationRule();
    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void validate() {
        this.schemas.stream()
                    .map(URI::create)
                    .forEach(schema ->
                                     this.errors.checkThat(
                                             this.errors.checkSucceeds(() -> this.loader.load(schema)),
                                             this.validation.validInstanceOf(SCHEMA_URI)));
    }
}
