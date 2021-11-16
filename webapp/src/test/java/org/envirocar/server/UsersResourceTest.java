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
package org.envirocar.server;

import com.sun.jersey.api.client.ClientResponse;
import org.junit.ClassRule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UsersResourceTest extends ResourceTestBase {
    @ClassRule
    public static EnviroCarServer server = new EnviroCarServer();

    @Override
    protected EnviroCarServer getServer() {
        return server;
    }

    @Test
    public void testGetUsers() {
        assertThat(resource().path("/users").get(ClientResponse.class).getStatus(), is(401));
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
    public void testPutUsers() {
        assertThat(resource().path("/users").put(ClientResponse.class).getStatus(), is(405));
    }

    @Test
    public void testDeleteUsers() {
        assertThat(resource().path("/users").delete(ClientResponse.class).getStatus(), is(405));
    }
}
