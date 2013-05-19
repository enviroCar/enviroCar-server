/*
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
package io.car.server.mongo.dao;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.dao.TrackDao;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.mongo.entity.MongoTrack;

/**
 *
 * @author Arne de Wall <a.dewall@52north.org>
 *
 */
public class MongoTrackDao extends BasicDAO<MongoTrack, ObjectId> implements
        TrackDao {
    @Inject
    public MongoTrackDao(Datastore ds) {
        super(MongoTrack.class, ds);
    }

    @Override
    public Track getById(String id) {
        ObjectId oid;
        try {
            oid = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return get(oid);
    }

    @Override
    public Tracks getByUser(User user) {
        return fetch(createQuery().field(MongoTrack.USER).equal(user));
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
    public Tracks getByBbox(Geometry bbox) {
        //FIXME implement
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Tracks get() {
        return get(0);
    }

    @Override
    public Tracks get(int limit) {
        return fetch(createQuery().limit(limit).order(MongoTrack.CREATION_DATE));
    }

    protected Tracks fetch(Query<MongoTrack> q) {
        return new Tracks(find(q).fetch());
    }

    @Override
    public Tracks getBySensor(Sensor car) {
        return fetch(createQuery().field(MongoTrack.SENSOR).equal(car));
    }
}
