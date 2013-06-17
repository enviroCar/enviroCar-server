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

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
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
import io.car.server.rest.provider.JsonNodeMessageBodyWriter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyEncoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonNodeMessageBodyWriter.class).in(Scopes.SINGLETON);
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
