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
package io.car.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import io.car.server.rest.mapper.IllegalModificationExceptionMapper;
import io.car.server.rest.mapper.JsonValidationExceptionMapper;
import io.car.server.rest.mapper.ResourceAlreadyExistExceptionMapper;
import io.car.server.rest.mapper.ResourceNotFoundExceptionMapper;
import io.car.server.rest.mapper.ValidationExceptionMapper;
import io.car.server.rest.provider.ActivitiesProvider;
import io.car.server.rest.provider.ActivityProvider;
import io.car.server.rest.provider.GroupProvider;
import io.car.server.rest.provider.GroupsProvider;
import io.car.server.rest.provider.JsonNodeProvider;
import io.car.server.rest.provider.MeasurementProvider;
import io.car.server.rest.provider.MeasurementsProvider;
import io.car.server.rest.provider.PhenomenonProvider;
import io.car.server.rest.provider.PhenomenonsProvider;
import io.car.server.rest.provider.SensorProvider;
import io.car.server.rest.provider.SensorsProvider;
import io.car.server.rest.provider.StatisticProvider;
import io.car.server.rest.provider.StatisticsProvider;
import io.car.server.rest.provider.TrackProvider;
import io.car.server.rest.provider.TracksProvider;
import io.car.server.rest.provider.UserProvider;
import io.car.server.rest.provider.UserReferenceProvider;
import io.car.server.rest.provider.UsersProvider;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyProviderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GroupProvider.class).in(Scopes.SINGLETON);
        bind(GroupsProvider.class).in(Scopes.SINGLETON);
        bind(UserProvider.class).in(Scopes.SINGLETON);
        bind(UserReferenceProvider.class).in(Scopes.SINGLETON);
        bind(UsersProvider.class).in(Scopes.SINGLETON);
        bind(TrackProvider.class).in(Scopes.SINGLETON);
        bind(TracksProvider.class).in(Scopes.SINGLETON);
        bind(SensorProvider.class).in(Scopes.SINGLETON);
        bind(SensorsProvider.class).in(Scopes.SINGLETON);
        bind(PhenomenonProvider.class).in(Scopes.SINGLETON);
        bind(PhenomenonsProvider.class).in(Scopes.SINGLETON);
        bind(MeasurementProvider.class).in(Scopes.SINGLETON);
        bind(MeasurementsProvider.class).in(Scopes.SINGLETON);
        bind(StatisticsProvider.class).in(Scopes.SINGLETON);
        bind(StatisticProvider.class).in(Scopes.SINGLETON);
        bind(ActivitiesProvider.class).in(Scopes.SINGLETON);
        bind(ActivityProvider.class).in(Scopes.SINGLETON);
        bind(JsonNodeProvider.class).in(Scopes.SINGLETON);

        bind(IllegalModificationExceptionMapper.class).in(Scopes.SINGLETON);
        bind(ResourceNotFoundExceptionMapper.class).in(Scopes.SINGLETON);
        bind(ValidationExceptionMapper.class).in(Scopes.SINGLETON);
        bind(ResourceAlreadyExistExceptionMapper.class).in(Scopes.SINGLETON);
        bind(JsonValidationExceptionMapper.class).in(Scopes.SINGLETON);
    }
}
