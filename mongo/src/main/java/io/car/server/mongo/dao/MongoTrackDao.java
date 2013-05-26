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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateResults;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.TrackDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;

/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 * 
 */
public class MongoTrackDao
        extends AbstractMongoDao<ObjectId, MongoTrack, Tracks>
        implements TrackDao {
    private static final Logger log = LoggerFactory
            .getLogger(MongoTrackDao.class);
    private final MeasurementDao measurementDao;

    @Inject
    public MongoTrackDao(Datastore ds, MeasurementDao measurementDao) {
        super(MongoTrack.class, ds);
        this.measurementDao = measurementDao;
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
        return fetch(q().field(MongoTrack.USER).equal(user), p);
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
        Pagination page = new Pagination();
        do {
            Measurements measurements = measurementDao.getByTrack(track, page);
            for (Measurement m : measurements) {
                m.setTrack(null);
                measurementDao.save(m);
            }
        } while ((page = page.next(page.getSize())) != null);
        delete((MongoTrack) track);
    }

    @Override
    public Tracks getByBbox(double minx, double miny, double maxx, double maxy,
            Pagination p) {
        Query<MongoTrack> q = q().field("measurements.location")
                .within(minx, miny, maxx, maxy);
        return fetch(q, p);
    }

    @Override
    public Tracks getByBbox(Geometry bbox, Pagination p) {
        // FIXME implement
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Tracks get(Pagination p) {
        return fetch(q().order(MongoTrack.CREATION_DATE), p);
    }

    @Override
    public Tracks getBySensor(Sensor car, Pagination p) {
        return fetch(q().field(MongoTrack.SENSOR).equal(car), p);
    }

    @Override
    public User getUser(String track) {
        ObjectId oid;
        try {
            oid = new ObjectId(track);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return q().field(MongoTrack.ID).equal(oid)
                .retrievedFields(true, MongoTrack.USER).get().getUser();
    }

    @Override
    public Sensor getSensor(String track) {
        ObjectId oid;
        try {
            oid = new ObjectId(track);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return q().field(MongoTrack.ID).equal(oid)
                .retrievedFields(true, MongoTrack.SENSOR).get().getSensor();
    }

    @Override
    public void update(Track track) {
        update(
                ((MongoTrack) track).getId(),
                up().set(MongoTrack.LAST_MODIFIED, new DateTime()));
    }

    void removeUser(MongoUser user) {
        UpdateResults<MongoTrack> result = update(
                q().field(MongoTrack.USER).equal(user),
                up().unset(MongoTrack.USER));
        if (result.getHadError()) {
            log.error("Error removing user {} from tracks: {}",
                      user, result.getError());
        } else {
            log.debug("Removed user {} from {} tracks",
                      user, result.getUpdatedCount());
        }
    }

    @Override
    protected Tracks createPaginatedIterable(
            Iterable<MongoTrack> i,
            Pagination p, long count) {
        return Tracks.from(i).withPagination(p).withElements(count).build();
    }
}
