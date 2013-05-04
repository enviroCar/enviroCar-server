package io.car.server.mongo;

import io.car.server.core.MeasurementValue;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Property;

@Embedded("measurementvalue")
public class MongoMeasurementValue extends MongoBaseEntity implements MeasurementValue<Object> {
	public static final String VALUE = "value";
	
	@Property(VALUE)
	Object value;
	
	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public MeasurementValue<Object> setValue(Object value) {
		this.value = value;
		return this;
	}
	
}
