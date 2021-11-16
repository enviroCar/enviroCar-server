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
import org.envirocar.server.core.entities.Track;

import javax.inject.Named;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

public final class KafkaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Serializer<Track>>() {}).to(TrackSerializer.class);
        bind(new TypeLiteral<Serializer<String>>() {}).to(StringSerializer.class);
        bind(KafkaListener.class).asEagerSingleton();
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
    @Named(KafkaConstants.KAFKA_TOPIC)
    public String topic(Properties properties) {
        return getProperty(properties, KafkaConstants.KAFKA_TOPIC, "tracks");
    }

    @Provides
    public Producer<String, Track> createProducer(Serializer<String> keySerializer,
                                                  Serializer<Track> valueSerializer,
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
