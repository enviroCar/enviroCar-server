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
package org.envirocar.server.mongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.converters.DefaultConverters;
import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.github.jmkgreen.morphia.logging.MorphiaLoggerFactory;
import com.github.jmkgreen.morphia.logging.slf4j.SLF4JLogrImplFactory;
import com.github.jmkgreen.morphia.mapping.DefaultCreator;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import org.envirocar.server.mongo.entity.MongoMeasurement;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Singleton
public class MongoDB {
    public static final String MAPPED_CLASSES = "mappedClasses";
    public static final String HOST_PROPERTY = "host";
    public static final String PORT_PROPERTY = "port";
    public static final String USER_PROPERTY = "user";
    public static final String PASS_PROPERTY = "pass";
    public static final String DATABASE_PROPERTY = "database";
    private final MongoClient mongo;
    private final Morphia morphia;
    private final Datastore datastore;

    @Inject
    public MongoDB(Injector injector,
                   Set<TypeConverter> converters,
                   @Named(MAPPED_CLASSES) Set<Class<?>> mappedClasses,
                   @Named(HOST_PROPERTY) String host,
                   @Named(PORT_PROPERTY) int port,
                   @Named(DATABASE_PROPERTY) String database,
                   @Nullable @Named(USER_PROPERTY) String username,
                   @Nullable @Named(PASS_PROPERTY) char[] password) {
        try {
            MorphiaLoggerFactory.registerLogger(SLF4JLogrImplFactory.class);
            mongo = new MongoClient(new ServerAddress(host, port));
            morphia = new Morphia();
            morphia.getMapper().getOptions().objectFactory =
                    new CustomGuiceObjectFactory(new DefaultCreator(), injector);
            addConverters(converters);
            addMappedClasses(mappedClasses);
            datastore = morphia
                    .createDatastore(mongo, database, username, password);
            datastore.ensureIndexes();
            ensureIndexes();
            datastore.ensureCaps();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MongoClient getMongoClient() {
        return this.mongo;
    }

    public Morphia getMorphia() {
        return this.morphia;
    }

    public Datastore getDatastore() {
        return this.datastore;
    }

    public Mapper getMapper() {
        return getMorphia().getMapper();
    }

    private void addConverters(Set<TypeConverter> converters) {
        DefaultConverters dc = getMapper().getConverters();
        for (TypeConverter tc : converters) {
            dc.addConverter(tc);
        }
    }

    private void addMappedClasses(Set<Class<?>> mappedClasses) {
        for (Class<?> c : mappedClasses) {
            getMapper().addMappedClass(c);
        }
    }

    /*
     * FIXME remove this once 2dsphere indexes are supported by morphia in v1.3.0
     */
    private void ensureIndexes() {
        DBCollection collection = getDatastore()
                .getCollection(MongoMeasurement.class);
        collection.ensureIndex(new BasicDBObject(MongoMeasurement.GEOMETRY,
                                                 "2dsphere"));
    }

    public <T> T deref(Class<T> c, Key<T> key) {
        return key == null ? null : getDatastore().getByKey(c, key);
    }

    public <T> Iterable<T> deref(Class<T> c, Iterable<Key<T>> keys) {
        return deref(c, Lists.newArrayList(keys));
    }

    @SuppressWarnings("unchecked")
    protected <T> Iterable<T> deref(Class<T> clazz, List<Key<T>> keys) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        ListMultimap<String, Key<T>> kindMap = getKindMap(clazz, keys);
        List<Iterable<T>> fetched = Lists.newLinkedList();
        for (String kind : kindMap.keySet()) {
            List<Key<T>> kindKeys = kindMap.get(kind);
            List<Object> objIds = new ArrayList<Object>(kindKeys.size());
            Class<T> kindClass = clazz == null
                                 ? (Class<T>) kindKeys.get(0).getKindClass()
                                 : clazz;
            for (Key<T> key : kindKeys) {
                objIds.add(key.getId());
            }
            fetched.add(getDatastore()
                    .find(kind, kindClass)
                    .disableValidation()
                    .field(Mapper.ID_KEY)
                    .in(objIds)
                    .fetch());
        }
        return Iterables.concat(fetched);
    }

    public <T> Key<T> key(T entity) {
        return entity == null ? null : getMapper().getKey(entity);
    }

    public <T> DBRef ref(T entity) {
        return entity == null ? null : getMapper().keyToRef(key(entity));
    }

    protected <T> ListMultimap<String, Key<T>> getKindMap(Class<T> clazz,
                                                          List<Key<T>> keys) {
        ListMultimap<String, Key<T>> kindMap = LinkedListMultimap.create();
        String clazzKind = (clazz == null) ? null : getMapper()
                .getCollectionName(clazz);
        for (Key<T> key : keys) {
            getMapper().updateKind(key);
            String kind = key.getKind();

            if (clazzKind != null && !kind.equals(clazzKind)) {
                throw new IllegalArgumentException(String.format(
                        "Types are not equal (%s!=%s) for key and method parameter clazz",
                        clazz, key.getKindClass()));
            }
            kindMap.put(kind, key);
        }
        return kindMap;
    }
}
