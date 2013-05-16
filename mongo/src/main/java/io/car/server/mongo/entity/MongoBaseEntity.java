/**
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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.PrePersist;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;

import io.car.server.core.entities.BaseEntity;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoBaseEntity<T> implements BaseEntity {
    public static final String ID = Mapper.ID_KEY;
    @Id
    private ObjectId id = new ObjectId();
    @Indexed
    @Property(CREATION_DATE)
    private DateTime creationDate;
    @Indexed
    @Property(LAST_MODIFIED)
    private DateTime lastModificationDate;

    @Override
    public DateTime getCreationDate() {
        return creationDate;
    }

    @SuppressWarnings("unchecked")
    public T setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
        return (T) this;
    }

    @Override
    public DateTime getLastModificationDate() {
        return lastModificationDate;
    }

    @SuppressWarnings("unchecked")
    public T setLastModificationDate(DateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
        return (T) this;
    }

    @PrePersist
    public void prePersist() {
        DateTime now = new DateTime(DateTimeZone.UTC);
        if (getCreationDate() == null) {
            setCreationDate(now);
        }
        setLastModificationDate(now);
    }

    public ObjectId getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    public T setId(ObjectId id) {
        this.id = id;
        return (T) this;
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
        final MongoBaseEntity<?> other = (MongoBaseEntity) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).omitNullValues().add(ID, getId()).toString();
    }
}
