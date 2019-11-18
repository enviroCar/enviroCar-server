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

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.envirocar.server.container.MongoContainer;
import org.envirocar.server.core.mail.Mailer;
import org.envirocar.server.core.mail.NoopMailer;
import org.envirocar.server.event.kafka.KafkaConstants;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.rest.decoding.json.JsonNodeMessageBodyReader;
import org.envirocar.server.rest.encoding.json.JsonNodeMessageBodyWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class EnviroCarServer extends ExternalResource {
    private static final Logger LOG = LoggerFactory.getLogger(EnviroCarServer.class);
    private final Server jettyServer;
    private final MongoContainer mongo;
    private final KafkaContainer kafka;
    private Injector injector;
    private final int port;

    public EnviroCarServer() {
        this(9998);
    }

    public EnviroCarServer(int port) {
        this.port = port;
        this.jettyServer = new Server(port);
        this.jettyServer.setStopAtShutdown(true);
        this.kafka = new KafkaContainer().withEmbeddedZookeeper();
        this.mongo = new MongoContainer();
    }

    @Override
    protected void before() throws Throwable {
        CheckedRunnable.runAll(kafka::start, mongo::start);

        ServletContextHandler sch = new ServletContextHandler(jettyServer, "/");
        sch.addFilter(GuiceFilter.class, "/*", null);
        ServletContextListener servletContextListener = new ServletContextListener(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(MongoContainer.class).toInstance(mongo);
                binder.bind(KafkaContainer.class).toInstance(kafka);
                binder.bind(Mailer.class).to(NoopMailer.class);
                binder.bind(String.class).annotatedWith(Names.named(MongoDB.DATABASE_PROPERTY))
                      .toInstance("enviroCar-Testing");
            }

            @Provides
            @Named(KafkaConstants.KAFKA_BROKERS)
            public String brokers(KafkaContainer kafka) {
                return kafka.getBootstrapServers();
            }

            @Provides
            @Named(MongoDB.HOST_PROPERTY)
            public String mongoHost(MongoContainer mongo) {
                return mongo.getHost();
            }

            @Provides
            @Named(MongoDB.PORT_PROPERTY)
            public int mongoPort(MongoContainer mongo) {
                return mongo.getPort();
            }
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

    @Override
    protected void after() throws Exception {
        CheckedRunnable.runAll(jettyServer::stop, mongo::stop, kafka::stop);
    }

    public Server getServer() {
        return jettyServer;
    }

    public Injector getInjector() {
        return injector;
    }

}
