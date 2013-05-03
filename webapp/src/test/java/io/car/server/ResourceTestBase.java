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
package io.car.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.BeforeClass;

import com.google.inject.Inject;
import com.google.inject.servlet.GuiceFilter;
import com.mongodb.DB;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class ResourceTestBase {
    private static Server server;

    @BeforeClass
    public static void start() throws Exception {
        server = new Server(9998);
        server.setStopAtShutdown(true);
        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addEventListener(new ServletContextListener());
        sch.addServlet(DefaultServlet.class, "/");
        server.start();
    }

    private DB db;

    protected WebResource resource() {
        return Client.create().resource("http://localhost:9998");
    }

    protected void clearDatabase() {
        for (String name : db.getCollectionNames()) {
            db.getCollection(name).drop();
        }
    }

    @Inject
    public void setDb(DB db) {
        this.db = db;
    }
}
