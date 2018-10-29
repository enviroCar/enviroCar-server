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

import org.envirocar.server.core.util.pagination.Paginated;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoEntityBase;
import org.joda.time.DateTime;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.annotations.Version;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.cache.EntityCache;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateOpsImpl;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.WriteResult;
import com.mongodb.client.model.DBCollectionFindAndModifyOptions;

/**
 * TODO JavaDoc
 *
 * @param <K> the key type
 * @param <E> the entity type
 * @param <C> the collection type
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractMongoDao<K, E, C extends Paginated<? super E>> {
    private final BasicDAO<E, K> dao;
    private final MongoDB mongoDB;

    public AbstractMongoDao(Class<E> type, MongoDB mongoDB) {
        this.mongoDB = mongoDB;
        this.dao = new BasicDAO<>(type, this.mongoDB.getDatastore());
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    protected Query<E> q() {
        return dao.createQuery();
    }

    protected UpdateOperations<E> up() {
        return dao.createUpdateOperations();
    }

    protected E get(K key) {
        return dao.get(key);
    }

    protected long count() {
        return dao.count();
    }

    protected long count(Query<E> q) {
        return dao.count(q);
    }

    protected Key<E> save(E entity) {
        return dao.save(entity);
    }

    protected UpdateResults update(K key, UpdateOperations<E> ops) {
        return dao.update(q().field(Mapper.ID_KEY).equal(key), ops);
    }

    protected BasicDAO<E, K> dao() {
        return this.dao;
    }

    protected UpdateResults update(Query<E> q, UpdateOperations<E> ops) {
        return dao.update(q, ops);
    }

    protected Iterable<E> fetch(Query<E> q) {
        return q.fetch();
    }

    protected Iterable<E> fetch(Query<E> q, FindOptions options) {
        return dao.find(q).fetch(options);
    }

    protected C fetch(Query<E> q, Pagination p) {
        long count = 0;

        FindOptions findOptions = new FindOptions();
        if (p != null) {
            count = count(q);
            findOptions.skip((int) p.getBegin());
            findOptions.limit((int) p.getLimit());
        }
        return createPaginatedIterable(fetch(q,findOptions), p, count);
    }

    protected E findAndModify(Query<E> query, UpdateOperations<E> update, boolean returnNew) {
        Class<E> entityClass = query.getEntityClass();

        Mapper mapper = getMapper();
        Datastore datastore = getDatastore();
        DBCollection dbColl = datastore.getCollection(entityClass);
        MappedClass mc = mapper.getMappedClass(entityClass);

        DBObject queryObject = query.getQueryObject();

        if (update.isIsolated()) {
            queryObject.put("$isolated", true);
        }

        mc.getFieldsAnnotatedWith(Version.class).stream()
                .map(MappedField::getNameToStore)
                .forEach(field -> update.inc(field, 1));

        DBObject operations = ((UpdateOpsImpl) update).getOps();

        DBCollectionFindAndModifyOptions options = new DBCollectionFindAndModifyOptions()
                .upsert(false).remove(false).update(operations).returnNew(returnNew);

        DBObject dbObject = dbColl.findAndModify(queryObject, options);

        if (dbObject == null) {
            return null;
        }

        EntityCache entityCache = mapper.createEntityCache();

        E entity = mapper.fromDBObject(datastore, entityClass, dbObject, entityCache);

        return entity;
    }

    protected abstract C createPaginatedIterable(
            Iterable<E> i, Pagination p, long count);

    protected WriteResult delete(K id) {
        return dao.deleteById(id);
    }

    protected WriteResult delete(Query<E> q) {
        return dao.deleteByQuery(q);
    }

    @SuppressWarnings("unchecked")
    protected void updateTimestamp(E e) {
        update((K) this.mongoDB.getMapper().getId(e), up()
               .set(MongoEntityBase.LAST_MODIFIED, new DateTime()));
    }

    public <T> T deref(Class<T> c, Key<T> key) {
        return mongoDB.deref(c, key);
    }

    public <T> Iterable<T> deref(Class<T> c, Iterable<Key<T>> keys) {
        return mongoDB.deref(c, keys);
    }

    public <T> Key<T> key(T entity) {
        return mongoDB.key(entity);
    }

    public <T> DBRef ref(T entity) {
        return mongoDB.ref(entity);
    }

    public Datastore getDatastore() {
        return mongoDB.getDatastore();
    }

    public Mapper getMapper() {
        return mongoDB.getMapper();
    }
}
