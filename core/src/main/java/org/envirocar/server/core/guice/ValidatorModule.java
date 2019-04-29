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
package org.envirocar.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.validation.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ValidatorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<EntityValidator<Track>>() {
        }).to(TrackValidator.class);
        bind(new TypeLiteral<EntityValidator<Measurement>>() {
        }).to(MeasurementValidator.class);
        bind(new TypeLiteral<EntityValidator<TrackSummary>>() {
        }).to(TrackSummaryValidator.class);
        bind(new TypeLiteral<EntityValidator<TrackSummaries>>() {
        }).to(TrackSummariesValidator.class);
    }
}
