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
package org.envirocar.server.mongo.guice;

import org.bson.BSONObject;

import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import org.envirocar.server.core.util.GeometryConverter;
import org.envirocar.server.mongo.convert.DateTimeConverter;
import org.envirocar.server.mongo.convert.DurationConverter;
import org.envirocar.server.mongo.convert.FileConverter;
import org.envirocar.server.mongo.convert.JTSConverter;
import org.envirocar.server.mongo.convert.URLConverter;

import org.envirocar.server.mongo.util.GeoBSON;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoConverterModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<TypeConverter> mb = Multibinder
                .newSetBinder(binder(), TypeConverter.class);
        bind(new TypeLiteral<GeometryConverter<BSONObject>>() {
        }).to(GeoBSON.class);
        mb.addBinding().to(DateTimeConverter.class);
        mb.addBinding().to(DurationConverter.class);
        mb.addBinding().to(FileConverter.class);
        mb.addBinding().to(JTSConverter.class);
        mb.addBinding().to(URLConverter.class);
    }
}
