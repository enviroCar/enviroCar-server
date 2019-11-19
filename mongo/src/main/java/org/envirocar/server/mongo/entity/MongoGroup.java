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
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import dev.morphia.mapping.experimental.MorphiaReference;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.mongo.util.Ref;

import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity(value = MongoGroup.COLLECTION, noClassnameStored = true)
public class MongoGroup extends MongoEntityBase implements Group {
    public static final String OWNER = "owner";
    public static final String NAME = "_id";
    public static final String DESCRIPTION = "desc";
    public static final String MEMBERS = "members";
    public static final String COLLECTION = "groups";
    @Id
    private String name;
    //@Reference(OWNER)
    private MorphiaReference<MongoUser> owner;
    //@Reference(MEMBERS)
    private MorphiaReference<Set<MongoUser>> members;
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
    public void setOwner(User owner) {
        this.owner = Ref.wrap(owner);
    }

    @Override
    public MongoUser getOwner() {
        return Ref.unwrap(owner);
    }

    @Override
    public boolean hasOwner() {
        return getOwner() != null;
    }

    public Set<MongoUser> getMembers() {
        return Ref.unwrap(members);
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
