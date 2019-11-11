/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.container;

import org.testcontainers.containers.GenericContainer;

public class KafkaContainer extends GenericContainer<KafkaContainer> {

    private int id = 1;
    private String zookeeper;

    public KafkaContainer() {
        super("confluentinc/cp-kafka:latest");
    }

    public KafkaContainer withZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
        return this;
    }

    public KafkaContainer withId(int id) {
        this.id = id;
        return this;
    }

    @Override
    protected void configure() {
        withExposedPorts(9092);
        withEnv("KAFKA_BROKER_ID", String.valueOf(id));
        withEnv("KAFKA_ZOOKEEPER_CONNECT", zookeeper);
        withEnv("KAFKA_ADVERTISED_LISTENERS", "LISTENER_DOCKER_INTERNAL://localhost:19092,LISTENER_DOCKER_EXTERNAL://127.0.0.1:9092");
        withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "LISTENER_DOCKER_INTERNAL:PLAINTEXT,LISTENER_DOCKER_EXTERNAL:PLAINTEXT");
        withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "LISTENER_DOCKER_INTERNAL");
        withEnv("KAFKA_LOG4J_LOGGERS", "kafka.controller=INFO,kafka.producer.async.DefaultEventHandler=INFO,state.change.logger=INFO");
        withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1");
        withEnv("KAFKA_MESSAGE_MAX_BYTES", "5048576");
        withEnv("KAFKA_MAX_REQUEST_SIZE", "5048576");
        withEnv("KAFKA_MAX_PARTITION_FETCH_BYTES", "5048576");
        super.configure();
    }

}
