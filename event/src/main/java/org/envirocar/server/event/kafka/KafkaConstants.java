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
package org.envirocar.server.event.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;

public interface KafkaConstants {
    String KAFKA_MEASUREMENT_TOPIC = "enviroCar.kafka.topic.measurements";
    String KAFKA_TRACK_TOPIC = "enviroCar.kafka.topic.tracks";
    String KAFKA_BOOTSTRAP_SERVERS = "kafka." + ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
    String KAFKA_CLIENT_ID = "kafka." + ProducerConfig.CLIENT_ID_CONFIG;
}
