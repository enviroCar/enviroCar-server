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
package io.car.server.rest;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JsonLoader;

import io.car.server.core.exception.ValidationException;
import io.car.server.rest.coding.JSONConstants;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JSONSchemaValidator implements Validator<JSONObject> {
    private final JsonSchemaFactory factory;

    public JSONSchemaValidator(JsonSchemaFactory factory) {
        this.factory = factory;
    }

    @Override
    public void validate(JSONObject t, MediaType mt) throws ValidationException {
        if (mt.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
            String schemaId = mt.getParameters().get("schema");
            if (schemaId != null) {
                try {
                    validate(t, factory.getJsonSchema(schemaId));
                } catch (ProcessingException ex) {
                    throw new ValidationException(ex);
                }
            }
        }
    }

    protected void validate(JSONObject t, JsonSchema schema) throws ValidationException, ProcessingException {
        try {
            validate(JsonLoader.fromString(t.toString()), schema);
        } catch (IOException ex) {
            throw new ValidationException(ex);
        }
    }

    protected void validate(JsonNode t, JsonSchema schema) throws ValidationException, ProcessingException {
        ProcessingReport report = schema.validate(t);
        if (!report.isSuccess()) {
            try {
                JSONArray errors = new JSONArray();
                for (ProcessingMessage message : report) {
                    errors.put(new JSONObject(message.getMessage()));
                }
                throw new JSONValidationException(new JSONObject().put(JSONConstants.ERRORS, errors));
            } catch (JSONException ex) {
                throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
