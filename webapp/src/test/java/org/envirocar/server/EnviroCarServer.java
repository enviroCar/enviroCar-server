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

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.envirocar.server.core.mail.NoopMailerModule;
import org.envirocar.server.rest.decoding.json.JsonNodeMessageBodyReader;
import org.envirocar.server.rest.encoding.json.JsonNodeMessageBodyWriter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class EnviroCarServer extends ExternalResource {
    private static EnviroCarServer instance;
    private final Server jettyServer;
    private final MongoDatabase mongoDatabase;
    private Injector injector;
    private final int port;

    public EnviroCarServer() {
        this(9998);
    }

    public EnviroCarServer(int port) {
        this.mongoDatabase = new MongoDatabase();
        this.port = port;
        this.jettyServer = new Server(port);
        this.jettyServer.setStopAtShutdown(true);
    }

    @Override
    protected void before() throws Throwable {
        this.mongoDatabase.start();


        ServletContextHandler sch = new ServletContextHandler(jettyServer, "/");
        sch.addFilter(GuiceFilter.class, "/*", null);
        ServletContextListener servletContextListener = new ServletContextListener(binder -> {
            binder.bind(MongoDatabase.class).toInstance(mongoDatabase);
            binder.install(new NoopMailerModule());
            binder.install(new MongoConfigurationModule("enviroCar-Testing"));
        });
        injector = servletContextListener.getInjector();
        sch.addEventListener(servletContextListener);
        sch.addServlet(DefaultServlet.class, "/");


        jettyServer.start();
    }


    public WebResource resource() {
        ClientConfig cc = new DefaultClientConfig();
        cc.getSingletons().add(injector.getInstance(JsonNodeMessageBodyReader.class));
        cc.getSingletons().add(injector.getInstance(JsonNodeMessageBodyWriter.class));
        return Client.create(cc).resource(String.format("http://localhost:%d", this.port));
    }


    public int getPort() {
        return this.port;
    }

    @Override
    protected void after() throws Throwable {
        try {
            this.jettyServer.stop();
        } catch (Throwable t1) {
            try {
                this.mongoDatabase.stop();
            } catch (Throwable t2) {
                t1.addSuppressed(t2);
            }
            throw t1;
        }
        this.mongoDatabase.stop();
    }

    public Server getServer() {
        return jettyServer;
    }

    public Injector getInjector() {
        return injector;
    }

}
