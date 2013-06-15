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

import static io.car.server.rest.validation.JSONSchemaResourceFilterFactory.VALIDATE_REQUESTS;
import static io.car.server.rest.validation.JSONSchemaResourceFilterFactory.VALIDATE_RESPONSES;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.activities.Activities;
import io.car.server.core.activities.Activity;
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
import io.car.server.rest.decoding.EntityDecoder;
import io.car.server.rest.decoding.GeoJSONDecoder;
import io.car.server.rest.decoding.GroupDecoder;
import io.car.server.rest.decoding.MeasurementDecoder;
import io.car.server.rest.decoding.PhenomenonDecoder;
import io.car.server.rest.decoding.SensorDecoder;
import io.car.server.rest.decoding.TrackDecoder;
import io.car.server.rest.decoding.UserDecoder;
import io.car.server.rest.encoding.ActivitiesEncoder;
import io.car.server.rest.encoding.ActivityEncoder;
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
import io.car.server.rest.provider.JsonNodeProvider;
import io.car.server.rest.provider.UserReferenceProvider;
import io.car.server.rest.util.GeoJSON;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyCodingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GeoJSON.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<GeometryConverter<JsonNode>>() {
        }).to(GeoJSON.class);
        install(new EncoderModule());
        install(new DecoderModule());
        install(new ValidationModule());

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

    private class ValidationModule extends AbstractModule {
        @Override
        protected void configure() {
            Multibinder<String> mb = Multibinder.newSetBinder(
                    binder(), String.class, Names
                    .named(JSONSchemaFactoryProvider.SCHEMAS));
            mb.addBinding().toInstance("/schema/definitions.json");
            mb.addBinding().toInstance("/schema/geometry.json");
            mb.addBinding().toInstance("/schema/group.create.json");
            mb.addBinding().toInstance("/schema/group.json");
            mb.addBinding().toInstance("/schema/group.modify.json");
            mb.addBinding().toInstance("/schema/groups.json");
            mb.addBinding().toInstance("/schema/measurement.create.json");
            mb.addBinding().toInstance("/schema/measurement.json");
            mb.addBinding().toInstance("/schema/measurements.json");
            mb.addBinding().toInstance("/schema/phenomenon.create.json");
            mb.addBinding().toInstance("/schema/phenomenon.json");
            mb.addBinding().toInstance("/schema/phenomenon.modify.json");
            mb.addBinding().toInstance("/schema/phenomenons.json");
            mb.addBinding().toInstance("/schema/root.json");
            mb.addBinding().toInstance("/schema/sensor.create.json");
            mb.addBinding().toInstance("/schema/sensor.json");
            mb.addBinding().toInstance("/schema/sensors.json");
            mb.addBinding().toInstance("/schema/track.create.json");
            mb.addBinding().toInstance("/schema/track.json");
            mb.addBinding().toInstance("/schema/track.modify.json");
            mb.addBinding().toInstance("/schema/tracks.json");
            mb.addBinding().toInstance("/schema/user.create.json");
            mb.addBinding().toInstance("/schema/user.json");
            mb.addBinding().toInstance("/schema/user.modify.json");
            mb.addBinding().toInstance("/schema/user.ref.json");
            mb.addBinding().toInstance("/schema/users.json");
            mb.addBinding().toInstance("/schema/statistics.json");
            mb.addBinding().toInstance("/schema/statistic.json");
            mb.addBinding().toInstance("/schema/activity.json");
            mb.addBinding().toInstance("/schema/activities.json");
            bindConstant().annotatedWith(Names.named(VALIDATE_REQUESTS))
                    .to(true);
            bindConstant().annotatedWith(Names.named(VALIDATE_RESPONSES))
                    .to(true);
            bind(JsonSchemaFactory.class)
                    .toProvider(JSONSchemaFactoryProvider.class)
                    .in(Scopes.SINGLETON);
        }
    }

    private class DecoderModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(UserDecoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityDecoder<User>>() {
            }).to(UserDecoder.class);
            bind(UserReferenceProvider.class).in(Scopes.SINGLETON);

            bind(PhenomenonDecoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityDecoder<Phenomenon>>() {
            }).to(PhenomenonDecoder.class);

            bind(GroupDecoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityDecoder<Group>>() {
            }).to(GroupDecoder.class);

            bind(GeoJSONDecoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityDecoder<Geometry>>() {
            }).to(GeoJSONDecoder.class);

            bind(MeasurementDecoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityDecoder<Measurement>>() {
            }).to(MeasurementDecoder.class);

            bind(TrackDecoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityDecoder<Track>>() {
            }).to(TrackDecoder.class);

            bind(SensorDecoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityDecoder<Sensor>>() {
            }).to(SensorDecoder.class);
        }
    }

    private class EncoderModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(JsonNodeProvider.class).in(Scopes.SINGLETON);

            bind(UserEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<User>>() {
            }).to(UserEncoder.class);
            bind(UsersEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Users>>() {
            }).to(UsersEncoder.class);

            bind(SensorEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Sensor>>() {
            }).to(SensorEncoder.class);
            bind(SensorsEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Sensors>>() {
            }).to(SensorsEncoder.class);

            bind(TrackEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Track>>() {
            }).to(TrackEncoder.class);
            bind(TracksEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Tracks>>() {
            }).to(TracksEncoder.class);

            bind(MeasurementEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Measurement>>() {
            }).to(MeasurementEncoder.class);
            bind(MeasurementsEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Measurements>>() {
            }).to(MeasurementsEncoder.class);

            bind(PhenomenonEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Phenomenon>>() {
            }).to(PhenomenonEncoder.class);

            bind(PhenomenonsEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Phenomenons>>() {
            }).to(PhenomenonsEncoder.class);

            bind(GroupEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Group>>() {
            }).to(GroupEncoder.class);

            bind(GroupsEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Groups>>() {
            }).to(GroupsEncoder.class);

            bind(StatisticEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Statistic>>() {
            }).to(StatisticEncoder.class);
            bind(StatisticsEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Statistics>>() {
            }).to(StatisticsEncoder.class);

            bind(ActivityEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Activity>>() {
            }).to(ActivityEncoder.class);
            bind(ActivitiesEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Activities>>() {
            }).to(ActivitiesEncoder.class);

            bind(GeoJSONEncoder.class).in(Scopes.SINGLETON);
            bind(new TypeLiteral<EntityEncoder<Geometry>>() {
            }).to(GeoJSONEncoder.class);
        }
    }
}
