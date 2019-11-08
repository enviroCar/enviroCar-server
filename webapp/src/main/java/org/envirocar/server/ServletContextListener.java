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

import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.util.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;
import java.util.Arrays;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ServletContextListener extends GuiceServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(ServletContextListener.class);
    private Injector injector;

    private Module overrides;

    public ServletContextListener() {
        this(null);
    }

    public ServletContextListener(Module overrides) {
        this.overrides = overrides;
    }

    @Override
    protected Injector getInjector() {

        if (injector == null) {
            Module module = new ServiceLoaderConfigurationModule();
            if (overrides != null) {
                module = Modules.override(module).with(overrides);
            }
            try {
                injector = Guice.createInjector(module);
            } catch (CreationException ex) {
                LOG.error("Error creating injector", ex);
                throw ex;
            }
        }

        return injector;

    }

    private void configureLogging() {
        java.util.logging.Logger rootLogger = java.util.logging.LogManager.getLogManager().getLogger("");
        Arrays.stream(rootLogger.getHandlers()).forEach(rootLogger::removeHandler);
        SLF4JBridgeHandler.install();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (injector != null) {
            ShutdownManager shutdownManager = injector.getInstance(ShutdownManager.class);
            if (shutdownManager != null) {
                shutdownManager.shutdownListeners();
            }
        }

        super.contextDestroyed(servletContextEvent);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        configureLogging();

        super.contextInitialized(servletContextEvent);
    }

}
