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
package org.envirocar.server.rest.decoding;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.UserReference;
import org.envirocar.server.rest.decoding.json.AbstractJSONEntityDecoder;
import org.envirocar.server.rest.decoding.json.AbstractJSONMessageBodyReader;
import org.envirocar.server.rest.decoding.json.JSONEntityDecoder;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
public class UserReferenceDecoder extends AbstractJSONEntityDecoder<UserReference> {
    private final JSONEntityDecoder<User> userDecoder;

    @Inject
    public UserReferenceDecoder(JSONEntityDecoder<User> userDecoder) {
        super(UserReference.class);
        this.userDecoder = userDecoder;
    }

    @Override
    public UserReference decode(JsonNode node, MediaType mediaType) {
        User user = userDecoder.decode(node, mediaType);
        return new UserReference(user.getName());
    }
}
