package io.car.server.mongo;

import io.car.server.core.Measurement;
import io.car.server.core.Measurements;
import io.car.server.core.Track;
import io.car.server.core.db.MeasurementDao;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Envelope;

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
	public Measurements getByPhenomenon(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Measurements getByTrack(Track track) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Measurements getByBbox(Envelope bbox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Measurements getByBbox(double minx, double miny, double maxx,
			double maxy) {
		// TODO Auto-generated method stub
		return null;
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
