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

import java.util.concurrent.ExecutionException;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

import io.car.server.core.dao.PhenomenonDao;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.entity.MongoPhenomenon;
import io.car.server.mongo.entity.MongoSensor;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class MongoPhenomenonDao extends BasicDAO<MongoPhenomenon, ObjectId>
        implements PhenomenonDao {
    private LoadingCache<String, MongoPhenomenon> cache = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
                .build(new CacheLoader<String, MongoPhenomenon>() {
        @Override
        public MongoPhenomenon load(String key) throws Exception {
            MongoPhenomenon phen =
                    createQuery().field(MongoSensor.NAME).equal(key).get();
            if (phen == null) {
                throw new PhenomenonNotFoundException();
            } else {
                return phen;
            }
        }
    });

    @Inject
    public MongoPhenomenonDao(Datastore datastore) {
        super(MongoPhenomenon.class, datastore);
    }

    @Override
    public MongoPhenomenon getByName(final String name) {
        try {
            return cache.get(name);
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof PhenomenonNotFoundException) {
                return null;
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Phenomenons get(Pagination p) {
        return fetch(createQuery(), p);
    }

    @Override
    public MongoPhenomenon create(Phenomenon phen) {
        MongoPhenomenon ph = (MongoPhenomenon) phen;
        save(ph);
        return ph;
    }
    protected Phenomenons fetch(Query<MongoPhenomenon> q, Pagination p) {
        long count = count(q);
        q.limit(p.getLimit()).offset(p.getOffset());
        Iterable<MongoPhenomenon> entities = find(q).fetch();
        return Phenomenons.from(entities).withElements(count).withPagination(p)
                .build();
    }

    private class PhenomenonNotFoundException extends Exception {
        private static final long serialVersionUID = 4584249990091719190L;
    }
}
