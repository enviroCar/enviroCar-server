package io.car.server.core;

public interface MeasurementValue<T> {
	T getValue();
	MeasurementValue<T> setValue(T value);
}
