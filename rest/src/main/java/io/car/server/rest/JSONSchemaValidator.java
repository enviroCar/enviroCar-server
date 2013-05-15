/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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
