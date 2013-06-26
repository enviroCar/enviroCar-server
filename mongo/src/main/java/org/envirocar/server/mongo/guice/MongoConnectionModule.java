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
package org.envirocar.server.mongo.guice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.envirocar.server.mongo.MongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Closeables;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoConnectionModule extends AbstractModule {
    private static final String PROPERTIES_FILE = "/mongo.properties";
    private static final Logger log = LoggerFactory
            .getLogger(MongoConnectionModule.class);

    @Override
    protected void configure() {
        String database = "enviroCar";
        String host = "localhost";
        int port = 27017;
        String user = null;
        char[] pass = null;
        try {
            Properties p = getProperties();
            if (p.containsKey(MongoDB.DATABASE_PROPERTY)) {
                database = p.getProperty(MongoDB.DATABASE_PROPERTY).trim();
            }
            if (p.containsKey(MongoDB.HOST_PROPERTY)) {
                host = p.getProperty(MongoDB.HOST_PROPERTY).trim();
            }
            if (p.containsKey(MongoDB.PORT_PROPERTY)) {
                port = Integer.valueOf(p.getProperty(MongoDB.PORT_PROPERTY)
                        .trim());
            }
            if (p.containsKey(MongoDB.USER_PROPERTY)) {
                user = p.getProperty(MongoDB.USER_PROPERTY).trim();
            }
            if (p.containsKey(MongoDB.PASS_PROPERTY)) {
                pass = p.getProperty(MongoDB.PASS_PROPERTY).trim().toCharArray();
            }
        } catch (IOException ex) {
            log
                    .error("Error reading mongo.properties. Using default values", ex);
        }
        bind(String.class).annotatedWith(Names.named(MongoDB.DATABASE_PROPERTY))
                .toInstance(database);
        bind(String.class).annotatedWith(Names.named(MongoDB.HOST_PROPERTY))
                .toInstance(host);
        bind(Integer.class).annotatedWith(Names.named(MongoDB.PORT_PROPERTY))
                .toInstance(port);
        bind(String.class).annotatedWith(Names.named(MongoDB.USER_PROPERTY))
                .toProvider(Providers.of(user));
        bind(char[].class).annotatedWith(Names.named(MongoDB.PASS_PROPERTY))
                .toProvider(Providers.of(pass));
    }

    private Properties getProperties() throws IOException {
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
}
