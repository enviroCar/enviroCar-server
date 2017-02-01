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
package org.envirocar.server.mongo.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.DimensionedNumber;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.User;
import org.joda.time.DateTime;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.utils.IndexDirection;
import com.google.common.base.Strings;

/**
 * Mongo implementation of a {@link Fueling}.
 *
 * @author Christian Autermann
 */
@Entity("fuelings")
public class MongoFueling extends MongoEntityBase implements Fueling {
    public static final String FUEL_TYPE = "fuelType";
    public static final String COST = "cost";
    public static final String VOLUME = "volume";
    public static final String MILEAGE = "mileage";
    public static final String TIME = "time";
    public static final String MISSED_FUEL_STOP = "missedFuelStop";
    public static final String COMMENT = "comment";
    public static final String CAR = "car";
    public static final String USER = "user";

    @Id
    private ObjectId id = new ObjectId();
    @Property(FUEL_TYPE)
    private String fuelType;
    @Embedded(COST)
    private DimensionedNumber cost;
    @Embedded(VOLUME)
    private DimensionedNumber volume;
    @Embedded(MILEAGE)
    private DimensionedNumber mileage;
    @Property(COMMENT)
    private String comment;
    @Indexed(IndexDirection.DESC)
    @Property(TIME)
    private DateTime time;
    @Embedded(CAR)
    private MongoSensor car;
    @Indexed
    @Property(USER)
    private Key<MongoUser> user;
    @Transient
    private User _user;
    @Property(MISSED_FUEL_STOP)
    private boolean missedFuelStop;

    @Override
    public String getFuelType() {
        return this.fuelType;
    }

    @Override
    public void setFuelType(@Nullable String type) {
        this.fuelType = type;
    }

    @Override
    public boolean hasFuelType() {
        return this.fuelType != null;
    }

    @Override
    public DimensionedNumber getCost() {
        return this.cost;
    }

    @Override
    public void setCost(@Nullable DimensionedNumber cost) {
        this.cost = cost;
    }

    @Override
    public boolean hasCost() {
        return this.cost != null;
    }

    @Override
    public DimensionedNumber getVolume() {
        return this.volume;
    }

    @Override
    public void setVolume(@Nullable DimensionedNumber volume) {
        this.volume = volume;
    }

    @Override
    public boolean hasVolume() {
        return this.volume != null;
    }

    @Override
    public DimensionedNumber getMileage() {
        return this.mileage;
    }

    @Override
    public void setMileage(@Nullable DimensionedNumber mileage) {
        this.mileage = mileage;
    }

    @Override
    public boolean hasMileage() {
        return this.mileage != null;
    }

    @Override
    public DateTime getTime() {
        return this.time;
    }

    @Override
    public void setTime(@Nullable DateTime time) {
        this.time = time;
    }

    @Override
    public boolean hasTime() {
        return this.time != null;
    }

    @Override
    public boolean isMissedFuelStop() {
        return this.missedFuelStop;
    }

    @Override
    public void setMissedFuelStop(boolean missedFuelStop) {
        this.missedFuelStop = missedFuelStop;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(@Nullable String comment) {
        this.comment = Strings.emptyToNull(comment);
    }

    @Override
    public boolean hasComment() {
        return this.comment != null;
    }

    @Override
    public Sensor getCar() {
        return this.car;
    }

    @Override
    public void setCar(@Nullable Sensor sensor) {
        this.car = (MongoSensor) sensor;
    }

    @Override
    public boolean hasCar() {
        return this.car != null;
    }

    @Override
    public User getUser() {
        if (this._user == null) {
            this._user = getMongoDB().deref(MongoUser.class, this.user);
        }
        return this._user;
    }

    @Override
    public void setUser(@Nullable User user) {
        Key<MongoUser> nKey = getMongoDB().key((MongoUser) user);
        if (nKey == null || !nKey.equals(this.user)) {
            this.user = nKey;
            this._user = null;
        }
    }

    @Override
    public boolean hasUser() {
        return this.user != null;
    }

    @Override
    public String getIdentifier() {
        return this.id.toString();
    }

    @Override
    public void setIdentifier(@Nonnull String identifier) {
        this.id = new ObjectId(identifier);
    }

    @Override
    public boolean hasIdentifier() {
        return true;
    }

    /**
     * Get the identifier of this {@code Fueling} as an {@code ObjectId}.
     *
     * @return the identifier
     */
    public ObjectId getId() {
        return this.id;
    }

    /**
     * Sets the identifier of this {@code Fueling} as an {@code ObjectId}.
     *
     * @param id the identifier
     */
    public void setId(@Nonnull ObjectId id) {
        this.id = checkNotNull(id);
    }
}
