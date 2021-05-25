/*
 * Copyright (C) 2013-2020 The enviroCar project
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
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.resources.RootResource;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Year;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.envirocar.server.matchers.JerseyMatchers.hasStatus;
import static org.envirocar.server.matchers.JerseyMatchers.isCompatible;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public class TrackResourceTest extends ResourceTestBase {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL_ADDRESS = "test@example.com";
    private static final String GASOLINE = "gasoline";
    private static final String CONSTRUCTION_YEAR = "constructionYear";
    private static final String MANUFACTURER = "manufacturer";
    private static final String MODEL = "model";
    private static final String ENGINE_DISPLACEMENT = "engineDisplacement";

    @Inject
    private ObjectReader reader;

    @ClassRule
    public static EnviroCarServer server = new EnviroCarServer();

    @Override
    protected EnviroCarServer getServer() {
        return server;
    }
    @Test
    public void testContentNegotiation() throws IOException {

        createUser(USERNAME, PASSWORD, MAIL_ADDRESS);

        String sensorId = createSensor();

        ClientResponse response = resource().path("/tracks")
                                            .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader(USERNAME, PASSWORD))
                                            .entity(getTrack(sensorId), MediaTypes.JSON_TYPE)
                                            .post(ClientResponse.class);
        assertThat(response, hasStatus(Response.Status.CREATED));
        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertThat(location, not(isEmptyOrNullString()));

        response = client().resource(location).accept(MediaTypes.CSV).get(ClientResponse.class);
        assertThat(response, hasStatus(Response.Status.OK));
        assertThat(response.getType(), isCompatible(MediaTypes.CSV_TYPE));

        response = client().resource(location + ".csv").accept(MediaTypes.CSV).get(ClientResponse.class);
        assertThat(response, hasStatus(Response.Status.OK));
        assertThat(response.getType(), isCompatible(MediaTypes.CSV_TYPE));

        response = client().resource(location).accept(MediaTypes.JSON).get(ClientResponse.class);
        assertThat(response, hasStatus(Response.Status.OK));
        assertThat(response.getType(), isCompatible(MediaTypes.JSON_TYPE));
        assertThat(response.getType().getParameters().containsKey(MediaTypes.SCHEMA_ATTRIBUTE), is(true));

        assertThat(response.getType().getParameters().get(MediaTypes.SCHEMA_ATTRIBUTE),
                   is(String.format("%s/schema/track.json", getBaseURL())));
    }

    protected void createPhenomenon(String name, String unit) {
        assertThat(resource().path("/").path(RootResource.PHENOMENONS)
                             .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader(USERNAME, PASSWORD))
                             .entity(getNodeFactory().objectNode()
                                                     .put(JSONConstants.NAME_KEY, name)
                                                     .put(JSONConstants.UNIT_KEY, unit),
                                     MediaTypes.JSON)
                             .post(ClientResponse.class),
                   hasStatus(Response.Status.CREATED));
    }

    private String createSensor() {

        final JsonNode sensor = getNodeFactory().objectNode()
                                                .put(JSONConstants.TYPE_KEY, JSONConstants.CAR_KEY)
                                                .set(JSONConstants.PROPERTIES_KEY,
                                                     getNodeFactory()
                                                             .objectNode()
                                                             .put(ENGINE_DISPLACEMENT, 123)
                                                             .put(MODEL, MODEL)
                                                             .put(MANUFACTURER, MANUFACTURER)
                                                             .put(CONSTRUCTION_YEAR, Year.now()
                                                                                         .getValue())
                                                             .put(JSONConstants.FUEL_TYPE, GASOLINE));
        ClientResponse response = resource().path("/").path(RootResource.SENSORS)
                                            .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader(USERNAME, PASSWORD))
                                            .entity(sensor, MediaTypes.JSON_TYPE)
                                            .post(ClientResponse.class);
        assertThat(response, hasStatus(Response.Status.CREATED));

        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertThat(location, not(isEmptyOrNullString()));
        String sensorId = client().resource(location).accept(MediaTypes.JSON).get(JsonNode.class)
                                  .path(JSONConstants.PROPERTIES_KEY).path(JSONConstants.IDENTIFIER_KEY).textValue();
        assertThat(sensorId, not(isEmptyOrNullString()));
        return sensorId;
    }

    private JsonNode getTrack(String sensor) throws IOException {
        JsonNode node = reader.readTree(getClass().getResourceAsStream("/track.json"));
        ((ObjectNode) node.path(JSONConstants.PROPERTIES_KEY)).put(JSONConstants.SENSOR_KEY, sensor);

        Set<String> phenomenons = new HashSet<>();

        node.path(GeoJSONConstants.FEATURES_KEY).forEach(n -> {
            Iterator<String> iter = n.path(GeoJSONConstants.PROPERTIES_KEY)
                                     .path(JSONConstants.PHENOMENONS_KEY).fieldNames();
            while (iter.hasNext()) {
                phenomenons.add(iter.next());
            }
        });

        phenomenons.forEach(phenomenon -> createPhenomenon(phenomenon, phenomenon));

        return node;
    }

}
