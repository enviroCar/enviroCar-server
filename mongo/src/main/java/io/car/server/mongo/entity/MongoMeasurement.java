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

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.MeasurementValue;
import io.car.server.core.entities.MeasurementValues;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
@Entity("measurements")
public class MongoMeasurement extends MongoBaseEntity<MongoMeasurement>
        implements Measurement {
    @Indexed
    @Reference(value = USER, lazy = true)
    private MongoUser user;
//    @Indexed(IndexDirection.GEO2DSPHERE)
    @Property(GEOMETRY)
    private Geometry geometry;
    @Reference(value = SENSOR, lazy = true)
    private MongoSensor sensor;
    @Embedded(PHENOMENONS)
    private Set<MongoMeasurementValue> values = Sets.newHashSet();
    @Indexed
    @Property(TIME)
    private DateTime time;
    @Indexed
    @Reference(value = TRACK, lazy = true)
    private MongoTrack track;

    @Override
    public MeasurementValues getValues() {
        return new MeasurementValues(this.values);
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
        return user;
    }

    @Override
    public Measurement setUser(User user) {
        this.user = (MongoUser) user;
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
        return sensor;
    }

    @Override
    public MongoMeasurement setSensor(Sensor sensor) {
        this.sensor = (MongoSensor) sensor;
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
