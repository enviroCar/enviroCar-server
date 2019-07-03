/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.envirocar.server.core.dao.TrackDao;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.mongo.util.MorphiaUtils;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MongoTrackDao extends AbstractMongoDao<ObjectId, MongoTrack, Tracks> implements TrackDao {
    private MongoMeasurementDao measurementDao;

    @Inject
    public MongoTrackDao(MongoDB mongoDB) {
        super(MongoTrack.class, mongoDB);
    }

    @Inject
    public void setMeasurementDao(MongoMeasurementDao measurementDao) {
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
    public Tracks get(TrackFilter request) {
        Query<MongoTrack> q = q();
        if (request.hasSpatialFilter()) {
            List<Key<MongoTrack>> keys = measurementDao
                    .getTrackKeysByBbox(new MeasurementFilter(
                            null, request.getSpatialFilter(), null, null));
            if (keys.isEmpty()) {
                return Tracks.none();
            }
            q.field(MongoTrack.ID).in(toIdList(keys));
        }
        if (request.hasTemporalFilter()) {
            MorphiaUtils.temporalFilter(q.field(MongoTrack.BEGIN),
                    q.field(MongoTrack.END),
                    request.getTemporalFilter());
        }
        return fetch(q, request.getPagination());
    }

    @Override
    protected Tracks createPaginatedIterable(Iterable<MongoTrack> i,
                                             Pagination p, long count) {
        return Tracks.from(i).withPagination(p).withElements(count).build();
    }

    @Override
    protected Iterable<MongoTrack> fetch(Query<MongoTrack> q) {
        return super.fetch(q.order(MongoTrack.RECENTLY_CREATED_ORDER));
    }

    @Override
    protected Tracks fetch(Query<MongoTrack> q, Pagination p) {
        return super.fetch(q.order(MongoTrack.RECENTLY_CREATED_ORDER), p);
    }

    private <T> List<Object> toIdList(List<Key<T>> keys) {
        List<Object> ids = Lists.newArrayListWithExpectedSize(keys.size());
        for (Key<T> key : keys) {
            ids.add(key.getId());
        }
        return ids;
    }

}
