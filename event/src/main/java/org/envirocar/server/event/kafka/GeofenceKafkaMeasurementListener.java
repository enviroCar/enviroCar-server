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

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.event.CreatedMeasurementEvent;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class GeofenceKafkaMeasurementListener {
    private static final Logger LOG = LoggerFactory.getLogger(GeofenceKafkaMeasurementListener.class);
    private final Map<String, Geometry> geofences;
    private final Producer<String, Measurement> producer;

    @Inject
    public GeofenceKafkaMeasurementListener(Producer<String, Measurement> producer,
                                            Map<String, Geometry> geofences) {
        this.producer = Objects.requireNonNull(producer);
        this.geofences = Objects.requireNonNull(geofences);
    }

    @Subscribe
    public void onCreatedMeasurementEvent(CreatedMeasurementEvent e) {
        this.geofences.forEach((topic, geofence) -> {
            if (geofence.intersects(e.getMeasurement().getGeometry())) {
                ProducerRecord<String, Measurement> record = new ProducerRecord<>(
                        topic, e.getMeasurement().getIdentifier(), e.getMeasurement());
                LOG.trace("Publishing measurement {} to kafka topic {}", record.key(), topic);
                this.producer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        LOG.error(String.format("Error publishing measurement %s to kafka topic %s",
                                                record.key(), topic), exception);
                    }
                });
            }
        });

    }

}
