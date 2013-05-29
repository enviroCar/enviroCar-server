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

import static io.car.server.core.entities.User.FRIENDS;
import static io.car.server.mongo.entity.MongoEntity.ID;

import java.util.Set;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("users")
public class MongoUser extends MongoUserBase implements User {
    @Property(TOKEN)
    private String token;
    @Property(IS_ADMIN)
    private boolean isAdmin = false;
    @Reference(value = FRIENDS, lazy = true)
    private Set<MongoUser> friends = Sets.newHashSet();
    @Reference(value = GROUPS, lazy = true)
    private Set<MongoGroup> groups = Sets.newHashSet();

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public Users getFriends() {
        return Users.from(this.friends).build();
    }

    @Override
    public void addFriend(User user) {
        this.friends.add((MongoUser) user);
    }

    @Override
    public void removeFriend(User user) {
        this.friends.remove((MongoUser) user);
    }

    @Override
    public boolean hasFriend(User user) {
        return this.friends.contains((MongoUser) user);
    }

    @Override
    public Groups getGroups() {
        return Groups.from(groups).build();
    }

    @Override
    public void addGroup(Group group) {
        this.groups.add((MongoGroup) group);
    }

    @Override
    public void removeGroup(Group group) {
        this.groups.remove((MongoGroup) group);
    }

    @Override
    public boolean hasGroup(Group group) {
        return this.groups.contains((MongoGroup) group);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add(ID, getId())
                .add(NAME, getName())
                .add(MAIL, getMail())
                .add(IS_ADMIN, isAdmin())
                .add(FRIENDS, getFriends())
                .toString();
    }
}
