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
package org.envirocar.server.mongo.entity;

import com.google.common.base.Objects;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;

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
    @Reference(value = TRACK, lazy = true)
    private MongoTrack track;
    @Reference(value = USER, lazy = true)
    private MongoUser user;
    @Reference(value = SENSOR, lazy = true)
    private MongoSensor sensor;
    @Property(ALL)
    private boolean all;

    public MongoStatisticKey(Track track, User user, Sensor sensor) {
        this.track = (MongoTrack) track;
        this.user = (MongoUser) user;
        this.sensor = (MongoSensor) sensor;
        all = track == null && user == null && sensor == null;
    }

    public MongoStatisticKey() {
        this(null, null, null);
    }

    public MongoTrack getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = (MongoTrack) track;
        this.all = track == null && user == null && sensor == null;
    }

    public MongoUser getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = (MongoUser) user;
        this.all = track == null && user == null && sensor == null;
    }

    public MongoSensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = (MongoSensor) sensor;
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

    @Override
    public String toString() {
        return String.format("MongoStatisticKey [user=%s, track=%s, sensor=%s]", this.user, this.track, this.sensor);
    }

}
