/*
 * Copyright (C) 2013-2018 The enviroCar project
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
import org.envirocar.server.core.entities.*;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.envirocar.server.rest.encoding.RDFEntityEncoder;
import org.envirocar.server.rest.encoding.rdf.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyRDFEncoderModule extends AbstractModule {
    @Override
    protected void configure() {

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
    }
}
