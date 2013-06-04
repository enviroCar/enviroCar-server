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
package io.car.server.mongo.activities;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import io.car.server.core.activities.ActivityType;
import io.car.server.core.activities.MeasurementActivity;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.User;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoMeasurement;

public class MongoMeasurementActivity extends MongoActivity implements
        MeasurementActivity {
    public static final String MEASUREMENT = "measurement";
    @Property(MEASUREMENT)
    private Key<MongoMeasurement> measurement;
    @Transient
    private MongoMeasurement _measurement;

    @AssistedInject
    public MongoMeasurementActivity(MongoDB mongoDB, @Assisted ActivityType type,
                                    @Assisted User user,
                                    @Assisted Measurement measurement) {
        super(mongoDB, user, type);
        this._measurement = (MongoMeasurement) measurement;
        this.measurement = mongoDB.reference(this._measurement);
    }

    @Inject
    public MongoMeasurementActivity(MongoDB mongoDB) {
        this(mongoDB, null, null, null);
    }


    @Override
    public MongoMeasurement getMeasurement() {
        if (this._measurement == null) {
            this._measurement = getMongoDB()
                    .dereference(MongoMeasurement.class, this.measurement);
        }
        return this._measurement;
    }

    @Override
    public void setMeasurement(Measurement measurement) {
        this._measurement = (MongoMeasurement) measurement;
        this.measurement = getMongoDB().reference(this._measurement);
    }

    @Override
    public boolean hasMeasurement() {
        return getMeasurement() != null;
    }
}
