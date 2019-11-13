/*
 * Copyright (C) 2013-2019 The enviroCar project
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class JsonNodeMessageBodyWriter extends AbstractJSONMessageBodyWriter<JsonNode> {
    public JsonNodeMessageBodyWriter() {
        super(JsonNode.class);
    }

    @Override
    public ObjectNode encodeJSON(JsonNode entity, MediaType mediaType) {
        return (ObjectNode) entity;
    }

    @Override
    public ObjectNode encodeJSON(JsonNode entity, AccessRights rights, MediaType mediaType) {
        return (ObjectNode) entity;
    }
}
