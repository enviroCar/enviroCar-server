package org.envirocar.server.core.entities;

import java.math.BigDecimal;

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
        return Objects.toStringHelper(this)
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
