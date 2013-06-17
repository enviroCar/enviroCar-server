/*
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
package io.car.server.rest.encoding;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.Phenomenon;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class PhenomenonEncoder extends AbstractEntityEncoder<Phenomenon> {
    public PhenomenonEncoder() {
        super(Phenomenon.class);
    }

    @Override
    public ObjectNode encode(Phenomenon t, AccessRights rights,
                             MediaType mediaType) {
        ObjectNode phenomenon = getJsonFactory().objectNode();
        if (t.hasName()) {
            phenomenon.put(JSONConstants.NAME_KEY, t.getName());
        }
        if (t.hasUnit()) {
            phenomenon.put(JSONConstants.UNIT_KEY, t.getUnit());
        }
        if (mediaType.equals(MediaTypes.PHENOMENON_TYPE)) {
            if (t.hasCreationTime()) {
                phenomenon.put(JSONConstants.CREATED_KEY, getDateTimeFormat()
                        .print(t.getCreationTime()));
            }
            if (t.hasModificationTime()) {
                phenomenon.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
        }
        return phenomenon;
    }
}
