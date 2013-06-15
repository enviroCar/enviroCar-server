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

import java.util.ArrayList;
import java.util.List;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.github.jmkgreen.morphia.query.UpdateResults;
import com.google.inject.Inject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Track;
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
    
    @Inject
    private GeometryConverter<BSONObject> geometryConv;
    
    @Inject
    protected MongoMeasurementDao(MongoDB mongoDB) {
        super(MongoMeasurement.class, mongoDB);
        this.mongoDB = mongoDB;
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
    public Measurements getByPhenomenon(String phenomenon, Pagination p) {
        //FIXME this one won't work
        return fetch(q().field(MongoMeasurement.PHENOMENONS).equal(phenomenon), p);
    }

    @Override
    public Measurements getByTrack(Track track, Pagination p) {
        return fetch(q().field(MongoMeasurement.TRACK)
                .equal(reference(track))
                .order(MongoMeasurement.TIME), p);
    }

    @Override
    public Measurements getByBbox(Geometry bbox, Pagination p) {
        // FIXME implement
        throw new UnsupportedOperationException("not yet implemented");
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
        return fetch(q()
                .field(MongoMeasurement.USER)
                .equal(reference(user))
                .order(MongoMeasurement.TIME), p);
    }

    void removeUser(MongoUser user) {
        UpdateResults<MongoMeasurement> result = update(
                q().field(MongoMeasurement.USER).equal(reference(user)),
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
                q().field(MongoMeasurement.TRACK).equal(reference(track)),
                up().unset(MongoMeasurement.TRACK));
        if (result.getHadError()) {
            log.error("Error removing track {} from measurements: {}",
                      track, result.getError());
        } else {
            log.debug("Removed track {} from {} measurements",
                      track, result.getUpdatedCount());
        }
    }
    
    void getTrackKeysByBbox(double minx, double miny, double maxx,
            double maxy, Pagination p) {
        fetch(q().field(MongoMeasurement.GEOMETRY)
                .within(minx, miny, maxx, maxy), p);
    }
    
    List<Key<MongoTrack>> getTrackKeysByBbox(Geometry polygon){
        Iterable<DBObject> res = aggregate(matchPolygon(polygon), project(), group()).results();
        List<Key<MongoTrack>> keys = new ArrayList<Key<MongoTrack>>();
        for(DBObject obj : res){
            BasicDBList list = (BasicDBList) obj.get(TRACKS);
            for(int i = 0; i < list.size(); i++){
                DBRef ref = (DBRef) list.get(i);
                Key<MongoTrack> key = getMapper().refToKey(ref);
                keys.add(key);
            }
        }
        return keys;
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
        return MongoUtils.match(MongoMeasurement.GEOMETRY, withinPolygon(polygon));
    }
    
    private DBObject withinPolygon(Geometry polygon){
        try {
            return MongoUtils.geoWithin(geometryConv.encode(polygon));
        } catch (GeometryConverterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DBObject project() {
        BasicDBObject fields = new BasicDBObject();
//        fields.put(MongoMeasurement.IDENTIFIER, 0);
//        fields.put(MongoMeasurement.PHENOMENONS, 0);
        fields.put(MongoMeasurement.TRACK, 1);
//        fields.put(MongoMeasurement.USER, 0);
        return MongoUtils.project(fields);
    }

    private DBObject group() {
        BasicDBObject fields = new BasicDBObject();
        fields.put(ID, TRACK_NAME_VALUE);
        fields.put(TRACKS, MongoUtils.addToSet(TRACK_VALUE));
        return MongoUtils.group(fields);
    }
}
