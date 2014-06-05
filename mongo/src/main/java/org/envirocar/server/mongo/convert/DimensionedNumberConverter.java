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
package org.envirocar.server.mongo.convert;

import java.math.BigDecimal;

import org.envirocar.server.core.entities.DimensionedNumber;

import com.github.jmkgreen.morphia.converters.SimpleValueConverter;
import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.github.jmkgreen.morphia.mapping.MappedField;
import com.github.jmkgreen.morphia.mapping.MappingException;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * MongoDB type converter for {@link DimensionedNumber}s.
 *
 * @author Christian Autermann
 */
public class DimensionedNumberConverter
        extends TypeConverter implements SimpleValueConverter {
    public DimensionedNumberConverter() {
        super(DimensionedNumber.class);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }
        DimensionedNumber dn = (DimensionedNumber) value;
        return BasicDBObjectBuilder.start()
                .add("value", dn.value().toString())
                .add("unit", dn.unit()).get();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public DimensionedNumber decode(Class c, Object o, MappedField i) throws
            MappingException {
        if (o == null) {
            return null;
        } else if (o instanceof DBObject) {
            DBObject dbObject = (DBObject) o;
            Object v = dbObject.get("value");
            BigDecimal value;
            if (v instanceof BigDecimal) {
                value = (BigDecimal) v;
            } else if (v instanceof String) {
                value = new BigDecimal((String) v);
            } else if (v instanceof Number) {
                value = new BigDecimal(((Number) v).doubleValue());
            } else {
                throw new MappingException("Can not decode " + v);
            }
            return new DimensionedNumber(value, (String) dbObject.get("unit"));
        } else {
            throw new MappingException("Can not decode " + o);
        }
    }
}
