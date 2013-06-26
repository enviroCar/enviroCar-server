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

import java.util.Collections;
import java.util.Set;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("groups")
public class MongoGroup extends MongoEntityBase implements Group {
    public static final String OWNER = "owner";
    public static final String NAME = Mapper.ID_KEY;
    public static final String DESCRIPTION = "desc";
    public static final String MEMBERS = "members";
    @Property(OWNER)
    private Key<MongoUser> owner;
    @Transient
    private MongoUser _owner;
    @Property(MEMBERS)
    private Set<Key<MongoUser>> members;
    @Id
    private String name;
    @Property(DESCRIPTION)
    private String description;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    @Override
    public boolean hasDescription() {
        return getDescription() != null && !getDescription().isEmpty();
    }

    @Override
    public void setOwner(User user) {
        this._owner = (MongoUser) user;
        this.owner = getMongoDB().key(this._owner);

    }

    @Override
    public MongoUser getOwner() {
        if (this._owner == null) {
            this._owner = getMongoDB().deref(MongoUser.class, this.owner);
        }
        return this._owner;
    }

    @Override
    public boolean hasOwner() {
        return getOwner() != null;
    }

    public Set<Key<MongoUser>> getMembers() {
        return members == null ? null : Collections.unmodifiableSet(members);
    }

    @Override
    public String toString() {
        return toStringHelper()
                .add(NAME, name)
                .add(OWNER, owner)
                .add(DESCRIPTION, description)
                .add(MEMBERS, members).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoGroup other = (MongoGroup) obj;
        return Objects.equal(this.name, other.name);
    }
}
