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

public interface KafkaConstants {
    String KAFKA_MEASUREMENT_TOPIC = "kafka.topic.measurement";
    String KAFKA_TRACK_TOPIC = "kafka.topic.track";
    String KAFKA_BROKERS = "kafka.brokers";
    String KAFKA_CLIENT_ID = "kafka.clientId";
    String KAFKA_DVFO_GEOFENCE = "kafka.dvfo.geofence";
    String KAFKA_DVFO_TOPIC = "kafka.dvfo.topic.measurements";
}
