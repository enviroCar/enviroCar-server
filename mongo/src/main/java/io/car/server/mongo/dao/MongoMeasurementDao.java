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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.query.UpdateResults;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoMeasurement;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;

/**
 *
 * @author Arne de Wall
 *
 */
public class MongoMeasurementDao extends AbstractMongoDao<ObjectId, MongoMeasurement, Measurements>
        implements MeasurementDao {
    private static final Logger log = LoggerFactory
            .getLogger(MongoMeasurementDao.class);
    @Inject
    protected MongoMeasurementDao(MongoDB mongoDB) {
        super(MongoMeasurement.class, mongoDB);
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
        return fetch(q().field(MongoMeasurement.PHENOMENONS).equal(phenomenon), p);
    }

    @Override
    public Measurements getByTrack(Track track, Pagination p) {
        return fetch(q().field(MongoMeasurement.TRACK).equal(track)
                .order(MongoMeasurement.TIME), p);
    }

    @Override
    public Measurements getByBbox(Geometry bbox, Pagination p) {
        // FIXME implement
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Measurements getByBbox(double minx, double miny, double maxx,
                                  double maxy, Pagination p) {
        return fetch(q().field(MongoMeasurement.GEOMETRY)
                .within(minx, miny, maxx, maxy), p);
    }

    @Override
    public Measurements get(Pagination p) {
        return fetch(q().order(MongoMeasurement.TIME), p);
    }

    @Override
    public Measurement getById(String id) {
        ObjectId oid;
        try {
            oid = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return get(oid);
    }

    @Override
    public Measurements getByUser(User user, Pagination p) {
        return fetch(q().field(MongoMeasurement.USER).equal(user)
                .order(MongoMeasurement.TIME), p);
    }

    void removeUser(MongoUser user) {
        UpdateResults<MongoMeasurement> result = update(
                q().field(MongoMeasurement.USER).equal(user),
                up().unset(MongoMeasurement.USER));
        if (result.getHadError()) {
            log.error("Error removing user {} from measurement: {}",
                      user, result.getError());
        } else {
            log.debug("Removed user {} from {} measurements",
                      user, result.getUpdatedCount());
        }
    }

    @Override
    protected Measurements createPaginatedIterable(
            Iterable<MongoMeasurement> i,
            Pagination p, long count) {
        return Measurements.from(i).withPagination(p).withElements(count)
                .build();
    }

    void removeTrack(MongoTrack track) {
        UpdateResults<MongoMeasurement> result = update(
                q().field(MongoMeasurement.TRACK).equal(track),
                up().unset(MongoMeasurement.TRACK));
        if (result.getHadError()) {
            log.error("Error removing track {} from measurements: {}",
                      track, result.getError());
        } else {
            log.debug("Removed track {} from {} measurements",
                      track, result.getUpdatedCount());
        }
    }
}
