/*
 * Copyright (C) 2013  Matthes Rieke, Daniel Nüst
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

public class EventBus {

	private static EventBus instance;
	private List<EventBusListener> listeners = new ArrayList<EventBusListener>();

	private EventBus() {
		
	}
	
	public static synchronized EventBus getInstance() {
		if (instance == null)
			instance = new EventBus();
		
		return null;
	}

	public void pushActivity(Activity ac) {
		for (EventBusListener ebl : this.listeners) {
			ebl.onNewActivity(ac);
		}
	}
	
	
	
}
