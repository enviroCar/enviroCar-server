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
package org.envirocar.server.rest.guice;

import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.core.entities.Announcements;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Groups;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Phenomenons;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.TermsOfUse;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.encoding.json.ActivitiesJSONEncoder;
import org.envirocar.server.rest.encoding.json.ActivityJSONEncoder;
import org.envirocar.server.rest.encoding.json.AnnouncementJSONEncoder;
import org.envirocar.server.rest.encoding.json.AnnouncementsJSONEncoder;
import org.envirocar.server.rest.encoding.json.GeometryJSONEncoder;
import org.envirocar.server.rest.encoding.json.GroupJSONEncoder;
import org.envirocar.server.rest.encoding.json.GroupsJSONEncoder;
import org.envirocar.server.rest.encoding.json.JsonNodeMessageBodyWriter;
import org.envirocar.server.rest.encoding.json.MeasurementJSONEncoder;
import org.envirocar.server.rest.encoding.json.MeasurementsJSONEncoder;
import org.envirocar.server.rest.encoding.json.PhenomenonJSONEncoder;
import org.envirocar.server.rest.encoding.json.PhenomenonsJSONEncoder;
import org.envirocar.server.rest.encoding.json.SensorJSONEncoder;
import org.envirocar.server.rest.encoding.json.SensorsJSONEncoder;
import org.envirocar.server.rest.encoding.json.StatisticJSONEncoder;
import org.envirocar.server.rest.encoding.json.StatisticsJSONEncoder;
import org.envirocar.server.rest.encoding.json.TermsOfUseJSONEncoder;
import org.envirocar.server.rest.encoding.json.TermsOfUseInstanceJSONEncoder;
import org.envirocar.server.rest.encoding.json.TrackJSONEncoder;
import org.envirocar.server.rest.encoding.json.TracksJSONEncoder;
import org.envirocar.server.rest.encoding.json.UserJSONEncoder;
import org.envirocar.server.rest.encoding.json.UsersJSONEncoder;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.vividsolutions.jts.geom.Geometry;

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
        
        bind(TermsOfUseInstanceJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<TermsOfUseInstance>>() {
        }).to(TermsOfUseInstanceJSONEncoder.class);
        bind(TermsOfUseJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<TermsOfUse>>() {
        }).to(TermsOfUseJSONEncoder.class);
        
        bind(AnnouncementJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Announcement>>() {
        }).to(AnnouncementJSONEncoder.class);
        bind(AnnouncementsJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Announcements>>() {
        }).to(AnnouncementsJSONEncoder.class);
    }
}
