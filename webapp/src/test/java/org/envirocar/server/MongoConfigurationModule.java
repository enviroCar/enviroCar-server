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
package org.envirocar.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.envirocar.server.mongo.MongoDB;

public class MongoConfigurationModule extends AbstractModule {
    private final String password;
    private final String username;
    private final String database;

    public MongoConfigurationModule() {
        this("enviroCar");
    }

    @Override
    protected void configure() {

    }

    public MongoConfigurationModule(String database) {
        this(database, null, null);
    }

    public MongoConfigurationModule(String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Provides
    @Named(MongoDB.DATABASE_PROPERTY)
    public String mongoDatabase() {
        return this.database;
    }

    @Provides
    @Named(MongoDB.USER_PROPERTY)
    public String mongoUsername() {
        return this.username;
    }

    @Provides
    @Named(MongoDB.PASS_PROPERTY)
    public String mongoPassword() {
        return this.password;
    }

    @Provides
    @Named(MongoDB.PORT_PROPERTY)
    public int mongoPort(MongoDatabase database) {
        return database.getMappedPort(27017);
    }

    @Provides
    @Named(MongoDB.HOST_PROPERTY)
    public String mongoHost(MongoDatabase database) {
        return database.getContainerIpAddress();
    }

}
