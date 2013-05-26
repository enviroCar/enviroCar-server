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

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

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
public class MongoMeasurement extends MongoEntityBase<MongoMeasurement>
        implements Measurement {
    @Indexed
    @Property(USER)
    private Key<MongoUser> user;
//    @Indexed(IndexDirection.GEO2DSPHERE)
    @Property(GEOMETRY)
    private Geometry geometry;
    @Property(SENSOR)
    private Key<MongoSensor> sensor;
    @Embedded(PHENOMENONS)
    private Set<MongoMeasurementValue> values = Sets.newHashSet();
    @Indexed
    @Property(TIME)
    private DateTime time;
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
    public Geometry getGeometry() {
        return this.geometry;
    }

    @Override
    public MongoMeasurement setGeometry(Geometry location) {
        this.geometry = location;
        return this;
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
    public Measurement setUser(User user) {
        if (user != null) {
            this.user = ((MongoUser) user).asKey();
        } else {
            this.user = null;
        }
        return this;
    }

    @Override
    public Measurement addValue(MeasurementValue value) {
        this.values.add((MongoMeasurementValue) value);
        return this;
    }

    @Override
    public Measurement removeValue(MeasurementValue value) {
        this.values.remove((MongoMeasurementValue) value);
        return this;
    }

    @Override
    public MongoSensor getSensor() {
        return sensorCache.get(sensor);
    }

    @Override
    public MongoMeasurement setSensor(Sensor sensor) {
        if (sensor != null) {
            this.sensor = ((MongoSensor) sensor).asKey();
        } else {
            this.sensor = null;
        }
        return this;
    }

    @Override
    public String getIdentifier() {
        return getId().toString();
    }

    @Override
    public MongoMeasurement setIdentifier(String identifier) {
        return setId(new ObjectId(identifier));
    }

    @Override
    public DateTime getTime() {
        return this.time;
    }

    @Override
    public MongoMeasurement setTime(DateTime time) {
        this.time = time;
        return this;
    }

    @Override
    public MongoMeasurement setTrack(Track track) {
        this.track = (MongoTrack) track;
        return this;
    }

    @Override
    public MongoTrack getTrack() {
        return this.track;
    }
}
