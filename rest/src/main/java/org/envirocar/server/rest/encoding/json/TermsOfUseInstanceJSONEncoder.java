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
package org.envirocar.server.rest.encoding.json;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class TermsOfUseInstanceJSONEncoder extends AbstractJSONEntityEncoder<TermsOfUseInstance> {
    private static final String DATE = "issued-date";
	private static final String CONTENTS = "contents";

	public TermsOfUseInstanceJSONEncoder() {
        super(TermsOfUseInstance.class);
    }

    @Override
    public ObjectNode encodeJSON(TermsOfUseInstance t, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode termsOfUse = getJsonFactory().objectNode();
        if (mediaType.equals(MediaTypes.TERMS_OF_USE_TYPE)) {
            if (t.hasCreationTime()) {
                termsOfUse.put(JSONConstants.CREATED_KEY, getDateTimeFormat()
                        .print(t.getCreationTime()));
            }
            if (t.hasModificationTime()) {
                termsOfUse.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
            if (t.getIssuedDate() != null) {
            	termsOfUse.put(DATE, t.getIssuedDate());
            }
            if (t.getContents() != null) {
            	termsOfUse.put(CONTENTS, t.getContents());
            }
        }
        return termsOfUse;
    }
}
