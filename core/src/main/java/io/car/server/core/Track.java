package io.car.server.core;

import com.vividsolutions.jts.geom.Polygon;

public interface Track extends BaseEntity {
	
	Track addMeasurements(Measurements measurements);
	Track addMeasurement(Measurement measurement);
	Track removeMeasurement(Measurement measurement);
	Measurements getMeasurements();	
	
	Polygon getBbox();
	Track setBbox(Polygon bbox);
	Track setBbox(double minx, double miny, double maxx, double maxy);
}
