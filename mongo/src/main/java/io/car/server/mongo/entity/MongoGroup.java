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


import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.google.common.base.Objects;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("groups")
public class MongoGroup extends MongoGroupBase implements Group {
    @Reference(value = OWNER, lazy = true)
    private MongoUser owner;

    @Override
    public void setOwner(User user) {
        this.owner = (MongoUser) user;
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
                .toString();
    }
}
