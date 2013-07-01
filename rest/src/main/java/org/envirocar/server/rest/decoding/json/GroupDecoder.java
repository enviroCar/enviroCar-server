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
package org.envirocar.server.rest.decoding.json;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonNode;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.rest.JSONConstants;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class GroupDecoder extends AbstractJSONEntityDecoder<Group> {
    public GroupDecoder() {
        super(Group.class);
    }

    @Override
    public Group decode(JsonNode j, MediaType mediaType) {
        Group group = getEntityFactory().createGroup();
        group.setName(j.path(JSONConstants.NAME_KEY).textValue());
        group.setDescription(j.path(JSONConstants.DESCRIPTION_KEY).textValue());
        return group;
    }
}
