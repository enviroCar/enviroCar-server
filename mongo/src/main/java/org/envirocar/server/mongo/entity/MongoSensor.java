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

import java.util.Map;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import org.envirocar.server.core.entities.Sensor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("sensors")
public class MongoSensor extends MongoEntityBase implements Sensor {
    public static final String NAME = Mapper.ID_KEY;
    public static final String TYPE = "type";
    public static final String PROPERTIES = "properties";
    @Id
    private ObjectId id = new ObjectId();
    @Property(TYPE)
    private String type;
    @Property(PROPERTIES)
    private Map<String, Object> properties = Maps.newHashMap();

    @Override
    public String toString() {
        return toStringHelper()
                .add(NAME, id).toString();
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
        final MongoSensor other = (MongoSensor) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public void addProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    @Override
    public boolean hasType() {
        return getType() != null && !getType().isEmpty();
    }

    @Override
    public boolean hasProperties() {
        return properties.size() > 0;
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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
