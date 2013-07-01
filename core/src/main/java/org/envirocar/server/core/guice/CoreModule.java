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
package org.envirocar.server.core.guice;

import org.envirocar.server.core.DataService;
import org.envirocar.server.core.DataServiceImpl;
import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.FriendServiceImpl;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.GroupServiceImpl;
import org.envirocar.server.core.StatisticsService;
import org.envirocar.server.core.StatisticsServiceImpl;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.UserServiceImpl;
import org.envirocar.server.core.activities.ActivityListener;
import org.envirocar.server.core.util.BCryptPasswordEncoder;
import org.envirocar.server.core.util.PasswordEncoder;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class CoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataService.class).to(DataServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(FriendService.class).to(FriendServiceImpl.class);
        bind(GroupService.class).to(GroupServiceImpl.class);
        bind(StatisticsService.class).to(StatisticsServiceImpl.class);
        bind(ActivityListener.class).asEagerSingleton();
        bind(PasswordEncoder.class).to(BCryptPasswordEncoder.class);
        DateTimeZone.setDefault(DateTimeZone.UTC);
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
    public DateTimeFormatter formatter() {
        return ISODateTimeFormat.dateTimeNoMillis();
    }
}
