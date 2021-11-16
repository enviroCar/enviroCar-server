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

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import org.envirocar.server.rest.JSONConstants;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.envirocar.server.matchers.JerseyMatchers.hasProperty;
import static org.envirocar.server.matchers.JerseyMatchers.hasStatus;
import static org.envirocar.server.matchers.JerseyMatchers.isCompatible;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class RootResourceTest extends ResourceTestBase {

    @ClassRule
    public static EnviroCarServer server = new EnviroCarServer();

    @Override
    protected EnviroCarServer getServer() {
        return server;
    }

    @Test
    public void testGetRoot() {
        ClientResponse response = resource().path("/")
                                            .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        assertThat(response, hasStatus(Status.OK));
        assertThat(response.getType(), isCompatible(MediaType.APPLICATION_JSON_TYPE));

        JsonNode root = response.getEntity(JsonNode.class);
        assertThat(root, is(notNullValue()));

        assertThat(root, hasProperty(JSONConstants.MEASUREMENTS_KEY));
        assertThat(root.get(JSONConstants.MEASUREMENTS_KEY).asText(), instanceOf(String.class));

        assertThat(root, hasProperty(JSONConstants.SENSORS_KEY));
        assertThat(root.get(JSONConstants.SENSORS_KEY).asText(), instanceOf(String.class));

        assertThat(root, hasProperty(JSONConstants.PHENOMENONS_KEY));
        assertThat(root.get(JSONConstants.PHENOMENONS_KEY).asText(), instanceOf(String.class));
    }

    @Test
    public void testPostRoot() {
        assertThat(resource().path("/").post(ClientResponse.class).getStatus(), is(405));
    }

    @Test
    public void testPutRoot() {
        assertThat(resource().path("/").put(ClientResponse.class).getStatus(), is(405));
    }

    @Test
    public void testDeleteRoot() {
        assertThat(resource().path("/").delete(ClientResponse.class).getStatus(), is(405));
    }

}
