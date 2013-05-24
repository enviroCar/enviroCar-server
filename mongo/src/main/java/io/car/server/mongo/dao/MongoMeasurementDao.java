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

import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.entity.MongoMeasurement;

/**
 *
 * @author Arne de Wall
 *
 */
public class MongoMeasurementDao extends BasicDAO<MongoMeasurement, String>
        implements MeasurementDao {
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
    public Measurements getByPhenomenon(String phenomenon, Pagination p) {
        Query<MongoMeasurement> q = createQuery();
        q.field(MongoMeasurement.PHENOMENONS).equal(phenomenon);
        return fetch(q, p);
    }

    @Override
    public Measurements getByTrack(Track track, Pagination p) {
        Query<MongoMeasurement> q =
                createQuery().field(MongoMeasurement.TRACK).equal(track)
                .order(MongoMeasurement.TIME);
        return fetch(q, p);
    }

    @Override
    public Measurements getByBbox(Geometry bbox, Pagination p) {
        // FIXME implement
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Measurements getByBbox(double minx, double miny, double maxx,
                                  double maxy, Pagination p) {
        Query<MongoMeasurement> q = createQuery();
        q.field(MongoMeasurement.GEOMETRY).within(minx, miny, maxx, maxy);
        return fetch(q, p);
    }

    @Override
    public Measurements get(Pagination p) {
        return fetch(createQuery().order(MongoMeasurement.TIME), p);
    }

    @Override
    public Measurement getById(String id) {
        ObjectId oid;
        try {
            oid = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return find(createQuery().field(MongoMeasurement.ID).equal(oid)).get();
    }

    @Override
    public Measurements getByUser(User user, Pagination p) {
        return fetch(createQuery().field(MongoMeasurement.USER).equal(user)
                .order(MongoMeasurement.TIME), p);
    }

    protected Measurements fetch(Query<MongoMeasurement> q, Pagination p) {
        long count = 0;
        if (p != null) {
            count = count(q);
            q.limit(p.getLimit()).offset(p.getOffset());
        }
        Iterable<MongoMeasurement> entities = find(q).fetch();
        return Measurements.from(entities)
                .withPagination(p)
                .withElements(count)
                .build();
    }
}
