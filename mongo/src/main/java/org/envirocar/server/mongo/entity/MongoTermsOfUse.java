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

import org.envirocar.server.core.entities.TermsOfUse;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;

@Entity("termsOfUse")
public class MongoTermsOfUse extends MongoEntityBase implements TermsOfUse {

	public static final String NAME = Mapper.ID_KEY;
	public static final String CONTENTS = "contents";

	@Id
	private String dateString;

	@Property(CONTENTS)
	private String contents;

	@Override
	public String getDateString() {
		return this.dateString;
	}

	@Override
	public void setDateString(String ds) {
		this.dateString = ds;
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
		return Objects.hashCode(this.dateString);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MongoTermsOfUse other = (MongoTermsOfUse) obj;
		return Objects.equal(this.dateString, other.dateString);
	}
}