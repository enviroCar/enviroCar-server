/*
 * Copyright (C) 2013-2022 The enviroCar project
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
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Fuelings;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * JSON encoder for {@link Fuelings}.
 *
 * @author Christian Autermann
 */
@Provider
@Singleton
public class FuelingsJSONEncoder extends AbstractJSONEntityEncoder<Fuelings> {
    private final JSONEntityEncoder<Fueling> fuelingEncoder;

    /**
     * Creates a new {@code FuelingsJSONEncoder} using the supplied encoder as a
     * delegate.
     *
     * @param fuelingEncoder the encoder to use for {@link Fueling}s
     */
    @Inject
    public FuelingsJSONEncoder(JSONEntityEncoder<Fueling> fuelingEncoder) {
        super(Fuelings.class);
        this.fuelingEncoder = fuelingEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Fuelings entity, AccessRights rights, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode fuelings = root.putArray(JSONConstants.FUELINGS);

        for (Fueling b : entity) {
            fuelings.add(fuelingEncoder.encodeJSON(b, rights, mediaType));
        }
        return root;
    }

}
