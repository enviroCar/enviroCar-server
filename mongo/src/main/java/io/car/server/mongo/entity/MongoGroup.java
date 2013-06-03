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

import java.util.Collections;
import java.util.Set;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.collect.Sets;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;

/**
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
    private Set<Key<MongoUser>> members = Sets.newHashSet();
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
        this.owner = getMongoDB().reference(this._owner);

    }

    @Override
    public MongoUser getOwner() {
        if (this._owner == null) {
            this._owner = getMongoDB().dereference(MongoUser.class, this.owner);
        }
        return this._owner;
    }

    @Override
    public boolean hasOwner() {
        return getOwner() != null;
    }

    public Set<Key<MongoUser>> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public void setMembers(Set<Key<MongoUser>> members) {
        this.members = members;
    }
}
