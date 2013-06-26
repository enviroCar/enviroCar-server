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

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Property;
import com.google.common.base.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Embedded
public class MongoStatisticKey {
    public static final String ALL = "all";
    public static final String TRACK = "track";
    public static final String USER = "user";
    public static final String SENSOR = "sensor";
    @Property(TRACK)
    private Key<MongoTrack> track;
    @Property(USER)
    private Key<MongoUser> user;
    @Property(SENSOR)
    private Key<MongoSensor> sensor;
    @Property(ALL)
    private boolean all = true;

    public MongoStatisticKey(Key<MongoTrack> track,
                             Key<MongoUser> user,
                             Key<MongoSensor> sensor) {
        this.track = track;
        this.user = user;
        this.sensor = sensor;
        all = track == null && user == null && sensor == null;
    }

    public MongoStatisticKey() {
        this(null, null, null);
    }

    public Key<MongoTrack> getTrack() {
        return track;
    }

    public void setTrack(Key<MongoTrack> track) {
        this.track = track;
        this.all = track == null && user == null && sensor == null;
    }

    public Key<MongoUser> getUser() {
        return user;
    }

    public void setUser(Key<MongoUser> user) {
        this.user = user;
        this.all = track == null && user == null && sensor == null;
    }

    public Key<MongoSensor> getSensor() {
        return sensor;
    }

    public void setSensor(Key<MongoSensor> sensor) {
        this.sensor = sensor;
        this.all = track == null && user == null && sensor == null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.track, this.user, this.sensor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoStatisticKey other = (MongoStatisticKey) obj;
        return Objects.equal(this.track, other.track) &&
               Objects.equal(this.user, other.user) &&
               Objects.equal(this.sensor, other.sensor);

    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
        if (all) {
            this.user = null;
            this.track = null;
            this.sensor = null;
        }
    }
}
