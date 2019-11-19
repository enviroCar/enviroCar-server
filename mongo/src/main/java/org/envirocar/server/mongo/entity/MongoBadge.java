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
import org.envirocar.server.core.entities.Badge;

import java.util.Map;

@Entity(value = MongoBadge.COLLECTION, noClassnameStored = true)
public class MongoBadge extends MongoEntityBase implements Badge {

    public static final String NAME = "_id";
    public static final String DISPLAY_NAME = "displayName";
    public static final String DESCRIPTION = "description";
    public static final String COLLECTION = "badges";
    @Id
    private String name;
    @Property(DISPLAY_NAME)
    private Map<String, String> displayName;
    @Property(DESCRIPTION)
    private Map<String, String> description;

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
        final MongoBadge other = (MongoBadge) obj;
        return Objects.equal(this.name, other.name);
    }

    @Override
    public Map<String, String> getDescription() {
        return description;
    }

    @Override
    public void setDescription(Map<String, String> descriptions) {
        this.description = descriptions;
    }

    @Override
    public Map<String, String> getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(Map<String, String> dnames) {
        this.displayName = dnames;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
