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


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.PrivacyStatements;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;

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
public class PrivacyStatementsJSONEncoder extends AbstractJSONEntityEncoder<PrivacyStatements> {
    private final JSONEntityEncoder<PrivacyStatement> privacyStatementEncoder;

    @Inject
    public PrivacyStatementsJSONEncoder(
            JSONEntityEncoder<PrivacyStatement> phenomenonEncoder) {
        super(PrivacyStatements.class);
        this.privacyStatementEncoder = phenomenonEncoder;
    }

    @Override
    public ObjectNode encodeJSON(PrivacyStatements statements, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode PrivacyStatements = root.putArray(JSONConstants.PRIVACY_STATEMENTS);

        for (PrivacyStatement statement : statements) {
            PrivacyStatements.add(this.privacyStatementEncoder.encodeJSON(statement, mediaType));
        }
        return root;
    }
}
