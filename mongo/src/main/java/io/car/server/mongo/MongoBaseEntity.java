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
package io.car.server.mongo;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.PrePersist;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;

import io.car.server.core.BaseEntity;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoBaseEntity implements BaseEntity {
    public static final String ID = Mapper.ID_KEY;
    public static final String CREATION_DATE = "created";
    public static final String LAST_MODIFIED = "modified";
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

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public DateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(DateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
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

    public void setId(ObjectId id) {
        this.id = id;
    }
}
