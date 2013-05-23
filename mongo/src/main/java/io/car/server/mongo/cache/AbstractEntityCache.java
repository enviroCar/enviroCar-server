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
package io.car.server.mongo.cache;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractEntityCache<T> implements EntityCache<T> {
    @Inject
    private Mapper mapr;
    @Inject
    private DB db;
    private final Class<T> type;
    private final LoadingCache<Key<T>, T> cache = CacheBuilder.newBuilder()
            .maximumSize(10000).build(new CacheLoaderImpl());

    public AbstractEntityCache(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get(Key<T> ref) {
        if (ref == null) {
            return null;
        }
        return cache.getUnchecked(ref);
    }

    @Override
    public void invalidate(T t) {
        if (t != null) {
            cache.invalidate(mapr.getKey(t));
        }
    }

    private class CacheLoaderImpl extends CacheLoader<Key<T>, T> {
        @Override
        @SuppressWarnings("unchecked")
        public T load(Key<T> key) {
            DBRef ref = mapr.keyToRef(key);
            DBObject dbo = db.getCollection(ref.getRef())
                    .findOne(new BasicDBObject(Mapper.ID_KEY, ref.getId()));
            return (T) mapr.fromDBObject(type, dbo, mapr.createEntityCache());
        }
    }
}
