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
package org.envirocar.server.mongo;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import dev.morphia.AdvancedDatastore;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.Morphia;
import dev.morphia.converters.TypeConverter;
import dev.morphia.ext.guice.GuiceExtension;
import dev.morphia.mapping.MapperOptions;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Singleton
public class MongoDB implements AutoCloseable {
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
        if (username == null) {
            mongo = new MongoClient(new ServerAddress(host, port));
        } else {
            mongo = new MongoClient(new ServerAddress(host, port),
                                    MongoCredential.createCredential(username, database, password),
                                    MongoClientOptions.builder().build());
        }

        morphia = new Morphia();

        MapperOptions options = morphia.getMapper().getOptions();
        options.setObjectFactory(new CustomGuiceObjectFactory(options, injector));

        converters.forEach(morphia.getMapper().getConverters()::addConverter);
        mappedClasses.forEach(morphia::map);

        datastore = morphia.createDatastore(mongo, database);
        datastore.ensureIndexes();
        datastore.ensureCaps();
    }

    public MongoClient getMongoClient() {
        return this.mongo;
    }

    public Morphia getMorphia() {
        return this.morphia;
    }

    public AdvancedDatastore getDatastore() {
        return (AdvancedDatastore) this.datastore;
    }

    public <T> Key<T> key(T entity) {
        return entity == null ? null : getDatastore().getKey(entity);
    }

    public <T> DBRef ref(T entity) {
        return getDatastore().getMapper().keyToDBRef(key(entity));
    }

    @Override
    public void close() {
        this.mongo.close();
    }
}
