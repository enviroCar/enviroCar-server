package io.car.server.mongo;

import io.car.server.core.Track;
import io.car.server.core.Tracks;
import io.car.server.core.User;
import io.car.server.core.db.TrackDao;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Polygon;

public class MongoTrackDao extends BasicDAO<MongoTrack, String> implements TrackDao{

	@Inject
	public MongoTrackDao(Datastore ds) {
		super(MongoTrack.class, ds);
	}

	@Override
	public Track getByUser(User user) {
		// XXX not implemented
		return null;
	}

	@Override
	public Track create(Track track) {
		return save(track);
	}

	@Override
	public MongoTrack save(Track track) {
		MongoTrack mongoTrack = (MongoTrack) track;
		save(mongoTrack);
		return mongoTrack;
	}

	@Override
	public void delete(Track track) {
		delete((MongoTrack) track);
	}

	@Override
	public Tracks getByBbox(double minx, double miny, double maxx, double maxy) {
		Query<MongoTrack> q = createQuery();
		q.field("measurements.location").within(minx, miny, maxx, maxy);
		return fetch(q);
	}

	@Override
	public Tracks getByBbox(Polygon bbox) {
		// XXX not implemented yet
		Query<MongoTrack> q = createQuery();
		
		return null;
	}

	@Override
	public Tracks getAll() {
		return getAll(0);
	}

	@Override
	public Tracks getAll(int limit) {
		return fetch(createQuery().limit(limit).order(MongoTrack.CREATION_DATE));
	}
	
	protected Tracks fetch(Query<MongoTrack> q){
		return new Tracks(find(q).fetch());
	}

}
