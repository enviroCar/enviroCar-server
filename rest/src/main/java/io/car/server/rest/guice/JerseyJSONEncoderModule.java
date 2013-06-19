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
import io.car.server.rest.encoding.json.ActivitiesJSONEncoder;
import io.car.server.rest.encoding.json.ActivityJSONEncoder;
import io.car.server.rest.encoding.JSONEntityEncoder;
import io.car.server.rest.encoding.json.GeometryJSONEncoder;
import io.car.server.rest.encoding.json.GroupJSONEncoder;
import io.car.server.rest.encoding.json.GroupsJSONEncoder;
import io.car.server.rest.encoding.json.MeasurementJSONEncoder;
import io.car.server.rest.encoding.json.MeasurementsJSONEncoder;
import io.car.server.rest.encoding.json.PhenomenonJSONEncoder;
import io.car.server.rest.encoding.json.PhenomenonsJSONEncoder;
import io.car.server.rest.encoding.json.SensorJSONEncoder;
import io.car.server.rest.encoding.json.SensorsJSONEncoder;
import io.car.server.rest.encoding.json.StatisticJSONEncoder;
import io.car.server.rest.encoding.json.StatisticsJSONEncoder;
import io.car.server.rest.encoding.json.TrackJSONEncoder;
import io.car.server.rest.encoding.json.TracksJSONEncoder;
import io.car.server.rest.encoding.json.UserJSONEncoder;
import io.car.server.rest.encoding.json.UsersJSONEncoder;
import io.car.server.rest.encoding.json.JsonNodeMessageBodyWriter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyJSONEncoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonNodeMessageBodyWriter.class).in(Scopes.SINGLETON);
        bind(UserJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<User>>() {
        }).to(UserJSONEncoder.class);
        bind(UsersJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Users>>() {
        }).to(UsersJSONEncoder.class);
        bind(SensorJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Sensor>>() {
        }).to(SensorJSONEncoder.class);
        bind(SensorsJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Sensors>>() {
        }).to(SensorsJSONEncoder.class);
        bind(TrackJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Track>>() {
        }).to(TrackJSONEncoder.class);
        bind(TracksJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Tracks>>() {
        }).to(TracksJSONEncoder.class);
        bind(MeasurementJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Measurement>>() {
        }).to(MeasurementJSONEncoder.class);
        bind(MeasurementsJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Measurements>>() {
        }).to(MeasurementsJSONEncoder.class);
        bind(PhenomenonJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Phenomenon>>() {
        }).to(PhenomenonJSONEncoder.class);
        bind(PhenomenonsJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Phenomenons>>() {
        }).to(PhenomenonsJSONEncoder.class);
        bind(GroupJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Group>>() {
        }).to(GroupJSONEncoder.class);
        bind(GroupsJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Groups>>() {
        }).to(GroupsJSONEncoder.class);
        bind(StatisticJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Statistic>>() {
        }).to(StatisticJSONEncoder.class);
        bind(StatisticsJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Statistics>>() {
        }).to(StatisticsJSONEncoder.class);
        bind(ActivityJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Activity>>() {
        }).to(ActivityJSONEncoder.class);
        bind(ActivitiesJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Activities>>() {
        }).to(ActivitiesJSONEncoder.class);
        bind(GeometryJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Geometry>>() {
        }).to(GeometryJSONEncoder.class);
    }
}
