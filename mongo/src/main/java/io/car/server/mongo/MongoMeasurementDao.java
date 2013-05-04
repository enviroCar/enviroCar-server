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
package io.car.server.mongo;

import io.car.server.core.Measurement;
import io.car.server.core.Measurements;
import io.car.server.core.Track;
import io.car.server.core.db.MeasurementDao;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Arne de Wall
 *
 */
public class MongoMeasurementDao extends BasicDAO<MongoMeasurement, String> implements MeasurementDao {

	@Inject
	protected MongoMeasurementDao(Datastore ds) {
		super(MongoMeasurement.class, ds);
	}

	@Override
	public MongoMeasurement create(Measurement measurement) {
		return save(measurement);
	}

	@Override
	public MongoMeasurement save(Measurement measurement) {
		MongoMeasurement mongoMeasurement = (MongoMeasurement) measurement;
		save(mongoMeasurement);
		return mongoMeasurement;
	}

	@Override
	public void delete(Measurement measurement) {
		delete((MongoMeasurement) measurement);
	}

	@Override
	public Measurements getByPhenomenon(String phenomenon) {
		Query<MongoMeasurement> q = createQuery();
		q.field(MongoMeasurement.PHENOMENONS).equal(phenomenon);
		return fetch(q);
	}

	@Override
	public Measurements getByTrack(Track track) {
		return track.getMeasurements();
	}

	@Override
	public Measurements getByBbox(Geometry bbox) {
		// XXX TODO
		Coordinate[] coords = bbox.getBoundary().getCoordinates();
		return null;
	}

	@Override
	public Measurements getByBbox(double minx, double miny, double maxx,
			double maxy) {
		Query<MongoMeasurement> q = createQuery();
		q.field(MongoMeasurement.LOCATION).within(minx, miny, maxx, maxy);
		return fetch(q);
	}

	@Override
	public Measurements getAll() {
		return getAll(0);
	}

	@Override
	public Measurements getAll(int limit) {
		return fetch(createQuery().limit(limit).order(MongoMeasurement.CREATION_DATE));
	}

    protected Measurements fetch(Query<MongoMeasurement> q) {
        return new Measurements(find(q).fetch());
    }
}
