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
package io.car.server.core.entities;

import com.vividsolutions.jts.geom.Geometry;


/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public interface Track extends BaseEntity {
    String BBOX = "bbox";
    String MEASUREMENTS = "measurements";
    String SENSOR = "sensor";
    String USER = "user";
    String NAME = "name";
    String DESCIPTION = "description";
    String getName();
    Track setName(String name);
    String getDescription();
    Track setDescription(String description);
    String getIdentifier();
    Track setIdentifier(String id);
    User getUser();
    Track setUser(User user);
	Measurements getMeasurements();	
	Track addMeasurement(Measurement measurement);
	Track addMeasurements(Measurements measurements);
    Track removeMeasurement(Measurement measurement);
    Sensor getSensor();
    Track setSensor(Sensor track);
	Geometry getBbox();
	Track setBbox(Geometry bbox);
	Track setBbox(double minx, double miny, double maxx, double maxy);	
}
