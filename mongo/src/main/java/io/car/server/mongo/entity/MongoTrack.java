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
package io.car.server.mongo.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import io.car.server.core.Measurement;
import io.car.server.core.Measurements;
import io.car.server.core.Track;

@Entity("track")
public class MongoTrack extends MongoBaseEntity<MongoTrack> implements Track {

	static GeometryFactory factory = new GeometryFactory();

	public static final String ID = "id";
	public static final String BBOX = "bbox";
	public static final String MEASUREMENTS = "measurements";
	public static final String CAR = "car";

	@Embedded(BBOX)
	private Polygon bbox;
	@Property(CAR)
	private String car;

	@Reference(value = MEASUREMENTS, lazy = true)
	private List<MongoMeasurement> measurements = new ArrayList<MongoMeasurement>();

	@Override
	public MongoTrack addMeasurement(Measurement measurement) {
		this.measurements.add((MongoMeasurement) measurement);
		return this;
	}

	@Override
	public MongoTrack removeMeasurement(Measurement measurement) {
		this.measurements.remove((MongoMeasurement) measurement);
		return this;
	}

	@Override
	public Measurements getMeasurements() {
		return new Measurements(this.measurements);
	}

	public MongoTrack setMeasurements(Measurements measurements) {
		this.measurements.clear();
		for (Measurement m : measurements) {
			this.measurements.add((MongoMeasurement) m);
		}
		return this;
	}

	@Override
	public MongoTrack addMeasurements(Measurements measurements) {
		for (Measurement m : measurements) {
			this.measurements.add((MongoMeasurement) m);
		}
		return this;
	}

	@Override
	public MongoTrack setBbox(Polygon bbox) {
		this.bbox = bbox;
		return this;
	}

	@Override
	public Polygon getBbox() {
		return this.bbox;
	}

	@Override
	public MongoTrack setBbox(double minx, double miny, double maxx, double maxy) {
		// new Double[]{minx, miny, maxx, miny, maxx, maxy, minx, maxy, minx,
		// miny}
		Coordinate[] coords = new Coordinate[] { new Coordinate(minx, miny),
				new Coordinate(maxx, maxy) };
		this.bbox = factory.createPolygon(coords);
		return null;
	}

	@Override
	public Track setCar(String car) {
		this.car = car;
		return this;
	}

	@Override
	public String getCar() {
		return this.car;
	}

}
