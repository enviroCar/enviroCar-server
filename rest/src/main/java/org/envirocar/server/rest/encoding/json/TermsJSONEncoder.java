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
package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.rights.AccessRights;

import javax.ws.rs.core.MediaType;

public abstract class TermsJSONEncoder<T extends Terms> extends AbstractJSONEntityEncoder<T> {
    private final String schema;

    public TermsJSONEncoder(Class<T> clazz, String schema) {
        super(clazz);
        this.schema = schema;
    }

    @Override
    public ObjectNode encodeJSON(T entity, AccessRights rights, MediaType mediaType) {
        ObjectNode terms = getJsonFactory().objectNode();
        if (entity.getIdentifier() != null) {
            terms.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        }
        if (entity.getIssuedDate() != null) {
            terms.put(JSONConstants.ISSUED_DATE, entity.getIssuedDate());
        }
        if (getSchemaUriConfiguration().isSchema(mediaType, schema)) {
            if (entity.hasCreationTime()) {
                terms.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
            }
            if (entity.hasModificationTime()) {
                terms.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
            }
            if (entity.getContents() != null) {
                terms.put(JSONConstants.CONTENTS, entity.getContents());
            }
        }
        return terms;
    }
}
