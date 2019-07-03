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
package org.envirocar.server.rest.resources;

import org.envirocar.server.core.entities.*;

import javax.annotation.Nullable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 */
public interface ResourceFactory {

    TrackResource createTrackResource(Track track);

    TracksResource createTracksResource();

    MeasurementResource createMeasurementResource(Measurement measurement, @Nullable Track track);

    MeasurementsResource createMeasurementsResource(@Nullable Track track);

    PhenomenonResource createPhenomenonResource(Phenomenon phenomenon);

    PhenomenonsResource createPhenomenonsResource();

    SensorResource createSensorResource(Sensor sensor);

    SensorsResource createSensorsResource();

    StatisticsResource createStatisticsResource();

    StatisticsResource createStatisticsResource(Track track);

    StatisticsResource createStatisticsResource(Sensor sensor);

    StatisticResource createStatisticResource(Phenomenon phenomenon, @Nullable Track track, @Nullable Sensor sensor);

    TermsOfUseResource createTermsOfUseResource();

    TermsOfUseInstanceResource createTermsOfUseInstanceResource(TermsOfUseInstance t);

    JSONSchemaResource createSchemaResource();

    PrivacyStatementsResource createPrivacyStatementsResource();

    PrivacyStatementResource createPrivacyStatementResource(PrivacyStatement announcement);

    ShareResource createShareResource(Track track);

    PreviewResource createPreviewResource(Track track);

}

