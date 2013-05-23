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
package io.car.server.core.dao;

import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public interface TrackDao {
    Track create(Track track);

    Track save(Track track);

    void delete(Track track);

    Track getById(String id);

    Tracks getByUser(User user);

    Tracks getBySensor(Sensor sensor);

    Tracks getByBbox(double minx, double miny, double maxx, double maxy);

    Tracks getByBbox(Geometry bbox);

    void addMeasurement(String track, Measurement m);

    Tracks get();

    Tracks get(int limit);

    User getUser(String track);

    Sensor getSensor(String track);
}
