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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class EnviroCarServer {
    private static EnviroCarServer instance;
    private Server server;
    private Injector injector;

    public EnviroCarServer() throws Exception {
        server = new Server(9998);
        server.setStopAtShutdown(true);
        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addFilter(GuiceFilter.class, "/*", null);
        ServletContextListener servletContextListener =
                new ServletContextListener();
        injector = servletContextListener.getInjector();
        sch.addEventListener(servletContextListener);
        sch.addServlet(DefaultServlet.class, "/");
        server.start();
    }

    public static EnviroCarServer getInstance() throws Exception {
        return instance == null ? instance = new EnviroCarServer() : instance;
    }

    public Server getServer() {
        return server;
    }

    public Injector getInjector() {
        return injector;
    }
}
