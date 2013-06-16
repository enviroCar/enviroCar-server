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


import java.util.List;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.github.jmkgreen.morphia.query.MorphiaIterator;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.QueryImpl;
import com.github.jmkgreen.morphia.query.UpdateResults;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.filter.MeasurementFilter;
import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.User;
import io.car.server.core.exception.GeometryConverterException;
import io.car.server.core.util.GeometryConverter;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoMeasurement;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;
import io.car.server.mongo.util.MongoUtils;

/**
 *
 * @author Arne de Wall
 *
 */
public class MongoMeasurementDao extends AbstractMongoDao<ObjectId, MongoMeasurement, Measurements>
        implements MeasurementDao {
    public static final String ID = Mapper.ID_KEY;
    private static final Logger log = LoggerFactory
            .getLogger(MongoMeasurementDao.class);
    private static final String TRACKS = "tracks";
    private static final String TRACK_NAME_PATH = MongoUtils.path(TRACKS,
                                                                  MongoMeasurement.TRACK, MongoMeasurement.IDENTIFIER);
    private static final String TRACK_NAME_VALUE = MongoUtils
            .valueOf(TRACK_NAME_PATH);
    public static final String TRACK_VALUE = MongoUtils
            .valueOf(MongoMeasurement.TRACK);
    private final MongoDB mongoDB;
    private final GeometryConverter<BSONObject> geometryConverter;

    @Inject
    protected MongoMeasurementDao(MongoDB mongoDB,
                                  GeometryConverter<BSONObject> geometryConverter) {
        super(MongoMeasurement.class, mongoDB);
        this.mongoDB = mongoDB;
        this.geometryConverter = geometryConverter;
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
        delete(((MongoMeasurement) measurement).getId());
    }

    @Override
    public Measurements get(MeasurementFilter request) {
        if (request.hasGeometry()) {
            return getMongo(request);
        } else {
            return getMorphia(request);
        }
    }

    private Measurements getMorphia(MeasurementFilter request) {
        Query<MongoMeasurement> q = q().order(MongoMeasurement.TIME);;
        if (request.hasTrack()) {
            q.field(MongoMeasurement.TRACK)
                    .equal(key(request.getTrack()));
        }
        if (request.hasUser()) {
            q.field(MongoMeasurement.USER)
                    .equal(key(request.getUser()));
        }
        return fetch(q, request.getPagination());
    }

    private Measurements getMongo(MeasurementFilter request) {
        BasicDBObjectBuilder q = new BasicDBObjectBuilder();
        if (request.hasGeometry()) {
            q.add(MongoMeasurement.GEOMETRY,
                  withinPolygon(request.getGeometry()));
        }
        if (request.hasTrack()) {
            q.add(MongoMeasurement.TRACK, ref(request.getTrack()));
        }
        if (request.hasUser()) {
            q.add(MongoMeasurement.USER, ref(request.getUser()));
        }
        return query(q.get(), request.getPagination());
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

    void removeUser(MongoUser user) {
        UpdateResults<MongoMeasurement> result = update(
                q().field(MongoMeasurement.USER).equal(key(user)),
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
                q().field(MongoMeasurement.TRACK).equal(key(track)),
                up().unset(MongoMeasurement.TRACK));
        if (result.getHadError()) {
            log.error("Error removing track {} from measurements: {}",
                      track, result.getError());
        } else {
            log.debug("Removed track {} from {} measurements",
                      track, result.getUpdatedCount());
        }
    }

    List<Key<MongoTrack>> getTrackKeysByBbox(Geometry polygon) {
        return toKeyList(aggregate(matchPolygon(polygon),
                                   project(),
                                   group()).results());
    }

    List<Key<MongoTrack>> getTrackKeysByBbox(Geometry polygon, User user) {
        return toKeyList(aggregate(matchPolygon(polygon),
                                   matchUser(user),
                                   project(),
                                   group()).results());
    }

    private AggregationOutput aggregate(DBObject firstOp,
                                        DBObject... additionalOps) {
        AggregationOutput result = mongoDB.getDatastore()
                .getCollection(MongoMeasurement.class)
                .aggregate(firstOp, additionalOps);
        result.getCommandResult().throwOnError();
        return result;
    }

    private DBObject matchPolygon(Geometry polygon) {
        return MongoUtils
                .match(MongoMeasurement.GEOMETRY, withinPolygon(polygon));
    }

    private DBObject matchUser(User user) {
        return MongoUtils.match(MongoMeasurement.USER, ref(user));
    }

    private DBObject withinPolygon(Geometry polygon) {
        try {
            return MongoUtils.geoWithin(geometryConverter.encode(polygon));
        } catch (GeometryConverterException e) {
            throw new RuntimeException(e);
        }
    }

    private DBObject project() {
        return MongoUtils.project(new BasicDBObject(MongoMeasurement.TRACK, 1));
    }

    private DBObject group() {
        BasicDBObject fields = new BasicDBObject();
        fields.put(ID, TRACK_NAME_VALUE);
        fields.put(TRACKS, MongoUtils.addToSet(TRACK_VALUE));
        return MongoUtils.group(fields);
    }

    protected List<Key<MongoTrack>> toKeyList(
            Iterable<DBObject> res) {
        List<Key<MongoTrack>> keys = Lists.newLinkedList();
        for (DBObject obj : res) {
            BasicDBList list = (BasicDBList) obj.get(TRACKS);
            for (int i = 0; i < list.size(); i++) {
                DBRef ref = (DBRef) list.get(i);
                Key<MongoTrack> key = getMapper().refToKey(ref);
                keys.add(key);
            }
        }
        return keys;
    }

    private Measurements query(DBObject query, Pagination p) {
        final Mapper mapper = this.mongoDB.getMapper();
        final Datastore ds = this.mongoDB.getDatastore();
        final DBCollection coll = ds.getCollection(MongoMeasurement.class);

        DBCursor cursor = coll.find(query, null);
        long count = 0;

        cursor.setDecoderFactory(ds.getDecoderFact());
        if (p != null) {
            count = coll.count(query);
            if (p.getOffset() > 0) {
                cursor.skip(p.getOffset());
            }
            if (p.getLimit() > 0) {
                cursor.limit(p.getLimit());
            }
        }
        cursor.sort(QueryImpl.parseFieldsString(MongoMeasurement.TIME,
                                                MongoMeasurement.class,
                                                mapper, true));
        Iterable<MongoMeasurement> i =
                new MorphiaIterator<MongoMeasurement, MongoMeasurement>(
                cursor, mapper, MongoMeasurement.class, coll.getName(),
                mapper.createEntityCache());
        return createPaginatedIterable(i, p, count);
    }
}
