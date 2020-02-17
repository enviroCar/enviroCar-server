/*
 * Copyright (C) 2013-2020 The enviroCar project
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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.envirocar.server.core.SpatialFilter;
import org.envirocar.server.core.SpatialFilter.SpatialFilterOperator;
import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.GeometryConverterException;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.util.GeometryConverter;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.util.MongoUtils;
import org.envirocar.server.mongo.util.MorphiaUtils;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.mapping.Mapper;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaIterator;
import dev.morphia.query.Query;
import dev.morphia.query.QueryImpl;
import dev.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBDecoderFactory;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;
import org.locationtech.jts.geom.Geometry;

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
    private static final String TRACK_NAME_PATH = MongoUtils
            .path(TRACKS, MongoMeasurement.TRACK, MongoMeasurement.IDENTIFIER);
    private static final String TRACK_NAME_VALUE = MongoUtils.valueOf(TRACK_NAME_PATH);
    public static final String TRACK_VALUE = MongoUtils.valueOf(MongoMeasurement.TRACK);
    private final MongoDB mongoDB;
    private final GeometryConverter<BSONObject> geometryConverter;
    @Inject
    private MongoTrackDao trackDao;

    @Inject
    protected MongoMeasurementDao(MongoDB mongoDB, GeometryConverter<BSONObject> geometryConverter) {
        super(MongoMeasurement.class, mongoDB);
        this.mongoDB = mongoDB;
        this.geometryConverter = geometryConverter;
    }

    @Override
    public long getCount() {
        return count();
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
            Iterator<MongoMeasurement> it = q()
                    .field(MongoMeasurement.TRACK).equal(key(track))
                    .order(MongoMeasurement.TIME)
                    .fetch(new FindOptions().limit(1));
            MongoMeasurement newBegin = it.hasNext() ? it.next() : null;
            track.setBegin(newBegin == null ? null : newBegin.getTime());
            update = true;
        }
        if (track.hasEnd() && m.getTime().equals(track.getEnd())) {
            Iterator<MongoMeasurement> it = q()
                    .field(MongoMeasurement.TRACK).equal(key(track))
                    .order(MongoUtils.reverse(MongoMeasurement.TIME))
                    .fetch(new FindOptions().limit(1));
            MongoMeasurement newEnd = it.hasNext() ? it.next() : null;
            track.setEnd(newEnd == null ? null : newEnd.getTime());
            update = true;
        }
        if (update) {
            trackDao.save(track);
        }
    }

    @Override
    public Measurements get(MeasurementFilter request) {
        if (request.hasSpatialFilter()) {
            //needed because of lacking geo2d support in morphia
            return getMongo(request);
        } else {
            return getMorphia(request);
        }
    }

    private Measurements getMorphia(MeasurementFilter request) {
        Query<MongoMeasurement> q = q().order(MongoMeasurement.TIME);
        if (request.hasTrack()) {
            q.field(MongoMeasurement.TRACK).equal(key(request.getTrack()));
        }
        if (request.hasUser()) {
            q.field(MongoMeasurement.USER).equal(key(request.getUser()));
        }
        if (request.hasTemporalFilter()) {
            MorphiaUtils.temporalFilter(q.field(MongoMeasurement.TIME), request.getTemporalFilter());
        }
        return fetch(q, request.getPagination());
    }

    private Measurements getMongo(MeasurementFilter request) {
        BasicDBObjectBuilder q = new BasicDBObjectBuilder();
        if (request.hasSpatialFilter()) {
            SpatialFilter sf = request.getSpatialFilter();
            try {
                q.add(MongoMeasurement.GEOMETRY, MongoUtils.spatialFilter(sf, geometryConverter));
            } catch (GeometryConverterException e) {
                log.error("Error while applying spatial filter: {}", e.getLocalizedMessage());
            }
        }
        if (request.hasTrack()) {
            q.add(MongoMeasurement.TRACK, ref(request.getTrack()));
        }
        if (request.hasUser()) {
            q.add(MongoMeasurement.USER, ref(request.getUser()));
        }
        if (request.hasTemporalFilter()) {
            q.add(MongoMeasurement.TIME, MongoUtils.temporalFilter(request.getTemporalFilter()));
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
        UpdateResults result = update(
                q().field(MongoMeasurement.USER).equal(key(user)),
                up().unset(MongoMeasurement.USER));
        if (result.getWriteResult() != null && !result.getWriteResult().wasAcknowledged()) {
            log.error("Error removing user {} from measurement: {}", user, result.getWriteResult());
        } else {
            log.debug("Removed user {} from {} measurements", user, result.getUpdatedCount());
        }
    }

    void deleteUser(MongoUser user) {
        WriteResult result = delete(q().field(MongoTrack.USER).equal(key(user)));
        if (result.wasAcknowledged()) {
            log.debug("Removed user {} from {} measurement", user, result.getN());
        } else {
            log.error("Error removing user {} from measurement: {}", user, result);
        }
    }

    @Override
    protected Measurements createPaginatedIterable(Iterable<MongoMeasurement> i, Pagination p, long count) {
        return Measurements.from(i).withPagination(p).withElements(count).build();
    }

    void removeTrack(MongoTrack track) {
        WriteResult delete = delete(q().field(MongoMeasurement.TRACK).equal(key(track)));
        if (delete.wasAcknowledged()) {
            log.debug("Removed track {} from {} measurements", track, delete.getN());
        } else {
            log.error("Error removing track {} from measurements: {}", track, delete);
        }
    }

    List<Key<MongoTrack>> getTrackKeysByBbox(MeasurementFilter filter) {
        ArrayList<DBObject> ops = new ArrayList<>(4);
        if (filter.hasSpatialFilter()) {
            SpatialFilter sf = filter.getSpatialFilter();
            if (sf.getOperator() == SpatialFilterOperator.BBOX) {
                ops.add(matchGeometry(filter.getSpatialFilter().getGeom()));
            }
            //TODO add further spatial filters
        }
        if (filter.hasUser()) {
            ops.add(matchUser(filter.getUser()));
        }
        if (filter.hasTrack()) {
            ops.add(matchTrack(filter.getTrack()));
        }
        if (filter.hasTemporalFilter()) {
            ops.add(matchTime(filter.getTemporalFilter()));
        }

        ops.add(project());
        ops.add(group());

        return toKeyList(aggregate(ops));
    }

    private Iterable<DBObject> aggregate(List<DBObject> ops) {
        AggregationOptions options = AggregationOptions.builder().build();
        DBCollection collection = mongoDB.getDatastore().getCollection(MongoMeasurement.class);
        try (Cursor cursor = collection.aggregate(ops, options)) {
            LinkedList<DBObject> list = new LinkedList<>();
            cursor.forEachRemaining(list::add);
            return list;
        }
    }

    private DBObject matchGeometry(Geometry polygon) {
        return MongoUtils.match(MongoMeasurement.GEOMETRY, withinGeometry(polygon));
    }

    private DBObject matchUser(User user) {
        return MongoUtils.match(MongoMeasurement.USER, ref(user));
    }

    private DBObject matchTrack(Track track) {
        return MongoUtils.match(MongoMeasurement.TRACK, ref(track));
    }

    private DBObject matchTime(TemporalFilter tf) {
        return MongoUtils.match(MongoMeasurement.TIME, MongoUtils.temporalFilter(tf));
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

    protected List<Key<MongoTrack>> toKeyList(Iterable<DBObject> res) {
        return StreamSupport.stream(res.spliterator(), false)
                .map(obj -> (BasicDBList) obj.get(TRACKS))
                .flatMap(BasicDBList::stream)
                .map(refObj -> (DBRef) refObj)
                .map(getMapper()::<MongoTrack>refToKey)
                .collect(toList());
    }

    private Measurements query(DBObject query, Pagination p) {
        final Mapper mapper = this.mongoDB.getMapper();
        final Datastore ds = this.mongoDB.getDatastore();
        final DBCollection coll = ds.getCollection(MongoMeasurement.class);

        DBCursor cursor = coll.find(query);
        long count = 0;

        DBDecoderFactory dbDecoderFactory = coll.getDBDecoderFactory();
        if (dbDecoderFactory == null) {
            dbDecoderFactory = mongoDB.getMongoClient()
                    .getMongoClientOptions()
                    .getDbDecoderFactory();
        }
        cursor.setDecoderFactory(dbDecoderFactory);

        if (p != null) {
            count = coll.count(query);
            if (p.getBegin() > 0) {
                cursor.skip((int) p.getBegin());
            }
            if (p.getLimit() > 0) {
                cursor.limit((int) p.getLimit());
            }
        }
        cursor.sort(QueryImpl.parseFieldsString(MongoMeasurement.TIME,
                                                MongoMeasurement.class,
                                                mapper, true));
        Iterable<MongoMeasurement> i = new MorphiaIterator<>(ds,
                                                             cursor, mapper, MongoMeasurement.class, coll.getName(),
                                                             mapper.createEntityCache());
        return createPaginatedIterable(i, p, count);
    }
}
