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
package io.car.server.mongo;

import java.util.Set;

import javax.annotation.Nullable;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.converters.DefaultConverters;
import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.github.jmkgreen.morphia.ext.guice.GuiceExtension;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
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
            mongo = new MongoClient(new ServerAddress(host, port));
            morphia = new Morphia();
            new GuiceExtension(morphia, injector);
            addConverters(converters);
            addMappedClasses(mappedClasses);
            datastore = morphia
                    .createDatastore(mongo, database, username, password);
            datastore.ensureIndexes();
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

    private void addConverters(Set<TypeConverter> converters) {
        DefaultConverters dc = morphia.getMapper().getConverters();
        for (TypeConverter tc : converters) {
            dc.addConverter(tc);
        }
    }

    private void addMappedClasses(Set<Class<?>> mappedClasses) {
        for (Class<?> c : mappedClasses) {
            morphia.getMapper().addMappedClass(c);
        }
    }
}
