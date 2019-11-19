/*
 * Copyright (C) 2013-2019 The enviroCar project
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
import org.locationtech.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public interface Track extends BaseEntity {
    String getName();

    void setName(String name);

    default boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    String getDescription();

    void setDescription(String description);

    default boolean hasDescription() {
        return getDescription() != null && !getDescription().isEmpty();
    }

    String getIdentifier();

    void setIdentifier(String id);

    default boolean hasIdentifier() {
        return getIdentifier() != null && !getIdentifier().isEmpty();
    }

    Geometry getBoundingBox();

    void setBoundingBox(Geometry boundingBox);

    default boolean hasBoundingBox() {
        return getBoundingBox() != null && !getBoundingBox().isEmpty();
    }

    User getUser();

    void setUser(User user);

    default boolean hasUser() {
        return getUser() != null;
    }

    Sensor getSensor();

    void setSensor(Sensor track);

    default boolean hasSensor() {
        return getSensor() != null;
    }

    DateTime getBegin();

    void setBegin(DateTime begin);

    default boolean hasBegin() {
        return getBegin() != null;
    }

    DateTime getEnd();

    void setEnd(DateTime end);

    default boolean hasEnd() {
        return getEnd() != null;
    }

    void setTouVersion(String touVersion);

    String getTouVersion();

    void setObdDevice(String obdDevice);

    String getObdDevice();

    void setAppVersion(String appVersion);

    String getAppVersion();

    default boolean hasAppVersion() {
        return getAppVersion() != null && !getAppVersion().isEmpty();
    }

    default boolean hasObdDevice() {
        return getObdDevice() != null && !getObdDevice().isEmpty();
    }

    default boolean hasTouVersion() {
        return getTouVersion() != null && !getTouVersion().isEmpty();
    }

    double getLength();

    void setLength(double length);

    default boolean hasLength() {
        return getLength() > 0.0d;
    }
}
