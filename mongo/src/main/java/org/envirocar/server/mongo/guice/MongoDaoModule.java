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
package org.envirocar.server.mongo.guice;

import com.google.inject.AbstractModule;
import org.envirocar.server.core.dao.*;
import org.envirocar.server.mongo.dao.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoDaoModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TrackDao.class).to(MongoTrackDao.class);
        bind(MeasurementDao.class).to(MongoMeasurementDao.class);
        bind(SensorDao.class).to(MongoSensorDao.class);
        bind(StatisticsDao.class).to(MongoStatisticsDao.class);
        bind(PhenomenonDao.class).to(MongoPhenomenonDao.class);
        bind(TermsOfUseDao.class).to(MongoTermsOfUseDao.class);
    }
}
