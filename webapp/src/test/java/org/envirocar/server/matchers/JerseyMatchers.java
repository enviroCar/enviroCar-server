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
package org.envirocar.server.matchers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.StatusType;

import org.hamcrest.Matcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.ClientResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyMatchers {
    private JerseyMatchers() {
    }

    public static Matcher<MediaType> isCompatible(MediaType type) {
        return new IsCompatibleMediaType(type);
    }

    public static Matcher<JsonNode> hasProperty(String key) {
        return new IsJsonObjectWithProperty(key);
    }

    public static Matcher<ClientResponse> hasStatus(StatusType status) {
        return new IsResponseStatus(status);
    }
}
