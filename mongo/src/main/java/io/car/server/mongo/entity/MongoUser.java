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
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;

import io.car.server.core.entities.User;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("users")
public class MongoUser extends MongoEntityBase implements User {
    public static final String TOKEN = "token";
    public static final String IS_ADMIN = "isAdmin";
    public static final String NAME = Mapper.ID_KEY;
    public static final String MAIL = "mail";
    public static final String FRIENDS = "friends";
    @Property(TOKEN)
    private String token;
    @Property(IS_ADMIN)
    private boolean isAdmin = false;
    @Id
    private String name;
    @Indexed(unique = true)
    @Property(MAIL)
    private String mail;
    @Property(FRIENDS)
    private Set<Key<MongoUser>> friends;

    public MongoUser(String name) {
        this.name = name;
    }

    public MongoUser() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    @Override
    public boolean hasMail() {
        return getMail() != null && !getMail().isEmpty();
    }

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
    public String toString() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add(NAME, getName())
                .add(MAIL, getMail())
                .add(IS_ADMIN, isAdmin())
                .toString();
    }

    @Override
    public boolean hasToken() {
        return getToken() != null && !getToken().isEmpty();
    }

    public Set<Key<MongoUser>> getFriends() {
        return friends == null ? null : Collections.unmodifiableSet(friends);
    }
}
