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
package org.envirocar.server.core.entities;

import java.math.BigDecimal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Entity to bind a unit to a number.
 *
 * @author Christian Autermann
 */
public class DimensionedNumber extends Number {
    private static final long serialVersionUID = 1L;
    private final BigDecimal value;
    private final String unit;

    /**
     * Creates a new {@code DimensionedNumber} for the specified value and unit.
     *
     * @param value the value
     * @param unit  the unit
     */
    public DimensionedNumber(BigDecimal value, String unit) {
        this.value = Preconditions.checkNotNull(value);
        this.unit = Preconditions.checkNotNull(Strings.emptyToNull(unit));
    }

    /**
     * Gets the underlying value of this {@code DimensionedNumber}.
     *
     * @return the value
     */
    public BigDecimal value() {
        return this.value;
    }

    /**
     * Gets the unit of this {@code DimensionedNumber}.
     *
     * @return the unit
     */
    public String unit() {
        return this.unit;
    }

    @Override
    public int intValue() {
        return value().intValue();
    }

    @Override
    public long longValue() {
        return value().longValue();
    }

    @Override
    public float floatValue() {
        return value().floatValue();
    }

    @Override
    public double doubleValue() {
        return value().doubleValue();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", this.value())
                .add("unit", this.unit())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value(), unit());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            DimensionedNumber that = (DimensionedNumber) obj;
            return Objects.equal(this.value(), that.value()) &&
                   Objects.equal(this.unit(), that.unit());
        }
        return false;
    }

}
