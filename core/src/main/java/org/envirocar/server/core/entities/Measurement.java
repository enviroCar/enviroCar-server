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
package org.envirocar.server.core.entities;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public interface Measurement extends BaseEntity, Comparable<Measurement> {
    Geometry getGeometry();

    void setGeometry(Geometry geometry);

    boolean hasGeometry();

    String getIdentifier();

    void setIdentifier(String identifier);

    boolean hasIdentifier();

    DateTime getTime();

    void setTime(DateTime time);

    boolean hasTime();

    MeasurementValues getValues();

    void addValue(MeasurementValue value);

    void removeValue(MeasurementValue value);

    User getUser();

    void setUser(User user);

    boolean hasUser();

    Sensor getSensor();

    void setSensor(Sensor sensor);

    boolean hasSensor();

    void setTrack(Track track);

    Track getTrack();

    boolean hasTrack();
}
