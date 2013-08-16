/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.mongo.dao;


import java.util.ArrayList;
import java.util.List;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.GeometryConverterException;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.util.GeometryConverter;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.util.MongoUtils;
import org.envirocar.server.mongo.util.MorphiaUtils;
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

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall
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
    private MongoTrackDao trackDao;

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
    public void delete(Measurement m) {
        delete(((MongoMeasurement) m).getId());
         if (m.hasTrack()) {
            updateTrackTimeForDeletedMeasurement(m);
        }
    }

    public void updateTrackTimeForDeletedMeasurement(Measurement m) {
        boolean update = false;
        Track track = m.getTrack();
        if (track.hasBegin() && m.getTime().equals(track.getBegin())) {
            MongoMeasurement newBegin = q()
                    .field(MongoMeasurement.TRACK).equal(key(track))
                    .order(MongoMeasurement.TIME).limit(1).get();
            track.setBegin(newBegin == null ? null : newBegin.getTime());
            update = true;
        }
        if (track.hasEnd() && m.getTime().equals(track.getEnd())) {
            MongoMeasurement newEnd = q()
                    .field(MongoMeasurement.TRACK).equal(key(track))
                    .order(MongoUtils.reverse(MongoMeasurement.TIME))
                    .limit(1).get();
            track.setEnd(newEnd == null ? null : newEnd.getTime());
            update = true;
        }
        if (update) {
            trackDao.save(track);
        }
    }

    @Override
    public Measurements get(MeasurementFilter request) {
        if (request.hasGeometry()) {
            //needed because of lacking geo2d support in morphia
            return getMongo(request);
        } else {
            return getMorphia(request);
        }
    }

    private Measurements getMorphia(MeasurementFilter request) {
        Query<MongoMeasurement> q = q().order(MongoMeasurement.TIME);
        if (request.hasTrack()) {
            q.field(MongoMeasurement.TRACK)
                    .equal(key(request.getTrack()));
        }
        if (request.hasUser()) {
            q.field(MongoMeasurement.USER)
                    .equal(key(request.getUser()));
        }
        if (request.hasTemporalFilter()) {
            MorphiaUtils.temporalFilter(q.field(MongoMeasurement.TIME),
                                           request.getTemporalFilter());
        }
        return fetch(q, request.getPagination());
    }

    private Measurements getMongo(MeasurementFilter request) {
        BasicDBObjectBuilder q = new BasicDBObjectBuilder();
        if (request.hasGeometry()) {
            q.add(MongoMeasurement.GEOMETRY,
                  withinGeometry(request.getGeometry()));
        }
        if (request.hasTrack()) {
            q.add(MongoMeasurement.TRACK, ref(request.getTrack()));
        }
        if (request.hasUser()) {
            q.add(MongoMeasurement.USER, ref(request.getUser()));
        }
        if (request.hasTemporalFilter()) {
            q.add(MongoMeasurement.TIME,
                  MongoUtils.temporalFilter(request.getTemporalFilter()));
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

    List<Key<MongoTrack>> getTrackKeysByBbox(MeasurementFilter filter) {
        ArrayList<DBObject> filters = new ArrayList<DBObject>(4);
        if (filter.hasGeometry()) {
            filters.add(matchGeometry(filter.getGeometry()));
        }
        if (filter.hasUser()) {
            filters.add(matchUser(filter.getUser()));
        }
        if (filter.hasTrack()) {
            filters.add(matchTrack(filter.getTrack()));
        }
        if (filter.hasTemporalFilter()) {
            filters.add(matchTime(filter.getTemporalFilter()));
        }

        final AggregationOutput out;
        if (filters.isEmpty()) {
            out = aggregate(project(), group());
        } else {
            int size = filters.size();
            if (size == 1) {
                out = aggregate(filters.get(0), project(), group());
            } else {
                DBObject first = filters.get(0);
                DBObject[] other = new DBObject[size + 1];
                for (int i = 1; i < size; ++i) {
                    other[i - 1] = filters.get(i);
                }
                other[other.length - 2] = project();
                other[other.length - 1] = group();
                out = aggregate(first, other);
            }
        }
        return toKeyList(out.results());
    }
    
    private AggregationOutput aggregate(DBObject firstOp,
                                        DBObject... additionalOps) {
        AggregationOutput result = mongoDB.getDatastore()
                .getCollection(MongoMeasurement.class)
                .aggregate(firstOp, additionalOps);
        result.getCommandResult().throwOnError();
        return result;
    }

    private DBObject matchGeometry(Geometry polygon) {
        return MongoUtils
                .match(MongoMeasurement.GEOMETRY, withinGeometry(polygon));
    }

    private DBObject matchUser(User user) {
        return MongoUtils.match(MongoMeasurement.USER, ref(user));
    }

    private DBObject matchTrack(Track track) {
        return MongoUtils.match(MongoMeasurement.TRACK, ref(track));
    }

    private DBObject matchTime(TemporalFilter tf) {
        return MongoUtils.match(MongoMeasurement.TIME,
                                MongoUtils.temporalFilter(tf));
    }

    private DBObject withinGeometry(Geometry polygon) {
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
