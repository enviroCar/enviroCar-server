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
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.utils.IndexDirection;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.mongo.cache.EntityCache;

@Entity("tracks")
public class MongoTrack extends MongoEntityBase<MongoTrack> implements Track {
    @Indexed(IndexDirection.GEO2D)
    @Embedded(BBOX)
    private Geometry bbox;
    @Property(USER)
    private Key<MongoUser> user;
    @Property(SENSOR)
    private Key<MongoSensor> sensor;
    @Property(DESCIPTION)
    private String description;
    @Property(NAME)
    private String name;
    @Inject
    @Transient
    private EntityCache<MongoSensor> sensorCache;
    @Inject
    @Transient
    private EntityCache<MongoUser> userCache;
    @Inject
    @Transient
    private GeometryFactory factory;


    @Override
    public MongoTrack setBbox(Geometry bbox) {
        this.bbox = bbox;
        return this;
    }

    @Override
    public Geometry getBbox() {
        return this.bbox;
    }

    @Override
    public MongoTrack setBbox(double minx, double miny, double maxx, double maxy) {
        Coordinate[] coords = new Coordinate[] { new Coordinate(minx, miny),
                                                 new Coordinate(maxx, maxy) };
        this.bbox = factory.createPolygon(coords);
        return this;
    }

    @Override
    public String getIdentifier() {
        return (getId() == null) ? null : getId().toString();
    }

    @Override
    public MongoTrack setIdentifier(String id) {
        return setId(new ObjectId(id));
    }

    @Override
    public MongoUser getUser() {
        if (user == null) {
            return null;
        }
        return this.userCache.get(user);
    }

    @Override
    public MongoTrack setUser(User user) {
        if (user != null) {
            this.user = ((MongoUser) user).asKey();
        } else {
            this.user = null;
        }
        return this;
    }

    @Override
    public MongoSensor getSensor() {
        return this.sensorCache.get(sensor);
    }

    @Override
    public MongoTrack setSensor(Sensor sensor) {
        if (sensor != null) {
            this.sensor = ((MongoSensor) sensor).asKey();
        } else {
            this.sensor = null;
        }
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MongoTrack setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public MongoTrack setDescription(String description) {
        this.description = description;
        return this;
    }
}
