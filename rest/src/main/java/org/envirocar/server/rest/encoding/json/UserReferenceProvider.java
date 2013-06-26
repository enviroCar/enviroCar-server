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

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.UserReference;
import org.envirocar.server.rest.decoding.json.AbstractJSONMessageBodyReader;
import org.envirocar.server.rest.decoding.json.JSONEntityDecoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class UserReferenceProvider extends AbstractJSONMessageBodyReader<UserReference> {
    private final JSONEntityDecoder<User> userDecoder;

    @Inject
    public UserReferenceProvider(JSONEntityDecoder<User> userDecoder) {
        super(UserReference.class);
        this.userDecoder = userDecoder;
    }

    @Override
    public UserReference decode(JsonNode j, MediaType mt) {
        User user = userDecoder.decode(j, mt);
        return new UserReference(user.getName());
    }
}
