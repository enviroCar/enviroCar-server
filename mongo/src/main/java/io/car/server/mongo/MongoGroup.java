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

import java.util.Set;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.google.common.base.Objects;

import io.car.server.core.Group;
import io.car.server.core.User;
import io.car.server.core.Users;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Entity("userGroups")
public class MongoGroup extends MongoBaseEntity implements Group {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "desc";
    public static final String MEMBERS = "members";
    public static final String OWNER = "owner";
    @Indexed(unique = true)
    @Property(NAME)
    private String name;
    @Property(DESCRIPTION)
    private String description;
    @Reference(value = MEMBERS, lazy = true)
    private Set<MongoUser> members;
    @Reference(value = OWNER, lazy = true)
    private MongoUser owner;

    @Override
    public MongoGroup setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MongoGroup setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Users getMembers() {
        return new Users(this.members);
    }

    @Override
    public MongoGroup addMember(User user) {
        this.members.add((MongoUser) user);
        return this;
    }

    @Override
    public MongoGroup removeMember(User user) {
        this.members.remove((MongoUser) user);
        return this;
    }

    @Override
    public MongoGroup setOwner(User user) {
        this.owner = (MongoUser) user;
        return this;
    }

    @Override
    public MongoUser getOwner() {
        return this.owner;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add(ID, getId())
                .add(NAME, getName())
                .add(DESCRIPTION, getDescription())
                .add(OWNER, getOwner())
                .add(MEMBERS, getMembers())
                .toString();
    }

}
