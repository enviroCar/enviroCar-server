package io.car.server.core.db;

import io.car.server.core.Track;
import io.car.server.core.Tracks;
import io.car.server.core.User;

import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 * @author Arne de Wall
 *
 */
public interface TrackDao {
	Track getByUser(User user);

	Track create(Track track);
	Track save(Track track);
	void delete(Track track);
	
	Tracks getByBbox(double minx, double miny, double maxx, double maxy);
	Tracks getByBbox(Polygon bbox);
	Tracks getAll();
	Tracks getAll(int limit);
}
