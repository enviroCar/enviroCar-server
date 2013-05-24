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
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.util.Pagination;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public interface MeasurementDao {
    Measurement create(Measurement measurement);

    Measurement save(Measurement measurement);

    void delete(Measurement measurement);

    Measurement getById(String id);

    Measurements getByPhenomenon(String string, Pagination p);

    Measurements getByTrack(Track track, Pagination p);

    Measurements getByBbox(Geometry bbox, Pagination p);

    Measurements getByBbox(double minx, double miny, double maxx, double maxy,
                           Pagination p);

    Measurements get(Pagination p);

    Measurements getByUser(User user, Pagination p);
}
