/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.mongo.entity;

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
import com.github.jmkgreen.morphia.utils.IndexDirection;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.MeasurementValues;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;

/**
 * TODO JavaDoc
 *
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
    @Indexed(IndexDirection.ASC)
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
            this._user = getMongoDB().deref(MongoUser.class, this.user);
        }
        return this._user;
    }

    @Override
    public void setUser(User user) {
        this._user = (MongoUser) user;
        this.user = getMongoDB().key(this._user);
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
        this.track = getMongoDB().key(this._track);
    }

    @Override
    public MongoTrack getTrack() {
        if (this._track == null) {
            this._track = getMongoDB().deref(MongoTrack.class, this.track);
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

    @Override
    public String toString() {
        return toStringHelper()
                .add(IDENTIFIER, id)
                .add(TIME, time)
                .add(GEOMETRY, geometry)
                .add(USER, user)
                .add(SENSOR, sensor)
                .add(TRACK, track)
                .add(PHENOMENONS, values)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoMeasurement other = (MongoMeasurement) obj;
        return Objects.equal(this.id, other.id);
    }
}
