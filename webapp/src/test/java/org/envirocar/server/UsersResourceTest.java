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
package org.envirocar.server;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UsersResourceTest extends ResourceTestBase {
    @Test
    public void testGetUsers() {
        assertThat(resource().path("/users")
                .get(ClientResponse.class).getStatus(), is(403));
//        ClientResponse response = resource()
//                .path("/rest/users")
//                .accept(MediaType.APPLICATION_JSON)
//                .get(ClientResponse.class);
//
//        assertThat(response, hasStatus(Status.OK));
//        assertThat(response.getType(), isCompatible(MediaType.APPLICATION_JSON_TYPE));
//
//        JsonNode root = response.getEntity(JsonNode.class);
//        assertThat(root, is(notNullValue()));
//
//        assertThat(root, hasProperty(JSONConstants.USERS_KEY));
//        assertThat(root.get(JSONConstants.USERS_KEY).isArray(), is(true));
    }

    @Test
    public void testPostUsers() {
    }

    @Test
    public void testPutUsers() {
        assertThat(resource().path("/users")
                .put(ClientResponse.class).getStatus(), is(405));
    }

    @Test
    public void testDeleteUsers() {
        assertThat(resource().path("/users")
                .delete(ClientResponse.class).getStatus(), is(405));
    }
}
