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
package org.envirocar.server;

import org.envirocar.server.rest.decoding.json.JsonNodeMessageBodyReader;
import org.envirocar.server.rest.encoding.json.JsonNodeMessageBodyWriter;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.inject.Inject;
import com.mongodb.DB;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ResourceTestBase {
    @Inject
    private DB db;
    @Inject
    private JsonNodeMessageBodyWriter jsonNodeWriter;
    @Inject
    private JsonNodeMessageBodyReader jsonNodeReader;

    @BeforeClass
    public static void start() throws Exception {
        EnviroCarServer.getInstance();
    }

    @Before
    public void inject() throws Exception {
        EnviroCarServer.getInstance().getInjector().injectMembers(this);
    }

    protected WebResource resource() {
        ClientConfig cc = new DefaultClientConfig();
        cc.getSingletons().add(jsonNodeReader);
        cc.getSingletons().add(jsonNodeWriter);
        return Client.create(cc).resource("http://localhost:9998");
    }

    protected void clearDatabase() {
        for (String name : db.getCollectionNames()) {
            db.getCollection(name).drop();
        }
    }
}
