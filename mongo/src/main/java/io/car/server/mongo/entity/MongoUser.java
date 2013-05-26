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

import static io.car.server.mongo.entity.MongoBaseEntity.ID;

import java.util.Set;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("users")
public class MongoUser extends MongoBaseEntity<MongoUser> implements User {
    @Indexed(unique = true)
    @Property(NAME)
    private String name;
    @Indexed(unique = true)
    @Property(MAIL)
    private String mail;
    @Property(TOKEN)
    private String token;
    @Property(IS_ADMIN)
    private boolean isAdmin = false;
    @Reference(value = FRIENDS, lazy = true)
    private Set<MongoUser> friends = Sets.newHashSet();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MongoUser setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public MongoUser setMail(String mail) {
        this.mail = mail;
        return this;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public MongoUser setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public MongoUser setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    @Override
    public Users getFriends() {
        return Users.from(this.friends).build();
    }

    @Override
    public MongoUser addFriend(User user) {
        this.friends.add((MongoUser) user);
        return this;
    }

    @Override
    public MongoUser removeFriend(User user) {
        this.friends.remove((MongoUser) user);
        return this;
    }

    public MongoUser setFriends(Users friends) {
        this.friends.clear();
        for (User u : friends) {
            this.friends.add((MongoUser) u);
        }
        return this;
    }

    @Override
    public boolean hasFriend(User user) {
        return this.friends.contains((MongoUser) user);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add(ID, getId())
                .add(NAME, getName())
                .add(MAIL, getMail())
                .add(TOKEN, getToken())
                .add(IS_ADMIN, isAdmin())
                .add(FRIENDS, getFriends())
                .toString();
    }
}
