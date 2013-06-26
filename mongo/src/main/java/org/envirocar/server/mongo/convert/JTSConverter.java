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

import org.bson.BSONObject;

import com.github.jmkgreen.morphia.converters.SimpleValueConverter;
import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.github.jmkgreen.morphia.mapping.MappedField;
import com.github.jmkgreen.morphia.mapping.MappingException;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import org.envirocar.server.core.exception.GeometryConverterException;

import org.envirocar.server.core.util.GeometryConverter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JTSConverter extends TypeConverter implements SimpleValueConverter {
    private final GeometryConverter<BSONObject> geoJSON;

    @Inject
    public JTSConverter(GeometryConverter<BSONObject> geoJSON) {
        super(Geometry.class, GeometryCollection.class,
              Point.class, MultiPoint.class,
              LineString.class, MultiLineString.class,
              Polygon.class, MultiPolygon.class);
        this.geoJSON = geoJSON;
    }

    @Override
    public BSONObject encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        } else if (value instanceof Geometry) {
            try {
                return geoJSON.encode((Geometry) value);
            } catch (GeometryConverterException ex) {
                throw new MappingException("Can not encode geometry", ex);
            }
        } else {
            throw new MappingException("value is not a Geometry");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Geometry decode(Class targetClass, Object db,
                           MappedField optionalExtraInfo) {
        if (db == null) {
            return null;
        } else if (db instanceof BSONObject) {
            try {
                return geoJSON.decode((BSONObject) db);
            } catch (GeometryConverterException ex) {
                throw new MappingException("Can not decode geometry", ex);
            }
        } else {
            throw new MappingException("value is not a BSONObject");
        }
    }
}
