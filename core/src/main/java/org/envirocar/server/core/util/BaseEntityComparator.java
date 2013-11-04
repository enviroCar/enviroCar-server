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
package org.envirocar.server.core.util;

import java.util.Comparator;

import org.envirocar.server.core.entities.BaseEntity;

/**
 * @author Matthes Rieke
 *
 */
public class BaseEntityComparator<T extends BaseEntity> implements Comparator<BaseEntity> {

	@Override
	public int compare(BaseEntity o1, BaseEntity o2) {
		if (o1.getCreationTime() == null && o2.getCreationTime() == null) return 0;
		
		if (o1.getCreationTime() == null) return -1;
		
		if (o2.getCreationTime() == null) return 1;
		
		return o1.getCreationTime().compareTo(o2.getCreationTime());
	}

}
