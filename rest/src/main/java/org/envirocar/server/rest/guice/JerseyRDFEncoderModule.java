/*
 * Copyright (C) 2013-2021 The enviroCar project
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
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Fuelings;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Groups;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Phenomenons;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.envirocar.server.rest.encoding.RDFEntityEncoder;
import org.envirocar.server.rest.encoding.rdf.ActivitiesRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.ActivityRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.FuelingRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.FuelingsRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.GroupRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.GroupsRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.MeasurementRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.MeasurementsRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.PhenomenonRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.PhenomenonsRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.SensorRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.SensorsRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.StatisticRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.StatisticsRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.TrackRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.TracksRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.UserRDFEncoder;
import org.envirocar.server.rest.encoding.rdf.UsersRDFEncoder;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyRDFEncoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ActivitiesRDFEncoder.class);

        bind(new TypeLiteral<RDFEntityEncoder<Activities>>() {}).to(ActivitiesRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Activity>>() {}).to(ActivityRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Group>>() {}).to(GroupRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Groups>>() {}).to(GroupsRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Measurement>>() {}).to(MeasurementRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Measurements>>() {}).to(MeasurementsRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Phenomenon>>() {}).to(PhenomenonRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Phenomenons>>() {}).to(PhenomenonsRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Sensor>>() {}).to(SensorRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Sensors>>() {}).to(SensorsRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Statistic>>() {}).to(StatisticRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Statistics>>() {}).to(StatisticsRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Track>>() {}).to(TrackRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Tracks>>() {}).to(TracksRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<User>>() {}).to(UserRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Users>>() {}).to(UsersRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Fueling>>() {}).to(FuelingRDFEncoder.class);
        bind(new TypeLiteral<RDFEntityEncoder<Fuelings>>() {}).to(FuelingsRDFEncoder.class);

        bind(ActivitiesRDFEncoder.class);
        bind(ActivityRDFEncoder.class);
        bind(GroupRDFEncoder.class);
        bind(GroupsRDFEncoder.class);
        bind(MeasurementRDFEncoder.class);
        bind(MeasurementsRDFEncoder.class);
        bind(PhenomenonRDFEncoder.class);
        bind(PhenomenonsRDFEncoder.class);
        bind(SensorRDFEncoder.class);
        bind(SensorsRDFEncoder.class);
        bind(StatisticRDFEncoder.class);
        bind(StatisticsRDFEncoder.class);
        bind(TrackRDFEncoder.class);
        bind(TracksRDFEncoder.class);
        bind(UserRDFEncoder.class);
        bind(UsersRDFEncoder.class);
        bind(FuelingRDFEncoder.class);
        bind(FuelingsRDFEncoder.class);

    }
}
