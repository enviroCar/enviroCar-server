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

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.Phenomenon;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.PhenomenonResource;
import io.car.server.rest.resources.PhenomenonsResource;
import io.car.server.rest.resources.RootResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class PhenomenonEncoder extends AbstractEntityEncoder<Phenomenon> {
    @Override
    public ObjectNode encode(Phenomenon t, MediaType mediaType) {
        ObjectNode phenomenon = getJsonFactory().objectNode();
        if (t.hasName()) {
            phenomenon.put(JSONConstants.NAME_KEY, t.getName());
        }
        if (mediaType.equals(MediaTypes.PHENOMENON_TYPE)) {
            if (t.hasUnit()) {
                phenomenon.put(JSONConstants.UNIT_KEY, t.getUnit());
            }
            if (t.hasCreationTime()) {
                phenomenon.put(JSONConstants.CREATED_KEY, getDateTimeFormat()
                        .print(t.getCreationTime()));
            }
            if (t.hasModificationTime()) {
                phenomenon.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
            URI stats = getUriInfo().getAbsolutePathBuilder()
                    .path(PhenomenonResource.STATISTIC).build();
            phenomenon.put(JSONConstants.STATISTIC_KEY, stats.toString());
        } else {
            URI href = getUriInfo().getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.PHENOMENONS)
                    .path(PhenomenonsResource.PHENOMENON)
                    .build(t.getName());
            phenomenon.put(JSONConstants.HREF_KEY, href.toString());
        }
        return phenomenon;
    }
}
