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
package org.envirocar.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.envirocar.server.core.CarSimilarityService;
import org.envirocar.server.core.CarSimilarityServiceImpl;
import org.envirocar.server.core.ConfirmationLinkFactory;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.DataServiceImpl;
import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.FriendServiceImpl;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.GroupServiceImpl;
import org.envirocar.server.core.StatisticsService;
import org.envirocar.server.core.StatisticsServiceImpl;
import org.envirocar.server.core.TermsRepositoryImpl;
import org.envirocar.server.core.TermsRepository;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.UserServiceImpl;
import org.envirocar.server.core.UserStatisticService;
import org.envirocar.server.core.UserStatisticServiceImpl;
import org.envirocar.server.core.activities.ActivityListener;
import org.envirocar.server.core.util.BCryptPasswordEncoder;
import org.envirocar.server.core.util.GeodesicGeometryOperations;
import org.envirocar.server.core.util.GeometryOperations;
import org.envirocar.server.core.util.PasswordEncoder;
import org.joda.time.DateTimeZone;

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
        bind(UserStatisticService.class).to(UserStatisticServiceImpl.class);
        bind(ActivityListener.class).asEagerSingleton();
        bind(PasswordEncoder.class).to(BCryptPasswordEncoder.class);
        bind(GeometryOperations.class).to(GeodesicGeometryOperations.class);
        bind(CarSimilarityService.class).to(CarSimilarityServiceImpl.class);
        DateTimeZone.setDefault(DateTimeZone.UTC);
        requireBinding(ConfirmationLinkFactory.class);
        bind(TermsRepository.class).to(TermsRepositoryImpl.class).in(Scopes.SINGLETON);
    }
}
