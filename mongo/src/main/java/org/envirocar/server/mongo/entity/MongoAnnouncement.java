/*
 * Copyright (C) 2013-2021 The enviroCar project
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
import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.Announcement;

import java.util.Map;

@Entity("announcements")
public class MongoAnnouncement extends MongoEntityBase implements Announcement {

    public static final String NAME = "_id";
    public static final String CONTENTS = "content";
    public static final String VERSIONS = "versions";
    public static final String PRIORITY = "priority";
    public static final String CATEGORY = "category";

    @Id
    private ObjectId id = new ObjectId();

    @Property(VERSIONS)
    private String versions;

    @Property(CONTENTS)
    private Map<String, String> contents;

    @Property(PRIORITY)
    private String priority;

    @Property(CATEGORY)
    private String category;

    @Override
    public String getVersions() {
        return this.versions;
    }

    @Override
    public void setVersions(String versions) {
        this.versions = versions;
    }

    @Override
    public Map<String, String> getContents() {
        return this.contents;
    }

    @Override
    public void setContents(Map<String, String> contents) {
        this.contents = contents;
    }

    @Override
    public String getPriority() {
        return this.priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getIdentifier() {
        return this.id == null ? null : this.id.toString();
    }

    @Override
    public void setIdentifier(String id) {
        this.id = id == null ? null : new ObjectId(id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MongoAnnouncement other = (MongoAnnouncement) obj;
        return Objects.equal(this.id, other.id);
    }

}
