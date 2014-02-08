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

import org.bson.types.ObjectId;
import org.envirocar.server.core.dao.FuelingDao;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Fuelings;
import org.envirocar.server.core.filter.FuelingFilter;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoFueling;
import org.envirocar.server.mongo.util.MorphiaUtils;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;

/**
 * Mongo implementation of a {@link FuelingDao}.
 *
 * @author Christian Autermann
 */
public class MongoFuelingDao extends AbstractMongoDao<ObjectId, MongoFueling, Fuelings>
        implements FuelingDao {

    @Inject
    public MongoFuelingDao(MongoDB mongoDB) {
        super(MongoFueling.class, mongoDB);
    }

    @Override
    protected Fuelings createPaginatedIterable(Iterable<MongoFueling> i,
                                               Pagination p, long count) {
        return Fuelings.from(i).withPagination(p).withElements(count).build();
    }

    @Override
    public Fueling getById(String id) {
        ObjectId oid;
        try {
            oid = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return get(oid);
    }

    @Override
    public Fueling create(Fueling fueling) {
        MongoFueling mf = (MongoFueling) fueling;
        save(mf);
        return mf;
    }

    @Override
    public Fuelings get(FuelingFilter request) {
        Query<MongoFueling> q = q();
        if (request.hasTemporalFilter()) {
            MorphiaUtils.temporalFilter(q.field(MongoFueling.TIME),
                                        request.getTemporalFilter());
        }
        if (request.hasUser()) {
            q.field(MongoFueling.TIME).equal(key(request.getUser()));
        }
        return fetch(q, request.getPagination());
    }

}
