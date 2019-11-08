/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.core.entities;

import org.joda.time.DateTime;

/**
 * Entity to represent a fueling stop.
 *
 * @author Christian Autermann
 */
public interface Fueling extends BaseEntity {

    /**
     * @return the fuel type
     */
    String getFuelType();

    /**
     * Sets the type of this {@code Fueling}.
     *
     * @param type the type
     */
    void setFuelType(String type);

    /**
     * Checks if this {@code Fueling} has a fuel type.
     *
     * @return if this {@code Fueling} has a fuel type
     */
    boolean hasFuelType();

    /**
     * Gets the costs of this {@code Fueling}.
     *
     * @return the costs
     */
    DimensionedNumber getCost();

    /**
     * Sets the costs of this {@code Fueling}.
     *
     * @param cost the costs
     */
    void setCost(DimensionedNumber cost);

    /**
     * Checks if this {@code Fueling} has a cost.
     *
     * @return if this {@code Fueling} has a cost
     */
    boolean hasCost();

    /**
     * Gets the amount of this {@code Fueling}.
     *
     * @return the amount
     */
    DimensionedNumber getVolume();

    /**
     * Sets the amount of this {@code Fueling}.
     *
     * @param volume the volume
     */
    void setVolume(DimensionedNumber volume);

    /**
     * Checks if this {@code Fueling} has a volume.
     *
     * @return if this {@code Fueling} has a volume
     */
    boolean hasVolume();

    /**
     * Gets the mileage of this {@code Fueling}.
     *
     * @return the mileage
     */
    DimensionedNumber getMileage();

    /**
     * Sets the mileage of this {@code Fueling}.
     *
     * @param mileage the mileage
     */
    void setMileage(DimensionedNumber mileage);

    /**
     * Checks if this {@code Fueling} has a mileage.
     *
     * @return if this {@code Fueling} has a mileage
     */
    boolean hasMileage();

    /**
     * Gets the time of this {@code Fueling}.
     *
     * @return the time
     */
    DateTime getTime();

    /**
     * Sets the time of this {@code Fueling}.
     *
     * @param time the time
     */
    void setTime(DateTime time);

    /**
     * Checks if this {@code Fueling} has a time.
     *
     * @return if this {@code Fueling} has a time
     */
    boolean hasTime();

    /**
     * Checks if {@code Fueling} is is a missed fuel stop.
     *
     * @return if {@code Fueling} is is a missed fuel stop
     */
    boolean isMissedFuelStop();

    /**
     * Sets if {@code Fueling} is is a missed fuel stop.
     *
     * @param missedFuelStop if {@code Fueling} is is a missed fuel stop
     */
    void setMissedFuelStop(boolean missedFuelStop);

    /**
     * @return if this is a partial fueling
     */
    boolean isPartialFueling();

    /**
     * @param partialFueling if this is a partial fueling
     */
    void setPartialFueling(boolean partialFueling);

    /**
     * Gets the comment of this {@code Fueling}.
     *
     * @return the time
     */
    String getComment();

    /**
     * Sets the comment of this {@code Fueling}.
     *
     * @param comment the time
     */
    void setComment(String comment);

    /**
     * Checks if this {@code Fueling} has a comment.
     *
     * @return if this {@code Fueling} has a comment
     */
    boolean hasComment();

    /**
     * Gets the car of this {@code Fueling}.
     *
     * @return the car
     */
    Sensor getCar();

    /**
     * Sets the car of this {@code Fueling}.
     *
     * @param sensor the car
     */
    void setCar(Sensor sensor);

    /**
     * Checks if this {@code Fueling} has a car.
     *
     * @return if this {@code Fueling} has a car
     */
    boolean hasCar();

    /**
     * Gets the {@code User} of this {@code Fueling}.
     *
     * @return the time
     */
    User getUser();

    /**
     * Sets the {@code User} of this {@code Fueling}.
     *
     * @param user the user
     */
    void setUser(User user);

    /**
     * Checks if this {@code Fueling} has a user.
     *
     * @return if this {@code Fueling} has a user
     */
    boolean hasUser();

    /**
     * Gets the identifier of this {@code Fueling}.
     *
     * @return the identifier
     */
    String getIdentifier();

    /**
     * Sets the identifier of this {@code Fueling}.
     *
     * @param identifier the identifier
     */
    void setIdentifier(String identifier);

    /**
     * Checks if this {@code Fueling} has a identifier.
     *
     * @return if this {@code Fueling} has a identifier
     */
    boolean hasIdentifier();

}
