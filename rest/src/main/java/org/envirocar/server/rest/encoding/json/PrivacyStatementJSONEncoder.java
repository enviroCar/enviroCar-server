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
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Matthes Rieke
 */
@Provider
@Singleton
public class PrivacyStatementJSONEncoder extends AbstractJSONEntityEncoder<PrivacyStatement> {

    public PrivacyStatementJSONEncoder() {
        super(PrivacyStatement.class);
    }

    @Override
    public ObjectNode encodeJSON(PrivacyStatement entity, AccessRights rights, MediaType mediaType) {
        ObjectNode termsOfUse = getJsonFactory().objectNode();
        if (entity.getIdentifier() != null) {
            termsOfUse.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        }
        if (entity.getIssuedDate() != null) {
            termsOfUse.put(JSONConstants.ISSUED_DATE, entity.getIssuedDate());
        }
        if (getSchemaUriConfiguration().isSchema(mediaType, Schemas.PRIVACY_STATEMENT)) {
            if (entity.hasCreationTime()) {
                termsOfUse.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
            }
            if (entity.hasModificationTime()) {
                termsOfUse.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
            }
            if (entity.getContents() != null) {
                termsOfUse.put(JSONConstants.CONTENTS, entity.getContents());
            }
        }
        return termsOfUse;
    }
}
