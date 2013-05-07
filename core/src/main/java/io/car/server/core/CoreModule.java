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
package io.car.server.core;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Arne de Wall
 */
public class CoreModule extends AbstractModule {
    private static final Logger log = LoggerFactory.getLogger(CoreModule.class);
    @Override
    protected void configure() {
        log.debug("Installing CoreModule");
        bind(new TypeLiteral<EntityUpdater<User>>() {}).to(UserUpdater.class);
        bind(new TypeLiteral<EntityValidator<User>>() {}).to(UserValidator.class);
        bind(new TypeLiteral<EntityUpdater<Group>>() {}).to(GroupUpdater.class);
        bind(new TypeLiteral<EntityValidator<Group>>() {}).to(GroupValidator.class);
        bind(new TypeLiteral<EntityUpdater<Track>>() {}).to(TrackUpdater.class);
        bind(new TypeLiteral<EntityValidator<Track>>() {}).to(TrackValidator.class);
        bind(new TypeLiteral<EntityUpdater<Measurement>>() {}).to(MeasurementUpdater.class);
        bind(new TypeLiteral<EntityValidator<Measurement>>() {}).to(MeasurementValidator.class);
        bind(Service.class);
        bind(PasswordEncoder.class).to(BCryptPasswordEncoder.class);
        bind(GeometryFactory.class)
                .toInstance(new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), 4326));
        bind(DateTimeFormatter.class).toInstance(ISODateTimeFormat.dateTimeNoMillis());
    }
}
