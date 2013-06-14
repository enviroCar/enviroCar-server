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

import io.car.server.core.activities.Activity;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
public class EventBus {

	private static final Logger log = LoggerFactory.getLogger(EventBus.class);

	private List<EventBusListener> listeners = new ArrayList<EventBusListener>();

	public EventBus() {
		//
	}

	public void pushActivity(Activity ac) {
		log.debug("New event pushed to bus: {}", ac);

		for (EventBusListener ebl : this.listeners) {
			ebl.onNewActivity(ac);
		}
	}

}
