/*
 * Copyright (C) 2013-2018 The enviroCar project
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

public final class KafkaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Serializer<Track>>() {}).to(TrackSerializer.class);
        bind(new TypeLiteral<Serializer<String>>() {}).to(StringSerializer.class);
        bind(KafkaListener.class).asEagerSingleton();
    }

    @Provides
    @Named(KafkaConstants.KAFKA_CLIENT_ID)
    public String clientId() {
        return Optional.ofNullable(System.getenv(KafkaConstants.KAFKA_CLIENT_ID))
                       .filter(x -> !x.isEmpty())
                       .orElse("enviroCar-server");
    }

    @Provides
    @Named(KafkaConstants.KAFKA_BROKERS)
    public String brokers() {
        return System.getenv(KafkaConstants.KAFKA_BROKERS);
    }

    @Provides
    @Named(KafkaConstants.KAFKA_TOPIC)
    public String topic() {
        return Optional.ofNullable(System.getenv(KafkaConstants.KAFKA_TOPIC))
                       .filter(x -> !x.isEmpty())
                       .orElse("tracks");
    }

    @Provides
    public Producer<String, Track> createProducer(Serializer<String> keySerializer,
                                                  Serializer<Track> valueSerializer,
                                                  @Named(KafkaConstants.KAFKA_BROKERS) String brokers,
                                                  @Named(KafkaConstants.KAFKA_BROKERS) String clientId,
                                                  Properties properties) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return new KafkaProducer<>(props, keySerializer, valueSerializer);
    }
}
