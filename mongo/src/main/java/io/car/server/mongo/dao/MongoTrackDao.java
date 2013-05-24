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
import org.joda.time.DateTime;

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
import io.car.server.core.util.Pagination;
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
    public Tracks getByUser(User user, Pagination p) {
        return fetch(createQuery().field(MongoTrack.USER).equal(user), p);
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
        // FIXME remove track reference from measurements
        delete((MongoTrack) track);
    }

    @Override
    public Tracks getByBbox(double minx, double miny, double maxx, double maxy,
                            Pagination p) {
        Query<MongoTrack> q = createQuery();
        q.field("measurements.location").within(minx, miny, maxx, maxy);
        return fetch(q, p);
    }

    @Override
    public Tracks getByBbox(Geometry bbox, Pagination p) {
        //FIXME implement
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Tracks get(Pagination p) {
        return fetch(createQuery().order(MongoTrack.CREATION_DATE), p);
    }

    @Override
    public Tracks getBySensor(Sensor car, Pagination p) {
        return fetch(createQuery().field(MongoTrack.SENSOR).equal(car), p);
    }

    @Override
    public User getUser(String track) {
        ObjectId oid;
        try {
            oid = new ObjectId(track);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return createQuery().field(MongoTrack.ID).equal(oid)
                .retrievedFields(true, MongoTrack.USER).get().getUser();
    }

    protected Tracks fetch(Query<MongoTrack> q, Pagination p) {
        long count = count(q);
        q.limit(p.getLimit()).offset(p.getOffset());
        Iterable<MongoTrack> entities = find(q).fetch();
        return Tracks.from(entities).withElements(count).withPagination(p)
                .build();
    }

    @Override
    public Sensor getSensor(String track) {
        ObjectId oid;
        try {
            oid = new ObjectId(track);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return createQuery().field(MongoTrack.ID).equal(oid)
                .retrievedFields(true, MongoTrack.SENSOR).get().getSensor();
    }

    @Override
    public void update(Track track) {
        getDatastore()
                .update((MongoTrack) track, createUpdateOperations()
                .set(MongoTrack.LAST_MODIFIED, new DateTime()));
    }
}
