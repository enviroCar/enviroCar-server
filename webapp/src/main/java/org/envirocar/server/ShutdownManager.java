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
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.server;

import java.util.Set;

import org.envirocar.server.core.guice.ResourceShutdownListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;

/**
 * Manager class to shutdown resources via ResourceShutdownListener instances.
 */
public class ShutdownManager {
    private static final Logger LOG = LoggerFactory.getLogger(ShutdownManager.class);
    private final Set<ResourceShutdownListener> listeners;

    @Inject
    public ShutdownManager(Set<ResourceShutdownListener> listeners) {
        this.listeners = listeners;
    }

    void shutdownListeners() {
        LOG.info(String.format("shutting down resources of %s listeners", listeners.size()));
        listeners.forEach(listener -> {
            try {
                listener.shutdownResources();
            } catch (Throwable e) {
                LOG.warn("Could not shutdown " + listener.getClass(), e);
            }
        });
        LOG.info("finished shutting down resources!");
    }

    public static class LocalModule implements Module {
        @Override
        public void configure(Binder binder) {
            binder.bind(ShutdownManager.class);
        }

    }

}
