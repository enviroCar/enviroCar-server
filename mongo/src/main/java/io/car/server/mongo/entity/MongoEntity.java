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
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;
import com.google.inject.Inject;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoEntity {
    public static final String ID = Mapper.ID_KEY;
    @Id
    private ObjectId id = new ObjectId();
    @Inject
    @Transient
    private Mapper mapr;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoEntityBase other = (MongoEntityBase) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add(ID, getId())
                .toString();
    }

    public Key<?> asKey() {
        if (this.id == null) {
            return null;
        } else {
            return mapr.getKey(this);
        }
    }
}
