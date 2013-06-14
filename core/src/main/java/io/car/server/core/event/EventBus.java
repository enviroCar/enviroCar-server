/*
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
package io.car.server.core.event;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.car.server.core.activities.Activity;

@Singleton
public class EventBus {
    private static final Logger log = LoggerFactory.getLogger(EventBus.class);
    private final Set<EventBusListener> listeners;

    @Inject
    public EventBus(Set<EventBusListener> listeners) {
        this.listeners = listeners;
    }

    public void pushActivity(Activity ac) {
        log.debug("New event pushed to bus: {}", ac);
        for (EventBusListener ebl : this.listeners) {
            ebl.onNewActivity(ac);
        }
    }
}
