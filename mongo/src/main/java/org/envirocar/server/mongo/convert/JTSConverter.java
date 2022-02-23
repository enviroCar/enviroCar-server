/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.google.inject.Inject;
import dev.morphia.converters.SimpleValueConverter;
import dev.morphia.converters.TypeConverter;
import dev.morphia.mapping.MappedField;
import dev.morphia.mapping.MappingException;
import org.bson.BSONObject;
import org.envirocar.server.core.exception.GeometryConverterException;
import org.envirocar.server.core.util.GeometryConverter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

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
                return this.geoJSON.encode((Geometry) value);
            } catch (GeometryConverterException ex) {
                throw new MappingException("Can not encode geometry", ex);
            }
        } else {
            throw new MappingException("value is not a Geometry");
        }
    }

    @Override
    public Geometry decode(Class targetClass, Object db,
                           MappedField optionalExtraInfo) {
        if (db == null) {
            return null;
        } else if (db instanceof BSONObject) {
            try {
                return this.geoJSON.decode((BSONObject) db);
            } catch (GeometryConverterException ex) {
                throw new MappingException("Can not decode geometry", ex);
            }
        } else {
            throw new MappingException("value is not a BSONObject");
        }
    }
}
