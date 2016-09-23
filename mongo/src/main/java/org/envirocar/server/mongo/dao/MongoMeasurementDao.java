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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryImpl;
import org.mongodb.morphia.query.UpdateResults;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;
import com.vividsolutions.jts.geom.Geometry;
import java.util.Collections;

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
    private static final String TRACK_NAME_PATH = MongoUtils.path(TRACKS, MongoMeasurement.TRACK, MongoMeasurement.IDENTIFIER);
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
        if (request.hasSpatialFilter()) {
            SpatialFilter sf = request.getSpatialFilter();
            try {
                q.add(MongoMeasurement.GEOMETRY,
                      MongoUtils.spatialFilter(sf, geometryConverter));
            } catch (GeometryConverterException e) {
                log.error("Error while applying spatial filter: " + e
                          .getLocalizedMessage());
            }
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
        UpdateResults result = update(
                q().field(MongoMeasurement.USER).equal(key(user)),
                up().unset(MongoMeasurement.USER));
        if (result.getWriteResult() != null && !result.getWriteResult().wasAcknowledged()) {
            log.error("Error removing user {} from measurement: {}",
                      user, result.getWriteResult());
        } else {
            log.debug("Removed user {} from {} measurements",
                      user, result.getUpdatedCount());
        }
    }

    void deleteUser(MongoUser user) {
        WriteResult result = delete(q().field(MongoTrack.USER).equal(key(user)));
        if (result.wasAcknowledged()) {
            log.debug("Removed user {} from {} measurement",
                      user, result.getN());
        } else {
            log.error("Error removing user {} from measurement: {}",
                      user, result);
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
        WriteResult delete = delete(q().field(MongoMeasurement.TRACK).equal(key(track)));
        if (delete.wasAcknowledged()) {
            log.debug("Removed track {} from {} measurements",
                      track, delete.getN());
        } else {
            log.error("Error removing track {} from measurements: {}",
                      track, delete);
        }
    }

    List<Key<MongoTrack>> getTrackKeysByBbox(MeasurementFilter filter) {
        ArrayList<DBObject> filters = new ArrayList<>(4);
        if (filter.hasSpatialFilter()) {
        	SpatialFilter sf = filter.getSpatialFilter();
        	if (sf.getOperator()==SpatialFilterOperator.BBOX){
        		filters.add(matchGeometry(filter.getSpatialFilter().getGeom()));
        	}
        	//TODO add further spatial filters
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
        return result;
    }

    private DBObject matchGeometry(Geometry polygon) {
        return MongoUtils.match(MongoMeasurement.GEOMETRY,
                                withinGeometry(polygon));
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
            for (Object refObj : (BasicDBList) obj.get(TRACKS)) {
                DBRef ref = (DBRef) refObj;
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

        cursor.setDecoderFactory(coll.getDBDecoderFactory());
        if (p != null) {
            count = coll.count(query);
            if (p.getBegin()> 0) {
                cursor.skip((int) p.getBegin());
            }
            if (p.getLimit() > 0) {
                cursor.limit((int) p.getLimit());
            }
        }
        cursor.sort(QueryImpl.parseFieldsString(MongoMeasurement.TIME,
                                                MongoMeasurement.class,
                                                mapper, true,
                                                Collections.emptyList(), Collections.emptyList()));
        Iterable<MongoMeasurement> i =
                new MorphiaIterator<>(ds,
                cursor, mapper, MongoMeasurement.class, coll.getName(),
                mapper.createEntityCache());
        return createPaginatedIterable(i, p, count);
    }
}
