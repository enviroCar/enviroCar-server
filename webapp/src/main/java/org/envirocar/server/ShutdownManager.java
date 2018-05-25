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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.server;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import java.util.Set;
import org.envirocar.server.core.guice.ResourceShutdownListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager class to shutdown resources via ResourceShutdownListener instances.
 */
public class ShutdownManager {
    private final Set<ResourceShutdownListener> listeners;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownManager.class);

    @Inject
    public ShutdownManager(Set<ResourceShutdownListener> listeners) {
        this.listeners = listeners;
    }

    void shutdownListeners() {
        LOGGER.info(String.format("shutting down resources of %s listeners", listeners.size()));
        listeners.stream().forEach((listener) -> {
            try {
                listener.shutdownResources();
            }
            catch (RuntimeException e) {
                LOGGER.warn("Could not shutdown "+ listener.getClass(), e);
            }
        });
        LOGGER.info("finished shutting down resources!");
    }
    
    
    public static class LocalModule implements Module {

        @Override
        public void configure(Binder binder) {
            binder.bind(ShutdownManager.class);
        }
        
    }
    
}
