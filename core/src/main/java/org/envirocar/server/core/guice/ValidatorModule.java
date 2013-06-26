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

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.validation.EntityValidator;
import org.envirocar.server.core.validation.GroupValidator;
import org.envirocar.server.core.validation.MeasurementValidator;
import org.envirocar.server.core.validation.TrackValidator;
import org.envirocar.server.core.validation.UserValidator;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

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
