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

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.google.inject.Inject;

import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.mongo.cache.EntityCache;

@Entity("tracks")
public class MongoTrack extends MongoTrackBase implements Track {
    @Property(USER)
    private Key<MongoUser> user;
    @Property(SENSOR)
    private Key<MongoSensor> sensor;
    @Inject
    @Transient
    private EntityCache<MongoSensor> sensorCache;
    @Inject
    @Transient
    private EntityCache<MongoUser> userCache;

    @Override
    public MongoUser getUser() {
        if (user == null) {
            return null;
        }
        return this.userCache.get(user);
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
    public MongoSensor getSensor() {
        return this.sensorCache.get(sensor);
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
}
