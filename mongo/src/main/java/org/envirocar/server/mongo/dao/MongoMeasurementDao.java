/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.google.inject.Inject;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCursor;
import dev.morphia.Key;
import dev.morphia.geo.GeoJson;
import dev.morphia.query.Query;
import dev.morphia.query.Shape;
import dev.morphia.query.Sort;
import dev.morphia.query.UpdateResults;
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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall
 */
public class MongoMeasurementDao extends AbstractMongoDao<ObjectId, MongoMeasurement, Measurements>
        implements MeasurementDao {
    private static final String ID = "_id";
    private static final Logger LOG = LoggerFactory.getLogger(MongoMeasurementDao.class);
    private static final String TRACKS = "tracks";
    private static final String TRACK_NAME_PATH = MongoUtils.path(TRACKS, MongoMeasurement.TRACK,
                                                                  MongoMeasurement.IDENTIFIER);
    private static final String TRACK_NAME_VALUE = MongoUtils.valueOf(TRACK_NAME_PATH);
    private static final String TRACK_VALUE = MongoUtils.valueOf(MongoMeasurement.TRACK);
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

    private void updateTrackTimeForDeletedMeasurement(Measurement m) {
        boolean update = false;
        Track track = m.getTrack();
        if (track.hasBegin() && m.getTime().equals(track.getBegin())) {
            MongoMeasurement newBegin = q().field(MongoMeasurement.TRACK).equal(key(track))
                                           .order(Sort.ascending(MongoMeasurement.TIME))
                                           .first();
            track.setBegin(newBegin == null ? null : newBegin.getTime());
            update = true;
        }
        if (track.hasEnd() && m.getTime().equals(track.getEnd())) {
            MongoMeasurement newEnd = q().field(MongoMeasurement.TRACK).equal(key(track))
                                         .order(Sort.descending(MongoMeasurement.TIME))
                                         .first();
            track.setEnd(newEnd == null ? null : newEnd.getTime());
            update = true;
        }
        if (update) {
            trackDao.save(track);
        }
    }

    @Override
    public Measurements get(MeasurementFilter request) {
        Query<MongoMeasurement> q = q().order(Sort.ascending(MongoMeasurement.TIME));

        if (request.hasSpatialFilter()) {
            SpatialFilter sf = request.getSpatialFilter();
            switch (sf.getOperator()) {
                case BBOX:
                    Envelope e = sf.getGeom().getEnvelopeInternal();
                    q.field(MongoMeasurement.GEOMETRY).within(Shape.box(new Shape.Point(e.getMinX(), e.getMinY()),
                                                                        new Shape.Point(e.getMaxX(), e.getMaxY())));
                    break;
                case NEARPOINT:
                    Coordinate c = sf.getGeom().getCoordinate();
                    q.field(MongoMeasurement.GEOMETRY).nearSphere(GeoJson.point(c.getX(), c.getY()),
                                                                  sf.getParams().get(0), null);
                    break;
                default:
                    throw new InvalidParameterException(String.format("Spatial operator %s not supported!",
                                                                      sf.getOperator()));
            }

        }

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
            LOG.error("Error removing user {} from measurement: {}", user, result.getWriteResult());
        } else {
            LOG.debug("Removed user {} from {} measurements", user, result.getUpdatedCount());
        }
    }

    void deleteUser(MongoUser user) {
        WriteResult result = delete(q().field(MongoTrack.USER).equal(key(user)));
        if (result.wasAcknowledged()) {
            LOG.debug("Removed user {} from {} measurement", user, result.getN());
        } else {
            LOG.error("Error removing user {} from measurement: {}", user, result);
        }
    }

    @Override
    protected Measurements createPaginatedIterable(MongoCursor<MongoMeasurement> i, Pagination p, long count) {
        return Measurements.from(asCloseableIterator(i)).withPagination(p).withElements(count).build();
    }

    void removeTrack(MongoTrack track) {
        WriteResult delete = delete(q().field(MongoMeasurement.TRACK).equal(key(track)));
        if (delete.wasAcknowledged()) {
            LOG.debug("Removed track {} from {} measurements", track, delete.getN());
        } else {
            LOG.error("Error removing track {} from measurements: {}", track, delete);
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

    private List<Key<MongoTrack>> toKeyList(Iterable<DBObject> res) {
        return StreamSupport.stream(res.spliterator(), false)
                            .map(obj -> (BasicDBList) obj.get(TRACKS))
                            .flatMap(BasicDBList::stream)
                            .map(refObj -> (DBRef) refObj)
                            .map(this::refToKey)
                            .collect(toList());
    }

    private Key<MongoTrack> refToKey(DBRef ref) {
        return ref == null ? null : new Key<>(MongoTrack.class, ref.getCollectionName(), ref.getId());
    }
}
