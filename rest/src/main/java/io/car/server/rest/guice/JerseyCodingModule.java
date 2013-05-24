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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
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
import io.car.server.rest.coding.CodingFactory;
import io.car.server.rest.coding.EntityDecoder;
import io.car.server.rest.coding.EntityEncoder;
import io.car.server.rest.coding.GeoJSON;
import io.car.server.rest.coding.GroupCoder;
import io.car.server.rest.coding.GroupsCoder;
import io.car.server.rest.coding.MeasurementCoder;
import io.car.server.rest.coding.MeasurementsCoder;
import io.car.server.rest.coding.PhenomenonCoder;
import io.car.server.rest.coding.PhenomenonsCoder;
import io.car.server.rest.coding.SensorCoder;
import io.car.server.rest.coding.SensorsCoder;
import io.car.server.rest.coding.StatisticEncoder;
import io.car.server.rest.coding.StatisticsEncoder;
import io.car.server.rest.coding.TrackCoder;
import io.car.server.rest.coding.TracksCoder;
import io.car.server.rest.coding.UserCoder;
import io.car.server.rest.coding.UsersCoder;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyCodingModule extends AbstractModule {
    @Override
    protected void configure() {
        configureCodingFactory();
    }

    protected void configureCodingFactory() {
        FactoryModuleBuilder fmb = new FactoryModuleBuilder();
        bind(fmb, new TypeLiteral<EntityEncoder<User>>() {
        }, UserCoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<User>>() {
        }, UserCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Users>>() {
        }, UsersCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Sensor>>() {
        }, SensorCoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Sensor>>() {
        }, SensorCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Sensors>>() {
        }, SensorsCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Track>>() {
        }, TrackCoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Track>>() {
        }, TrackCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Tracks>>() {
        }, TracksCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Measurement>>() {
        }, MeasurementCoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Measurement>>() {
        }, MeasurementCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Measurements>>() {
        }, MeasurementsCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Phenomenon>>() {
        }, PhenomenonCoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Phenomenon>>() {
        }, PhenomenonCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Phenomenons>>() {
        }, PhenomenonsCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Group>>() {
        }, GroupCoder.class);
        bind(fmb, new TypeLiteral<EntityDecoder<Group>>() {
        }, GroupCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Groups>>() {
        }, GroupsCoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Statistic>>() {
        }, StatisticEncoder.class);
        bind(fmb, new TypeLiteral<EntityEncoder<Statistics>>() {
        }, StatisticsEncoder.class);
        bind(new TypeLiteral<EntityDecoder<Geometry>>() {
        }).to(GeoJSON.class);
        bind(new TypeLiteral<EntityEncoder<Geometry>>() {
        }).to(GeoJSON.class);
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
