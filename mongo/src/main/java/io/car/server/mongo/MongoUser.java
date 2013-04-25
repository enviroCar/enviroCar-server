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

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
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
    @Id
    private String name;
    @Indexed(unique = true)
    private String mail;
    private String token;

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
}
