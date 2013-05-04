package io.car.server.mongo;

import io.car.server.core.Measurement;
import io.car.server.core.MeasurementValue;

import java.util.Map;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Arne de Wall
 *
 */
@Entity("measurement")
public class MongoMeasurement extends MongoBaseEntity implements Measurement{
	public static final String PHENOMENONS = "phenomenons";
	public static final String VALUES = "values";
	public static final String LOCATION = "location";
	
	@Property(LOCATION)
	private Point location;
	
	@Embedded(PHENOMENONS)
	private Map<String, MeasurementValue<?>> phenomenonMap;

	@Override
	public Map<String, MeasurementValue<?>> getPhenomenons() {
		return this.phenomenonMap;
	}

	@Override
	public Measurement setPhenomenon(String phenomenon,
			MeasurementValue<?> value) {
		this.phenomenonMap.put(phenomenon, (MongoMeasurementValue) value);
		return this;
	}

	@Override
	public Point getLocation() {
		return this.location;
	}

	@Override
	public MongoMeasurement setLocation(Point location) {
		this.location = location;
		return this;
	}

	@Override
	public int compareTo(Measurement o) {
		return this.getCreationDate().compareTo(o.getCreationDate());
	}	
}
