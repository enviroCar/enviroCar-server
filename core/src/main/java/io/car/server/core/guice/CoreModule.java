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
package io.car.server.core.guice;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import io.car.server.core.Service;
import io.car.server.core.statistics.StatisticsService;
import io.car.server.core.statistics.StatisticsServiceImpl;
import io.car.server.core.util.BCryptPasswordEncoder;
import io.car.server.core.util.PasswordEncoder;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class CoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Service.class);
        bind(StatisticsService.class).to(StatisticsServiceImpl.class);
        bind(PasswordEncoder.class).to(BCryptPasswordEncoder.class);
    }

    @Provides
    @Singleton
    public GeometryFactory geometryFactory(PrecisionModel precisionModel) {
        return new GeometryFactory(precisionModel, 4326);
    }

    @Provides
    @Singleton
    public PrecisionModel precisionModel() {
        return new PrecisionModel(PrecisionModel.FLOATING_SINGLE);
    }

    @Provides
    @Singleton
    protected DateTimeFormatter formatter() {
        return ISODateTimeFormat.dateTimeNoMillis();
    }
}
