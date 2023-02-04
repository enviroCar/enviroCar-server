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
package org.envirocar.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.event.kafka.KafkaConstants;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.resources.RootResource;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.KafkaContainer;

import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Duration;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.envirocar.server.matchers.JerseyMatchers.hasStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class TrackChunkTest extends ResourceTestBase {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL_ADDRESS = "test@example.com";
    private static final String GASOLINE = "gasoline";
    private static final String CONSTRUCTION_YEAR = "constructionYear";
    private static final String MANUFACTURER = "manufacturer";
    private static final String MODEL = "model";
    private static final String ENGINE_DISPLACEMENT = "engineDisplacement";
    public static final String TOPIC_NAME = "tracks";

    @Inject
    private ObjectReader reader;
    @Inject
    private ObjectMapper mapper;
    @Inject
    private JsonNodeFactory nodeFactory;
    @ClassRule
    public static final EnviroCarServer server = new EnviroCarServer();
    @Inject
    private KafkaContainer kafkaContainer;
    @Inject
    @Named(KafkaConstants.KAFKA_TRACK_TOPIC)
    private String trackTopic;
    @Inject
    @Named(KafkaConstants.KAFKA_MEASUREMENT_TOPIC)
    private String measurementTopic;

    @Override
    protected EnviroCarServer getServer() {
        return server;
    }

    private KafkaConsumer<String, JsonNode> createKafkaConsumer()
            throws ExecutionException, InterruptedException, TimeoutException {
        String bootstrapServers = this.kafkaContainer.getBootstrapServers();

        try (AdminClient adminClient = AdminClient.create(ImmutableMap.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                                                                          bootstrapServers))) {
            NewTopic topic = new NewTopic(TOPIC_NAME, 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(topic)).all()
                       .get(30, TimeUnit.SECONDS);
        }
        KafkaConsumer<String, JsonNode> consumer = new KafkaConsumer<>(
                ImmutableMap.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ConsumerConfig.GROUP_ID_CONFIG, "test-" + UUID.randomUUID(),
                        ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 157286400,
                        ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 157286400,
                        ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2000
                               ),
                new StringDeserializer(),
                new KafkaJacksonDeserializer<>(JsonNode.class, this.mapper));
        consumer.subscribe(Arrays.asList(this.trackTopic, this.measurementTopic));
        return consumer;
    }

    @Test
    public void testChunkedTrackCreation()
            throws IOException, InterruptedException, ExecutionException, TimeoutException {
        ConsumerRecords<String, JsonNode> records;
        ClientResponse response;
        JsonNode responseEntity;

        try (KafkaConsumer<String, JsonNode> kafkaConsumer = createKafkaConsumer()) {

            // check that there are no published tracks
            records = kafkaConsumer.poll(Duration.ofSeconds(2));
            assertThat(records.count(), is(0));

            createUser(USERNAME, PASSWORD, MAIL_ADDRESS);

            // create empty ongoing track
            String location = createTrack(createEmptyTrack(createSensor()));

            // check the track response
            String trackId = checkTrackResponse(location, TrackStatus.ONGOING).path(GeoJSONConstants.PROPERTIES_KEY)
                                                                              .path(JSONConstants.IDENTIFIER_KEY)
                                                                              .textValue();

            // add measurements to the track
            JsonNode features = getFeatures();
            createPhenomenons(features);

            response = putTrack(createTrackFeatureUpdate(features), location);
            assertThat(response, hasStatus(Response.Status.NO_CONTENT));

            // check that the track response now contains the measurements
            responseEntity = checkTrackResponse(location, TrackStatus.ONGOING);
            assertThat(responseEntity.path(GeoJSONConstants.FEATURES_KEY).isArray(), is(true));
            assertThat(responseEntity.path(GeoJSONConstants.FEATURES_KEY).size(), is(features.size()));
            assertThat(responseEntity.path(GeoJSONConstants.PROPERTIES_KEY).path(JSONConstants.LENGTH_KEY).asDouble(), is(0.5));

            // check that there are no published tracks
            records = kafkaConsumer.poll(Duration.ofSeconds(2));
            assertThat(records.count(), is(features.size()));
            assertThat(Iterables.size(records.records(this.trackTopic)), is(0));
            assertThat(Iterables.size(records.records(this.measurementTopic)), is(features.size()));


            // finish the track
            response = putTrack(createTrackStatusUpdate(), location);
            assertThat(response, hasStatus(Response.Status.NO_CONTENT));

            // check that the track response now has the finished status
            responseEntity = checkTrackResponse(location, TrackStatus.FINISHED);
            assertThat(responseEntity.path(GeoJSONConstants.FEATURES_KEY).isArray(), is(true));
            assertThat(responseEntity.path(GeoJSONConstants.FEATURES_KEY).size(), is(features.size()));
            assertThat(responseEntity.path(GeoJSONConstants.PROPERTIES_KEY).path(JSONConstants.LENGTH_KEY).asDouble(), is(200.0));

            // check that the track was published
            records = kafkaConsumer.poll(Duration.ofSeconds(30));
            assertThat(records.count(), is(1));
            JsonNode value = records.iterator().next().value();
            assertThat(value, is(notNullValue()));
            assertThat(value.path(GeoJSONConstants.PROPERTIES_KEY)
                            .path(JSONConstants.STATUS_KEY).textValue(),
                       is(TrackStatus.FINISHED.toString()));
            assertThat(value.path(GeoJSONConstants.PROPERTIES_KEY)
                            .path(JSONConstants.IDENTIFIER_KEY)
                            .textValue(),
                       is(trackId));
        }
    }

    private ObjectNode createTrackStatusUpdate() {
        ObjectNode track = createFeatureCollection();
        track.with(GeoJSONConstants.PROPERTIES_KEY)
             .put(JSONConstants.STATUS_KEY, TrackStatus.FINISHED.toString())
             .put(JSONConstants.LENGTH_KEY, 200.0);
        return track;
    }

    private ObjectNode createEmptyTrack(String sensorId) {
        ObjectNode track = createFeatureCollection();
        track.with(GeoJSONConstants.PROPERTIES_KEY)
             .put(JSONConstants.STATUS_KEY, TrackStatus.ONGOING.toString())
             .put(JSONConstants.LENGTH_KEY, 0.5)
             .put(JSONConstants.SENSOR_KEY, sensorId);
        return track;
    }

    private ObjectNode createTrackFeatureUpdate(JsonNode features) {
        return createFeatureCollection().set(GeoJSONConstants.FEATURES_KEY, features);
    }

    private ObjectNode createFeatureCollection() {
        ObjectNode node = this.nodeFactory.objectNode()
                                          .put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_COLLECTION_TYPE);
        node.putObject(GeoJSONConstants.PROPERTIES_KEY);
        node.putArray(GeoJSONConstants.FEATURES_KEY);
        return node;
    }

    private String createTrack(ObjectNode track) {
        ClientResponse response = resource().path("/").path(RootResource.TRACKS)
                                            .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader(USERNAME, PASSWORD))
                                            .entity(track, MediaTypes.JSON_TYPE)
                                            .post(ClientResponse.class);
        assertThat(response, hasStatus(Response.Status.CREATED));
        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertThat(location, not(isEmptyOrNullString()));
        return location;
    }

    private JsonNode checkTrackResponse(String location, TrackStatus status) {
        ClientResponse response = client().resource(location).accept(MediaTypes.JSON).get(ClientResponse.class);
        assertThat(response, hasStatus(Response.Status.OK));
        JsonNode responseEntity = response.getEntity(JsonNode.class);
        assertThat(responseEntity.path(GeoJSONConstants.PROPERTIES_KEY)
                                 .path(JSONConstants.STATUS_KEY)
                                 .textValue(),
                   is(status.toString()));
        return responseEntity;
    }

    private ClientResponse putTrack(ObjectNode track, String location) {
        return client().resource(location)
                       .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeader(USERNAME, PASSWORD))
                       .entity(track, MediaTypes.JSON_TYPE)
                       .put(ClientResponse.class);
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

        JsonNode sensor = getNodeFactory().objectNode()
                                          .put(JSONConstants.TYPE_KEY, JSONConstants.CAR_KEY)
                                          .set(JSONConstants.PROPERTIES_KEY,
                                               getNodeFactory()
                                                       .objectNode()
                                                       .put(ENGINE_DISPLACEMENT, 123)
                                                       .put(MODEL, MODEL)
                                                       .put(MANUFACTURER, MANUFACTURER)
                                                       .put(CONSTRUCTION_YEAR, Year.now().getValue())
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

    private JsonNode getFeatures() throws IOException {
        return this.reader.readTree(getClass().getResourceAsStream("/track.json"))
                          .path(GeoJSONConstants.FEATURES_KEY);
    }

    private void createPhenomenons(JsonNode features) {
        Set<String> phenomenons = new HashSet<>();
        features.forEach(n -> n.path(GeoJSONConstants.PROPERTIES_KEY)
                               .path(JSONConstants.PHENOMENONS_KEY)
                               .fieldNames()
                               .forEachRemaining(phenomenons::add));

        // create the phenomenons for the features
        phenomenons.forEach(phenomenon -> createPhenomenon(phenomenon, phenomenon));

    }

    public static class KafkaJacksonDeserializer<T> implements Deserializer<T> {
        private final Class<T> type;
        private final ObjectMapper objectMapper;

        public KafkaJacksonDeserializer(Class<T> type, ObjectMapper objectMapper) {
            this.type = Objects.requireNonNull(type);
            this.objectMapper = Objects.requireNonNull(objectMapper);
        }

        @Override
        public T deserialize(String s, byte[] bytes) {
            if (bytes == null) {
                return null;
            }
            try {
                return this.objectMapper.readValue(bytes, this.type);
            } catch (IOException e) {
                throw new SerializationException("Error reading " + this.type, e);
            }
        }
    }
}
