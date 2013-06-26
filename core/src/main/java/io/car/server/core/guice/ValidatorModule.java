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
package io.car.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.validation.EntityValidator;
import io.car.server.core.validation.GroupValidator;
import io.car.server.core.validation.MeasurementValidator;
import io.car.server.core.validation.TrackValidator;
import io.car.server.core.validation.UserValidator;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ValidatorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<EntityValidator<User>>() {
        }).to(UserValidator.class);
        bind(new TypeLiteral<EntityValidator<Group>>() {
        }).to(GroupValidator.class);
        bind(new TypeLiteral<EntityValidator<Track>>() {
        }).to(TrackValidator.class);
        bind(new TypeLiteral<EntityValidator<Measurement>>() {
        }).to(MeasurementValidator.class);
    }
}
