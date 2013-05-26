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
package io.car.server.mongo.entity;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;

import io.car.server.core.entities.Sensor;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("sensors")
public class MongoSensor extends MongoEntityBase<MongoSensor> implements Sensor {
    @Property(NAME)
    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Sensor setName(String name) {
        this.name = name;
        return this;
    }
}
