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

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;

import org.envirocar.server.core.entities.Phenomenon;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("phenomenons")
public class MongoPhenomenon extends MongoEntityBase implements Phenomenon {
    public static final String NAME = Mapper.ID_KEY;
    public static final String UNIT = "unit";
    @Id
    private String name;
    @Property(UNIT)
    private String unit;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    @Override
    public boolean hasUnit() {
        return getUnit() != null && !getUnit().isEmpty();
    }

    @Override
    public String toString() {
        return toStringHelper()
                .add(NAME, name).toString();
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
        final MongoPhenomenon other = (MongoPhenomenon) obj;
        return Objects.equal(this.name, other.name);
    }
}
