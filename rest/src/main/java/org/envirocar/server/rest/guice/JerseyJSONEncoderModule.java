/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.activities.*;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.encoding.json.*;
import org.envirocar.server.rest.util.ErrorMessage;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyJSONEncoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonNodeMessageBodyWriter.class);

        bind(new TypeLiteral<JSONEntityEncoder<User>>() {}).to(UserJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Users>>() {}).to(UsersJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Sensor>>() {}).to(SensorJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Sensors>>() {}).to(SensorsJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Track>>() {}).to(TrackJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Tracks>>() {}).to(TracksJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Measurement>>() {}).to(MeasurementJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Measurements>>() {}).to(MeasurementsJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Phenomenon>>() {}).to(PhenomenonJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Phenomenons>>() {}).to(PhenomenonsJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Group>>() {}).to(GroupJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Groups>>() {}).to(GroupsJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Statistic>>() {}).to(StatisticJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Statistics>>() {}).to(StatisticsJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Activity>>() {}).to(ActivityJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Activities>>() {}).to(ActivitiesJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Geometry>>() {}).to(GeometryJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<TermsOfUseInstance>>() {}).to(TermsOfUseInstanceJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<TermsOfUse>>() {}).to(TermsOfUseJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<PrivacyStatement>>() {}).to(PrivacyStatementJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<PrivacyStatements>>() {}).to(PrivacyStatementsJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Announcement>>() {}).to(AnnouncementJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Announcements>>() {}).to(AnnouncementsJSONEncoder.class);
        bind(new TypeLiteral<JSONEntityEncoder<Badge>>() {}).to(BadgeJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Badges>>() {}).to(BadgesJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Fueling>>() {}).to(FuelingJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<Fuelings>>() {}).to(FuelingsJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<UserStatistic>>() {}).to(UserStatisticJSONEncoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityEncoder<ErrorMessage>>() {}).to(ErrorMessageJSONEncoder.class).in(Scopes.SINGLETON);
        bind(UserJSONEncoder.class);
        bind(UsersJSONEncoder.class);
        bind(SensorJSONEncoder.class);
        bind(SensorsJSONEncoder.class);
        bind(TrackJSONEncoder.class);
        bind(TracksJSONEncoder.class);
        bind(MeasurementJSONEncoder.class);
        bind(MeasurementsJSONEncoder.class);
        bind(PhenomenonJSONEncoder.class);
        bind(PhenomenonsJSONEncoder.class);
        bind(GroupJSONEncoder.class);
        bind(GroupsJSONEncoder.class);
        bind(StatisticJSONEncoder.class);
        bind(StatisticsJSONEncoder.class);
        bind(ActivityJSONEncoder.class);
        bind(ActivitiesJSONEncoder.class);
        bind(GeometryJSONEncoder.class);
        bind(TermsOfUseInstanceJSONEncoder.class);
        bind(TermsOfUseJSONEncoder.class);
        bind(PrivacyStatementJSONEncoder.class);
        bind(PrivacyStatementsJSONEncoder.class);
        bind(AnnouncementJSONEncoder.class);
        bind(AnnouncementsJSONEncoder.class);
        bind(BadgeJSONEncoder.class);
        bind(BadgesJSONEncoder.class);
        bind(FuelingJSONEncoder.class);
        bind(FuelingsJSONEncoder.class);
        bind(UserStatisticJSONEncoder.class);
        bind(ErrorMessageJSONEncoder.class);

    }
}
