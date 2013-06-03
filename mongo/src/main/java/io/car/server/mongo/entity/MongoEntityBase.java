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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.PrePersist;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.google.inject.Inject;

import io.car.server.core.entities.BaseEntity;
import io.car.server.mongo.MongoDB;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class MongoEntityBase implements BaseEntity {
    public static final String CREATION_DATE = "created";
    public static final String LAST_MODIFIED = "modified";
    @Transient
    private MongoDB mongoDB;
    @Indexed
    @Property(CREATION_DATE)
    private DateTime creationTime;
    @Indexed
    @Property(LAST_MODIFIED)
    private DateTime modificationTime;

    @Override
    public DateTime getCreationTime() {
        return creationTime;
    }

    @SuppressWarnings("unchecked")
    public void setCreationTime(DateTime creationDate) {
        this.creationTime = creationDate;
    }

    @Override
    public DateTime getModificationTime() {
        return modificationTime;
    }

    @SuppressWarnings("unchecked")
    public void setModificationTime(DateTime lastModificationDate) {
        this.modificationTime = lastModificationDate;
    }

    @PrePersist
    public void prePersist() {
        DateTime now = new DateTime(DateTimeZone.UTC);
        if (getCreationTime() == null) {
            setCreationTime(now);
        }
        setModificationTime(now);
    }

    @Override
    public boolean hasCreationTime() {
        return getCreationTime() != null;
    }

    @Override
    public boolean hasModificationTime() {
        return getModificationTime() != null;
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    @Inject
    public void setMongoDB(MongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }
}
