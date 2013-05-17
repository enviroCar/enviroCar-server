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
package io.car.server.rest.schema;

import static com.github.fge.jsonschema.report.LogLevel.INFO;
import static com.github.fge.jsonschema.report.LogLevel.NONE;

import java.io.IOException;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JsonLoader;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import io.car.server.rest.guice.JSONSchemaFactoryProvider;
import io.car.server.rest.guice.JSONSchemaModule;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class SchemaValidationTest {
    private static final Logger log = LoggerFactory.getLogger(SchemaValidationTest.class);
    private static Set<String> schemas;

    @BeforeClass
    public static void createInjector() {
        Injector i = Guice.createInjector(new JSONSchemaModule());
        schemas = i.getInstance(Key.get(new TypeLiteral<Set<String>>() {
        }, Names.named(JSONSchemaFactoryProvider.SCHEMAS)));
    }

    @Test
    public void validate() throws ProcessingException, IOException, JSONException {
        JsonSchemaFactory fac = JsonSchemaFactory.byDefault();
        JsonSchema schemaV4 = fac.getJsonSchema("resource:/draftv4/schema");
        for (String schema : schemas) {
            log.info("Validating: {}", schema);
            ProcessingReport result = schemaV4.validate(JsonLoader.fromResource(schema));
            for (ProcessingMessage m : result) {
                String message = new JSONObject(m.toString()).toString(4);
                switch (m.getLogLevel()) {
                    case DEBUG:
                        log.debug("Result: {}", message);
                        break;
                    case NONE:
                    case INFO:
                        log.info("Result: {}", message);
                        break;
                    case WARNING:
                        log.warn("Result: {}", message);
                        break;
                    case ERROR:
                    case FATAL:
                        log.error("Result: {}", message);
                        break;
                }
            }
            if (!result.isSuccess()) {
                Assert.fail(String.format("Schema %s is invalid.", schema));
            }
        }
    }
}
