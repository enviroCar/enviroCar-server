package io.car.server.mongo;

import io.car.server.core.Measurement;
import io.car.server.core.Measurements;
import io.car.server.core.Track;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

@Entity("track")
public class MongoTrack extends MongoBaseEntity implements Track {

	static GeometryFactory factory = new GeometryFactory();

	public static final String ID = "id";
	public static final String BBOX = "bbox";
	public static final String MEASUREMENTS = "measurements";

	@Embedded
	@Property(BBOX)
	private Polygon bbox;

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

}
