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

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

@Entity("tracks")
public class MongoTrack extends MongoEntityBase implements Track {
    public static final String USER = "user";
    public static final String SENSOR = "sensor";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String BBOX = "bbox";
    @Id
    private ObjectId id = new ObjectId();
    @Property(USER)
    private Key<MongoUser> user;
    @Transient
    private MongoUser _user;
    @Embedded(SENSOR)
    private MongoSensor sensor;
    @Property(NAME)
    private String name;
    @Property(DESCRIPTION)
    private String description;
    @Property(BBOX)
    private Geometry bbox;

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
    public MongoSensor getSensor() {
        return this.sensor;
    }

    @Override
    public void setSensor(Sensor sensor) {
        this.sensor = (MongoSensor) sensor;
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
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasName() {
        return getName() != null && !getDescription().isEmpty();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean hasDescription() {
        return getDescription() != null && !getDescription().isEmpty();
    }

    @Override
    public String getIdentifier() {
        return this.id == null ? null : this.id.toString();
    }

    @Override
    public void setIdentifier(String id) {
        this.id = id == null ? null : new ObjectId(id);
    }

    @Override
    public boolean hasIdentifier() {
        return this.id != null;
    }

    @Override
    public Geometry getBoundingBox() {
        return this.bbox;
    }

    @Override
    public void setBoundingBox(Geometry bbox) {
        this.bbox = bbox;
    }

    @Override
    public boolean hasBoundingBox() {
        return getBoundingBox() != null && !getBoundingBox().isEmpty();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
