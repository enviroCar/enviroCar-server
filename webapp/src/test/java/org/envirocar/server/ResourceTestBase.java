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
package org.envirocar.server;

import com.google.inject.Inject;
import com.mongodb.DB;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.ClassRule;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ResourceTestBase {
    @Inject
    private DB db;
    @ClassRule
    public static EnviroCarServer server = new EnviroCarServer();

    @Before
    public void inject() {
        server.getInjector().injectMembers(this);
    }

    protected WebResource resource() {
        return server.resource();
    }

    protected void clearDatabase() {
        for (String name : db.getCollectionNames()) {
            db.getCollection(name).drop();
        }
    }
}
