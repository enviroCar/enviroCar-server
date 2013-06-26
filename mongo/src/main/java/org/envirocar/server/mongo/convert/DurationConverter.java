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

import org.joda.time.Duration;

import com.github.jmkgreen.morphia.converters.SimpleValueConverter;
import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.github.jmkgreen.morphia.mapping.MappedField;
import com.github.jmkgreen.morphia.mapping.MappingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class DurationConverter extends TypeConverter implements
        SimpleValueConverter {
    public DurationConverter() {
        super(Duration.class);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        return value == null ? null : ((Duration) value).getMillis();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object decode(Class c, Object o, MappedField i) throws
            MappingException {
        if (o == null) {
            return null;
        } else if (o instanceof Duration) {
            return o;
        } else if (o instanceof Number) {
            return new Duration(((Number) o).longValue());
        } else {
            return new Duration(o);
        }
    }
}
