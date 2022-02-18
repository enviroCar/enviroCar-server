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
package org.envirocar.server.event.kafka;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Track;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import javax.inject.Named;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

public final class KafkaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Serializer<Track>>() {}).to(TrackSerializer.class);
        bind(new TypeLiteral<Serializer<Measurement>>() {}).to(MeasurementSerializer.class);
        bind(new TypeLiteral<Serializer<String>>() {}).to(StringSerializer.class);
        bind(KafkaTrackListener.class).asEagerSingleton();
        bind(KafkaMeasurementListener.class).asEagerSingleton();
        bind(GeofenceKafkaMeasurementListener.class).asEagerSingleton();
    }

    @Provides
    public Map<String, Geometry> geofences(Properties properties) throws ParseException {
        Map<String, Geometry> geofences = new HashMap<>(1);
        String wkt = getProperty(properties, KafkaConstants.KAFKA_DVFO_GEOFENCE, null);
        String topic = getProperty(properties, KafkaConstants.KAFKA_DVFO_TOPIC, "dvfo_measurements");
        if (wkt != null) {
            try (StringReader reader = new StringReader(wkt)) {
                Geometry geofence = new WKTReader().read(reader);
                geofences.put(topic, geofence);
            }
        }
        return Collections.unmodifiableMap(geofences);
    }

    @Provides
    @Named(KafkaConstants.KAFKA_CLIENT_ID)
    public String clientId(Properties properties) {
        return getProperty(properties, KafkaConstants.KAFKA_CLIENT_ID,
                           String.format("enviroCar-server-%s", UUID.randomUUID()));
    }

    @Provides
    @Named(KafkaConstants.KAFKA_BROKERS)
    public String brokers(Properties properties) {
        return getProperty(properties, KafkaConstants.KAFKA_BROKERS, "processing.envirocar.org:9092");
    }

    @Provides
    @Named(KafkaConstants.KAFKA_TRACK_TOPIC)
    public String tracksTopic(Properties properties) {
        return getProperty(properties, KafkaConstants.KAFKA_TRACK_TOPIC, "tracks");
    }

    @Provides
    @Named(KafkaConstants.KAFKA_MEASUREMENT_TOPIC)
    public String measurementsTopic(Properties properties) {
        return getProperty(properties, KafkaConstants.KAFKA_MEASUREMENT_TOPIC, "measurements");
    }

    @Provides
    public Producer<String, Track> createTrackProducer(
            Serializer<String> keySerializer,
            Serializer<Track> valueSerializer,
            @Named(KafkaConstants.KAFKA_BROKERS) String brokers,
            @Named(KafkaConstants.KAFKA_CLIENT_ID) String clientId) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 157286400); // 150MB
        return new KafkaProducer<>(props, keySerializer, valueSerializer);
    }

    @Provides
    public Producer<String, Measurement> createMeasurementProducer(
            Serializer<String> keySerializer,
            Serializer<Measurement> valueSerializer,
            @Named(KafkaConstants.KAFKA_BROKERS) String brokers,
            @Named(KafkaConstants.KAFKA_CLIENT_ID) String clientId) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 157286400); // 150MB
        return new KafkaProducer<>(props, keySerializer, valueSerializer);
    }

    private String getProperty(Properties properties, String key, String defaultValue) {
        Optional<String> property = getOptional(System.getenv(key));

        if (!property.isPresent()) {
            property = getOptional(System.getProperty(key));
        }
        if (!property.isPresent()) {
            property = getOptional(properties.getProperty(key));
        }

        return property.orElse(defaultValue);
    }

    private Optional<String> getOptional(String property) {
        return Optional.ofNullable(property).filter(x -> !x.isEmpty());
    }
}

