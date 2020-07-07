/*
 * Copyright (C) 2013-2020 The enviroCar project
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
import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.Terms;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.mapping.Mapper;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class MongoTerms extends MongoEntityBase implements Terms {
    public static final String NAME = "_id";
    public static final String CONTENTS = "contents";
    public static final String ISSUED_DATE = "issuedDate";
    public static final String TRANSLATIONS = "translations";

    @Id
    private ObjectId id = new ObjectId();

    @Property(ISSUED_DATE)
    private String issuedDate;

    @Property(CONTENTS)
    private String contents;

    @Property(TRANSLATIONS)
    private Map<String, String> translations = Collections.emptyMap();

    @Override
    public Map<String, String> getTranslations() {
        return translations;
    }

    @Override
    public void setTranslations(Map<String, String> translations) {
        this.translations = Optional.ofNullable(translations).orElseGet(Collections::emptyMap);
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
    public String getIssuedDate() {
        return this.issuedDate;
    }

    @Override
    public void setIssuedDate(String ds) {
        this.issuedDate = ds;
    }

    @Override
    public String getContents() {
        return this.contents;
    }

    @Override
    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.issuedDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoTerms other = (MongoTerms) obj;
        return Objects.equal(this.issuedDate, other.issuedDate);
    }
}
