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
package io.car.server.core.db;

import io.car.server.core.Measurement;
import io.car.server.core.Measurements;
import io.car.server.core.Track;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Arne de Wall
 *
 */
public interface MeasurementDao {
	Measurement create(Measurement measurement);
	Measurement save(Measurement measurement);
	void delete(Measurement measurement);
	
	Measurements getByPhenomenon(String string);
	Measurements getByTrack(Track track);
	Measurements getByBbox(Geometry bbox);
	Measurements getByBbox(double minx, double miny, double maxx, double maxy);
	
	Measurements getAll();
	Measurements getAll(int limit);

}
