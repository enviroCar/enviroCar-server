/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.inject.Inject;
import org.envirocar.server.core.guice.JtsModule;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Modules;
import org.envirocar.server.rest.guice.JacksonModule;
import org.envirocar.server.rest.guice.JsonSchemaModule;
import org.envirocar.server.rest.provider.StaticUriInfoModule;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Modules({JacksonModule.class, JtsModule.class, StaticUriInfoModule.class, JsonSchemaModule.class})
public class ValidationRule implements TestRule {
    @Inject
    private ObjectReader reader;
    @Inject
    private ObjectWriter writer;
    @Inject
    private JsonNodeCreator nodeFactory;
    @Inject
    private JsonSchemaFactory factory;
    @Inject
    private JsonSchemaUriConfiguration schemaUriConfiguration;

    public JsonNode parse(String json) {
        try {
            return reader.readTree(json);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Matcher<JsonNode> validInstanceOf(MediaType mt) {
        return validInstanceOf(mt.getParameters().get(MediaTypes.SCHEMA_ATTRIBUTE));
    }

    public Matcher<JsonNode> validInstanceOf(String uri) {
        return validInstanceOf(URI.create(uri));
    }

    public Matcher<JsonNode> validInstanceOf(URI uri) {
        return new IsValidInstanceOf(uri);
    }

    @Override
    public Statement apply(Statement base, org.junit.runner.Description description) {
        return base;
    }

    private class IsValidInstanceOf extends TypeSafeDiagnosingMatcher<JsonNode> {
        private final URI schema;

        IsValidInstanceOf(URI schema) {
            this.schema = schema;
        }

        @Override
        protected boolean matchesSafely(JsonNode item, Description mismatchDescription) {
            try {
                JsonSchema jsonSchema = factory.getJsonSchema(schemaUriConfiguration.toInternalURI(schema).toString());
                ProcessingReport report = jsonSchema.validate(item);
                if (!report.isSuccess()) {
                    ObjectNode objectNode = nodeFactory.objectNode();
                    objectNode.set(JSONConstants.INSTANCE_KEY, item);
                    ArrayNode errors = objectNode.putArray(JSONConstants.ERRORS_KEY);
                    for (ProcessingMessage m : report) {
                        errors.add(m.asJson());
                    }
                    mismatchDescription.appendText(writer.withDefaultPrettyPrinter().writeValueAsString(objectNode));
                }
                return report.isSuccess();
            } catch (ProcessingException | JsonProcessingException ex) {
                mismatchDescription.appendText(ex.getMessage());
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("valid instance of ").appendText(schema.toString());
        }
    }
}
