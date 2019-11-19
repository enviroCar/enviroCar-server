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

import com.mongodb.DBRef;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCursor;
import dev.morphia.AdvancedDatastore;
import dev.morphia.FindAndModifyOptions;
import dev.morphia.Key;
import dev.morphia.mapping.Mapper;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.UpdateResults;
import org.envirocar.server.core.util.CloseableIterator;
import org.envirocar.server.core.util.PaginatedIterableImpl;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoEntityBase;
import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @param <K> the key type
 * @param <E> the entity type
 * @param <C> the collection type
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractMongoDao<K, E, C extends PaginatedIterableImpl<? super E>> {
    private final MongoDB mongoDB;
    private final Class<E> type;

    public AbstractMongoDao(Class<E> type, MongoDB mongoDB) {
        this.mongoDB = mongoDB;
        this.type = Objects.requireNonNull(type);
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    protected Query<E> q() {
        return getDatastore().createQuery(type);
    }

    protected UpdateOperations<E> up() {
        return getDatastore().createUpdateOperations(type);
    }

    protected E get(K key) {
        return q().field("_id").equal(key).first();
    }

    @Deprecated
    protected long count() {
        return count(q());
    }

    @Deprecated
    protected long count(Query<E> q) {
        return q.count();
    }

    protected Key<E> save(E entity) {
        return getDatastore().save(entity);
    }

    protected UpdateResults update(K key, UpdateOperations<E> ops) {
        return getDatastore().update(q().field("_id").equal(key), ops);
    }

    protected UpdateResults update(Query<E> q, UpdateOperations<E> ops) {
        return getDatastore().update(q, ops);
    }

    protected Iterator<E> fetch(Query<E> q) {
        return q.find();
    }

    protected MongoCursor<E> fetch(Query<E> q, FindOptions options) {
        return q.find(options);
    }

    protected C fetch(Query<E> q, Pagination p) {
        long count = 0;

        FindOptions findOptions = new FindOptions();
        if (p != null) {
            count = count(q);
            findOptions.skip((int) p.getBegin());
            findOptions.limit((int) p.getLimit());
        }
        return createPaginatedIterable(fetch(q, findOptions), p, count);
    }

    protected E findAndModify(Query<E> query, UpdateOperations<E> update, boolean returnNew) {
        return getDatastore().findAndModify(query, update, new FindAndModifyOptions()
                                                                   .returnNew(returnNew)
                                                                   .upsert(false));
    }

    protected WriteResult delete(K id) {
        return getDatastore().delete(q().field("_id").equal(id));
    }

    protected WriteResult delete(Query<E> q) {
        return getDatastore().delete(q);
    }

    @SuppressWarnings("unchecked")
    protected void updateTimestamp(E e) {
        final K key = (K) getDatastore().getKey(e).getId();
        final UpdateOperations<E> operations = up().set(MongoEntityBase.LAST_MODIFIED, new DateTime());
        update(key, operations);
    }

    public <T> Key<T> key(T entity) {
        return mongoDB.key(entity);
    }

    public <T> DBRef ref(T entity) {
        return mongoDB.ref(entity);
    }

    public AdvancedDatastore getDatastore() {
        return (AdvancedDatastore) mongoDB.getDatastore();
    }

    @Deprecated
    public Mapper getMapper() {
        return mongoDB.getDatastore().getMapper();
    }

    protected abstract C createPaginatedIterable(MongoCursor<E> i, Pagination p, long count);

    protected <T> CloseableIterator<T> asCloseableIterator(MongoCursor<T> cursor) {
        return new CloseableIterator<T>() {
            @Override
            public void close() {
                cursor.close();
            }

            @Override
            public boolean hasNext() {
                return cursor.hasNext();
            }

            @Override
            public T next() {
                return cursor.next();
            }
        };
    }
}
