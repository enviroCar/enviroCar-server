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

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.MeasurementBase;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class MongoMeasurementBase extends MongoEntityBase implements
        MeasurementBase {
//    @Indexed(IndexDirection.GEO2DSPHERE)
    @Property(GEOMETRY)
    private Geometry geometry;
    @Indexed
    @Property(TIME)
    private DateTime time;

    @Override
    public Geometry getGeometry() {
        return this.geometry;
    }

    @Override
    public void setGeometry(Geometry location) {
        this.geometry = location;
    }

    @Override
    public String getIdentifier() {
        return getId().toString();
    }

    @Override
    public void setIdentifier(String identifier) {
        setId(new ObjectId(identifier));
    }

    @Override
    public DateTime getTime() {
        return this.time;
    }

    @Override
    public void setTime(DateTime time) {
        this.time = time;
    }
}
