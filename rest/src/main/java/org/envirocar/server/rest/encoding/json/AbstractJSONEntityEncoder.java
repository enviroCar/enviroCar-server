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
package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.server.rest.rights.AccessRights;
import org.envirocar.server.rest.schema.JsonSchemaUriConfiguration;
import org.joda.time.format.DateTimeFormatter;

import javax.ws.rs.core.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractJSONEntityEncoder<T>
        extends AbstractJSONMessageBodyWriter<T> {
    private JsonNodeFactory jsonFactory;
    private DateTimeFormatter dateTimeFormat;
    private Provider<AccessRights> rights;
    private JsonSchemaUriConfiguration schemaUriConfiguration;

    public AbstractJSONEntityEncoder(Class<T> classType) {
        super(classType);
    }

    @Inject
    public void setJsonFactory(JsonNodeFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public DateTimeFormatter getDateTimeFormat() {
        return this.dateTimeFormat;
    }

    @Inject
    public void setDateTimeFormat(DateTimeFormatter dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    @Inject
    public void setRights(Provider<AccessRights> rights) {
        this.rights = rights;
    }

    @Override
    public JsonNode encodeJSON(T entity, MediaType mediaType) {
        return encodeJSON(entity, this.rights.get(), mediaType);
    }

    @Inject
    public void setSchemaUriConfiguration(JsonSchemaUriConfiguration schemaUriConfiguration) {
        this.schemaUriConfiguration = schemaUriConfiguration;
    }

    protected JsonSchemaUriConfiguration getSchemaUriConfiguration() {
        return this.schemaUriConfiguration;
    }
}