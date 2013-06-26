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
package org.envirocar.server.mongo.activities;

import org.bson.types.ObjectId;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.activities.ActivityType;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoUser;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Polymorphic;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.github.jmkgreen.morphia.utils.IndexDirection;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Polymorphic
@Entity("activities")
public class MongoActivity implements Activity {
    public static final String ID = Mapper.ID_KEY;
    public static final String USER = "user";
    public static final String TYPE = "type";
    public static final String TIME = "time";
    @Id
    private ObjectId id = new ObjectId();
    @Indexed(IndexDirection.DESC)
    @Property(TIME)
    private DateTime time = new DateTime();
    @Indexed
    @Property(USER)
    private Key<MongoUser> user;
    @Transient
    private MongoUser _user;
    @Indexed
    @Property(TYPE)
    private ActivityType type;
    @Inject
    @Transient
    private MongoDB mongoDB;

    @AssistedInject
    public MongoActivity(MongoDB mongoDB,
                         @Assisted User user,
                         @Assisted ActivityType type) {
        this.mongoDB = mongoDB;
        this._user = (MongoUser) user;
        this.user = mongoDB.key(this._user);
        this.type = type;
    }

    @Inject
    public MongoActivity(MongoDB mongoDB) {
        this(mongoDB, null, null);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public boolean hasId() {
        return getId() != null;
    }

    @Override
    public MongoUser getUser() {
        if (this._user == null) {
            this._user = getMongoDB().deref(MongoUser.class, this.user);
        }
        return this._user;
    }

    public void setUser(User user) {
        this._user = (MongoUser) user;
        this.user = getMongoDB().key(this._user);
    }

    @Override
    public ActivityType getType() {
        return this.type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    @Override
    public DateTime getTime() {
        return this.time;
    }

    @Override
    public boolean hasUser() {
        return getUser() != null;
    }

    @Override
    public boolean hasType() {
        return getType() != null;
    }

    @Override
    public boolean hasTime() {
        return getTime() != null;
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
        final MongoActivity other = (MongoActivity) obj;
        return Objects.equal(this.id, other.getId());
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    protected ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add(ID, this.id)
                .add(TYPE, this.type)
                .add(USER, this.user);
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    @Override
    public String getIdentifier() {
        return getId().toString();
    }
}
