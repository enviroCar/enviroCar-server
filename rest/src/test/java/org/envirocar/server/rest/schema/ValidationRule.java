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

import javax.ws.rs.core.MediaType;

import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.guice.JerseyCodingModule;
import org.envirocar.server.rest.guice.JerseyValidationModule;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Modules({ JerseyCodingModule.class, JerseyValidationModule.class })
public class ValidationRule implements TestRule {
    @Inject
    private ObjectReader reader;
    @Inject
    private ObjectWriter writer;
    @Inject
    private JsonNodeFactory nodeFactory;
    @Inject
    private JsonSchemaFactory factory;

    public JsonNode parse(String json) {
        try {
            return reader.readTree(json);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Matcher<JsonNode> validInstanceOf(MediaType mt) {
        return new IsValidInstanceOf(mt.getParameters()
                .get(MediaTypes.SCHEMA_ATTRIBUTE));
    }

    public Matcher<JsonNode> validInstanceOf(String uri) {
        return new IsValidInstanceOf(uri);
    }

    @Override
    public Statement apply(Statement base,
                           org.junit.runner.Description description) {
        return base;
    }

    private class IsValidInstanceOf extends TypeSafeDiagnosingMatcher<JsonNode> {
        private final String schema;

        IsValidInstanceOf(String schema) {
            this.schema = schema;
        }

        @Override
        protected boolean matchesSafely(JsonNode item,
                                        Description mismatchDescription) {
            try {
                JsonSchema jsonSchema = factory.getJsonSchema(schema);
                ProcessingReport report = jsonSchema.validate(item);
                if (!report.isSuccess()) {
                    ObjectNode objectNode = nodeFactory.objectNode();
                    objectNode.put(JSONConstants.INSTANCE_KEY, item);
                    ArrayNode errors = objectNode
                            .putArray(JSONConstants.ERRORS_KEY);
                    for (ProcessingMessage m : report) {
                        errors.add(m.asJson());
                    }
                    mismatchDescription.appendText(writer
                            .withDefaultPrettyPrinter()
                            .writeValueAsString(objectNode));
                }
                return report.isSuccess();
            } catch (ProcessingException ex) {
                mismatchDescription.appendText(ex.getMessage());
            } catch (JsonProcessingException ex) {
                mismatchDescription.appendText(ex.getMessage());
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("valid instance of ").appendText(schema);
        }
    }
}
