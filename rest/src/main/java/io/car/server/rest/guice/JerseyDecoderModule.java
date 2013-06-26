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
package io.car.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.rest.decoding.json.GeoJSONDecoder;
import io.car.server.rest.decoding.json.GroupDecoder;
import io.car.server.rest.decoding.json.JSONEntityDecoder;
import io.car.server.rest.decoding.json.JsonNodeMessageBodyReader;
import io.car.server.rest.decoding.json.MeasurementDecoder;
import io.car.server.rest.decoding.json.PhenomenonDecoder;
import io.car.server.rest.decoding.json.SensorDecoder;
import io.car.server.rest.decoding.json.TrackDecoder;
import io.car.server.rest.decoding.json.UserDecoder;
import io.car.server.rest.encoding.json.UserReferenceProvider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyDecoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonNodeMessageBodyReader.class).in(Scopes.SINGLETON);
        bind(UserDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<User>>() {
        }).to(UserDecoder.class);
        bind(UserReferenceProvider.class).in(Scopes.SINGLETON);
        bind(PhenomenonDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Phenomenon>>() {
        }).to(PhenomenonDecoder.class);
        bind(GroupDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Group>>() {
        }).to(GroupDecoder.class);
        bind(GeoJSONDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Geometry>>() {
        }).to(GeoJSONDecoder.class);
        bind(MeasurementDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Measurement>>() {
        }).to(MeasurementDecoder.class);
        bind(TrackDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Track>>() {
        }).to(TrackDecoder.class);
        bind(SensorDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Sensor>>() {
        }).to(SensorDecoder.class);
    }
}
