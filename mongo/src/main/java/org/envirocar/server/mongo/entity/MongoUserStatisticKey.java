/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.google.common.base.Objects;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Reference;
import org.envirocar.server.core.entities.User;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
@Embedded
public class MongoUserStatisticKey {
    public static final String USER = "user";
    @Reference(value = USER, lazy = true)
    private MongoUser user;

    public MongoUserStatisticKey(User user) {
        this.user = (MongoUser) user;
    }

    public MongoUserStatisticKey() {
        this(null);
    }

    public MongoUser getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = (MongoUser) user;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.user);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoUserStatisticKey other = (MongoUserStatisticKey) obj;
        return Objects.equal(this.user, other.user);
    }

    @Override
    public String toString() {
        return String.format("MongoUserStatisticKey [user=%s]", this.user);
    }

}
