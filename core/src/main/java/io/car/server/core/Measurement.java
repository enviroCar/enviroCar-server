package io.car.server.core;

import java.util.Map;

import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Arne de Wall
 *
 */
public interface Measurement extends BaseEntity, Comparable<Measurement>  {
	Map<String, MeasurementValue<?>> getPhenomenons();
	Measurement setPhenomenon(String phenomenon, MeasurementValue<?> value);
	Point getLocation();
	Measurement setLocation(Point location);
}
