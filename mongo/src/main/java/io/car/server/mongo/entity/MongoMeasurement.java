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
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.mapping.Mapper;
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
public class MongoMeasurement extends MongoEntityBase implements Measurement {
    public static final String IDENTIFIER = Mapper.ID_KEY;
    public static final String PHENOMENONS = "phenomenons";
    public static final String USER = "user";
    public static final String SENSOR = "sensor";
    public static final String TRACK = "track";
    public static final String GEOMETRY = "geometry";
    public static final String TIME = "time";
    @Id
    private ObjectId id = new ObjectId();
    @Property(GEOMETRY)
    private Geometry geometry;
    @Indexed
    @Property(TIME)
    private DateTime time;
    @Indexed
    @Property(USER)
    private Key<MongoUser> user;
    @Embedded(SENSOR)
    private MongoSensor sensor;
    @Indexed
    @Property(TRACK)
    private Key<MongoTrack> track;
    @Embedded(PHENOMENONS)
    private Set<MongoMeasurementValue> values = Sets.newHashSet();
    @Transient
    private MongoTrack _track;
    @Transient
    private MongoUser _user;

    @Override
    public Geometry getGeometry() {
        return this.geometry;
    }

    @Override
    public void setGeometry(Geometry location) {
        this.geometry = location;
    }

    @Override
    public String getIdentifier() {
        return this.id == null ? null : this.id.toString();
    }

    @Override
    public void setIdentifier(String identifier) {
        this.id = id == null ? null : new ObjectId(identifier);
    }

    @Override
    public DateTime getTime() {
        return this.time;
    }

    @Override
    public void setTime(DateTime time) {
        this.time = time;
    }

    @Override
    public boolean hasGeometry() {
        return getGeometry() != null;
    }

    @Override
    public boolean hasIdentifier() {
        return getIdentifier() != null;
    }

    @Override
    public boolean hasTime() {
        return getTime() != null;
    }

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
        if (this._user == null) {
            this._user = getMongoDB().dereference(MongoUser.class, this.user);
        }
        return this._user;
    }

    @Override
    public void setUser(User user) {
        this._user = (MongoUser) user;
        this.user = getMongoDB().reference(this._user);
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
        return this.sensor;
    }

    @Override
    public void setSensor(Sensor sensor) {
        this.sensor = (MongoSensor) sensor;
    }

    @Override
    public void setTrack(Track track) {
        this._track = (MongoTrack) track;
        this.track = getMongoDB().reference(this._track);
    }

    @Override
    public MongoTrack getTrack() {
        if (this._track == null) {
            this._track = getMongoDB().dereference(MongoTrack.class, this.track);
        }
        return this._track;
    }

    @Override
    public boolean hasUser() {
        return getUser() != null;
    }

    @Override
    public boolean hasSensor() {
        return getSensor() != null;
    }

    @Override
    public boolean hasTrack() {
        return getTrack() != null;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
