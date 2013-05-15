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
package io.car.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.rest.CodingFactory;
import io.car.server.rest.EntityDecoder;
import io.car.server.rest.EntityEncoder;
import io.car.server.rest.coding.GroupCoder;
import io.car.server.rest.coding.GroupsCoder;
import io.car.server.rest.coding.MeasurementCoder;
import io.car.server.rest.coding.MeasurementsCoder;
import io.car.server.rest.coding.PhenomenonCoder;
import io.car.server.rest.coding.PhenomenonsCoder;
import io.car.server.rest.coding.SensorCoder;
import io.car.server.rest.coding.SensorsCoder;
import io.car.server.rest.coding.TrackCoder;
import io.car.server.rest.coding.TracksCoder;
import io.car.server.rest.coding.UserCoder;
import io.car.server.rest.coding.UsersCoder;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JerseyCodingModule extends AbstractModule {
    @Override
    protected void configure() {
        FactoryModuleBuilder fmb = new FactoryModuleBuilder();
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<User>>() {}, UserCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityDecoder<User>>() {}, UserCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Users>>() {}, UsersCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Sensor>>() {}, SensorCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityDecoder<Sensor>>() {}, SensorCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Sensors>>() {}, SensorsCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Track>>() {}, TrackCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityDecoder<Track>>() {}, TrackCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Tracks>>() {}, TracksCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Measurement>>() {}, MeasurementCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityDecoder<Measurement>>() {}, MeasurementCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Measurements>>() {}, MeasurementsCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Phenomenon>>() {}, PhenomenonCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityDecoder<Phenomenon>>() {}, PhenomenonCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Phenomenons>>() {}, PhenomenonsCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Group>>() {}, GroupCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityDecoder<Group>>() {}, GroupCoder.class);
        implementAndBind(fmb, new TypeLiteral<EntityEncoder<Groups>>() {}, GroupsCoder.class);
        install(fmb.build(CodingFactory.class));
    }

    protected <T> TypeLiteral<EntityEncoder<T>> encoder(Class<T> c) {
        return new TypeLiteral<EntityEncoder<T>>() {};
    }

    protected <T> TypeLiteral<EntityDecoder<T>> decoder(Class<T> c) {
        return new TypeLiteral<EntityDecoder<T>>() {};
    }

    protected <T> void implementAndBind(FactoryModuleBuilder fmb, TypeLiteral<T> source, Class<? extends T> target) {
        fmb.implement(source, target);
        bind(source).to(target);
    }
}
