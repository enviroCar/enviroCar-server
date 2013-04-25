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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.PrePersist;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;

import io.car.server.core.User;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Entity("users")
public class MongoUser implements User {
    public static final String NAME = Mapper.ID_KEY;
    public static final String MAIL = "mail";
    public static final String TOKEN = "token";
    public static final String IS_ADMIN = "isAdmin";
    public static final String CREATION_DATE = "created";
    public static final String LAST_MODIFIED = "modified";
    @Id
    private String name;
    @Indexed(unique = true)
    @Property(MAIL)
    private String mail;
    @Property(TOKEN)
    private String token;
    @Property(IS_ADMIN)
    private boolean isAdmin = false;
    @Indexed
    @Property(CREATION_DATE)
    private DateTime creationDate;
    @Indexed
    @Property(LAST_MODIFIED)
    private DateTime lastModificationDate;

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
}
