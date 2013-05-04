package io.car.server.core.db;

import io.car.server.core.Measurement;
import io.car.server.core.MeasurementValue;
import io.car.server.core.Measurements;
import io.car.server.core.Track;

import com.vividsolutions.jts.geom.Envelope;

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
	Measurements getByBbox(Envelope bbox);
	Measurements getByBbox(double minx, double miny, double maxx, double maxy);
}
