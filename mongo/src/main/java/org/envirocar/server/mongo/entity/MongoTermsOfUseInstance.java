/*
 * Copyright (C) 2013 The enviroCar project
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

import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.TermsOfUseInstance;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;

@Entity("termsOfUse")
public class MongoTermsOfUseInstance extends MongoEntityBase implements TermsOfUseInstance {

	public static final String NAME = Mapper.ID_KEY;
	public static final String CONTENTS = "contents";
	public static final String DATE_STRING = "issuedDate";

	@Id
	 private ObjectId id = new ObjectId();
	
	@Property(DATE_STRING)
	private String issuedDate;

	@Property(CONTENTS)
	private String contents;

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
	public void setContents(String c) {
		this.contents = c;
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
		final MongoTermsOfUseInstance other = (MongoTermsOfUseInstance) obj;
		return Objects.equal(this.issuedDate, other.issuedDate);
	}

}
