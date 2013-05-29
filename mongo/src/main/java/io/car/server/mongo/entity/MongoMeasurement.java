/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.mongo.entity;

import java.util.Set;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.MeasurementValue;
import io.car.server.core.entities.MeasurementValues;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.mongo.cache.EntityCache;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
@Entity("measurements")
public class MongoMeasurement extends MongoMeasurementBase
        implements Measurement {
    @Indexed
    @Property(USER)
    private Key<MongoUser> user;
    @Embedded(SENSOR)
    private Key<MongoSensor> sensor;
    @Embedded(PHENOMENONS)
    private Set<MongoMeasurementValue> values = Sets.newHashSet();
    @Indexed
    @Reference(value = TRACK, lazy = true)
    private MongoTrack track;
    @Inject
    @Transient
    private EntityCache<MongoUser> userCache;
    @Inject
    @Transient
    private EntityCache<MongoSensor> sensorCache;

    @Override
    public MeasurementValues getValues() {
        return MeasurementValues.from(this.values).build();
    }

    @Override
    public int compareTo(Measurement o) {
        return this.getTime().compareTo(o.getTime());
    }

    @Override
    public MongoUser getUser() {
        return userCache.get(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setUser(User user) {
        if (user != null) {
            this.user = (Key<MongoUser>) ((MongoUser) user).asKey();
        } else {
            this.user = null;
        }
    }

    @Override
    public void addValue(MeasurementValue value) {
        this.values.add((MongoMeasurementValue) value);
    }

    @Override
    public void removeValue(MeasurementValue value) {
        this.values.remove((MongoMeasurementValue) value);
    }

    @Override
    public MongoSensor getSensor() {
        return sensorCache.get(sensor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSensor(Sensor sensor) {
        if (sensor != null) {
            this.sensor = (Key<MongoSensor>) ((MongoSensor) sensor).asKey();
        } else {
            this.sensor = null;
        }
    }

    @Override
    public void setTrack(Track track) {
        this.track = (MongoTrack) track;
    }

    @Override
    public MongoTrack getTrack() {
        return this.track;
    }
}
