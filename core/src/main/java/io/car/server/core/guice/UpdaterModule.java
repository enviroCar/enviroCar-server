/**
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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import io.car.server.core.update.EntityUpdater;
import io.car.server.core.update.GroupUpdater;
import io.car.server.core.update.MeasurementUpdater;
import io.car.server.core.update.TrackUpdater;
import io.car.server.core.update.UserUpdater;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class UpdaterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<EntityUpdater<User>>() {}).to(UserUpdater.class);
        bind(new TypeLiteral<EntityUpdater<Group>>() {}).to(GroupUpdater.class);
        bind(new TypeLiteral<EntityUpdater<Track>>() {}).to(TrackUpdater.class);
        bind(new TypeLiteral<EntityUpdater<Measurement>>() {}).to(MeasurementUpdater.class);
    }
}
