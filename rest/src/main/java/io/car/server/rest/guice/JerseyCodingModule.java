/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.statistics.Statistic;
import io.car.server.core.statistics.Statistics;
import io.car.server.core.util.GeometryConverter;
import io.car.server.rest.CodingFactory;
import io.car.server.rest.decoding.EntityDecoder;
import io.car.server.rest.decoding.GeoJSONDecoder;
import io.car.server.rest.decoding.GroupDecoder;
import io.car.server.rest.decoding.MeasurementDecoder;
import io.car.server.rest.decoding.PhenomenonDecoder;
import io.car.server.rest.decoding.SensorDecoder;
import io.car.server.rest.decoding.TrackDecoder;
import io.car.server.rest.decoding.UserDecoder;
import io.car.server.rest.encoding.EntityEncoder;
import io.car.server.rest.encoding.GeoJSONEncoder;
import io.car.server.rest.encoding.GroupEncoder;
import io.car.server.rest.encoding.GroupsEncoder;
import io.car.server.rest.encoding.MeasurementEncoder;
import io.car.server.rest.encoding.MeasurementsEncoder;
import io.car.server.rest.encoding.PhenomenonEncoder;
import io.car.server.rest.encoding.PhenomenonsEncoder;
import io.car.server.rest.encoding.SensorEncoder;
import io.car.server.rest.encoding.SensorsEncoder;
import io.car.server.rest.encoding.StatisticEncoder;
import io.car.server.rest.encoding.StatisticsEncoder;
import io.car.server.rest.encoding.TrackEncoder;
import io.car.server.rest.encoding.TracksEncoder;
import io.car.server.rest.encoding.UserEncoder;
import io.car.server.rest.encoding.UsersEncoder;
import io.car.server.rest.util.GeoJSON;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyCodingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<GeometryConverter<JsonNode>>() {
        }).to(GeoJSON.class).in(Scopes.SINGLETON);
        configureCodingFactory();
    }

    protected void configureCodingFactory() {
        FactoryModuleBuilder fmb = new FactoryModuleBuilder();
        bind(fmb, new TypeLiteral<EntityEncoder<User>>() {
        }, UserEncoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<User>>() {
        }, UserDecoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Users>>() {
        }, UsersEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Sensor>>() {
        }, SensorEncoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Sensor>>() {
        }, SensorDecoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Sensors>>() {
        }, SensorsEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Track>>() {
        }, TrackEncoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Track>>() {
        }, TrackDecoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Tracks>>() {
        }, TracksEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Measurement>>() {
        }, MeasurementEncoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Measurement>>() {
        }, MeasurementDecoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Measurements>>() {
        }, MeasurementsEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Phenomenon>>() {
        }, PhenomenonEncoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Phenomenon>>() {
        }, PhenomenonDecoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Phenomenons>>() {
        }, PhenomenonsEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Group>>() {
        }, GroupEncoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Group>>() {
        }, GroupDecoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Groups>>() {
        }, GroupsEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Statistic>>() {
        }, StatisticEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Statistics>>() {
        }, StatisticsEncoder.class);
        bind(new TypeLiteral<EntityDecoder<Geometry>>() {
        }).to(GeoJSONDecoder.class);
        bind(new TypeLiteral<EntityEncoder<Geometry>>() {
        }).to(GeoJSONEncoder.class);
        install(fmb.build(CodingFactory.class));
    }

    protected <T> void bind(FactoryModuleBuilder fmb,
                                        TypeLiteral<T> source,
                                        Class<? extends T> target) {
        fmb.implement(source, target);
        bind(source).to(target);
    }

    @Provides
    @Singleton
    public JsonNodeFactory jsonNodeFactory() {
        return JsonNodeFactory.withExactBigDecimals(false);
    }

    @Provides
    @Singleton
    public ObjectReader objectReader(ObjectMapper mapper) {
        return mapper.reader();
    }

    @Provides
    @Singleton
    public ObjectWriter objectWriter(ObjectMapper mapper) {
        return mapper.writer();
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper(JsonNodeFactory factory) {
        return new ObjectMapper().setNodeFactory(factory)
                .disable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }
}
