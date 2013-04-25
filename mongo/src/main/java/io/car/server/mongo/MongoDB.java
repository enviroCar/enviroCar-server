/**
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.converters.DefaultConverters;
import com.github.jmkgreen.morphia.ext.guice.GuiceExtension;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.google.inject.Injector;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.car.server.mongo.convert.DateTimeConverter;
import io.car.server.mongo.convert.DurationConverter;
import io.car.server.mongo.convert.FileConverter;
import io.car.server.mongo.convert.URLConverter;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Singleton
public class MongoDB {
    public static final String PROPERTIES_FILE = "/mongo.properties";
    public static final String HOST_PROPERTY = "host";
    public static final String PORT_PROPERTY = "port";
    public static final String USER_PROPERTY = "user";
    public static final String PASS_PROPERTY = "pass";
    public static final String DATABASE_PROPERTY = "database";
    public static final String DEFAULT_HOST_NAME = "localhost";
    public static final String DEFAULT_PORT = "27017";
    public static final String DEFAULT_DATABASE_NAME = "car-io";

    private static Properties getProperties() throws IOException {
        InputStream is = MongoDB.class.getResourceAsStream(PROPERTIES_FILE);
        Properties p = new Properties();
        if (is != null) {
            try {
                p.load(is);
            } finally {
                Closeables.closeQuietly(is);
            }
        }
        return p;
    }

    private static String getHost(Properties p) {
        String host = p.getProperty(HOST_PROPERTY);
        host = (host == null || host.trim().isEmpty()) ? DEFAULT_HOST_NAME : host;
        return host;
    }

    private static int getPort(Properties p) {
        String port = p.getProperty(PORT_PROPERTY);
        port = (port == null || port.trim().isEmpty()) ? DEFAULT_PORT : port;
        return Integer.valueOf(port);
    }

    private static char[] getPassword(Properties p) {
        String property = p.getProperty(PASS_PROPERTY, null);
        return property == null ? null : property.toCharArray();
    }

    private static String getUser(Properties p) {
        return p.getProperty(USER_PROPERTY, null);
    }

    private static String getDatabase(Properties p) {
        String dbna = p.getProperty(DATABASE_PROPERTY);
        return (dbna == null || dbna.trim().isEmpty()) ? DEFAULT_DATABASE_NAME : dbna;
    }
    
    private final MongoClient mongo;
    private final Morphia morphia;
    private final Datastore datastore;

    @Inject
    public MongoDB(Injector injector) throws IOException {
        this(injector, getProperties());
    }

    public MongoDB(Injector injector, Properties p) {
        this(injector, getHost(p), getPort(p), getUser(p), getPassword(p), getDatabase(p));
    }

    public MongoDB(Injector injector, String host, int port, String username, char[] password, String database) {
        try {
            mongo = new MongoClient(new ServerAddress(host, port));
            morphia = new Morphia();
            new GuiceExtension(morphia, injector);
            for (Class<?> c : getMappedClasses()) {
                morphia.getMapper().addMappedClass(c);
            }
            addConverters(morphia);
            datastore = morphia.createDatastore(mongo, database, username, password);
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

    private void addConverters(Morphia m) {
        final DefaultConverters dc = m.getMapper().getConverters();
        dc.addConverter(DateTimeConverter.class);
        dc.addConverter(DurationConverter.class);
        dc.addConverter(FileConverter.class);
        dc.addConverter(URLConverter.class);
    }

    private Iterable<Class<?>> getMappedClasses() {
        return Sets.newHashSet(new Class<?>[] { MongoUser.class });
    }
}
