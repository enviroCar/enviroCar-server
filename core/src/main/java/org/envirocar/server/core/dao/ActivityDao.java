/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.core.dao;

import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.filter.ActivityFilter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface ActivityDao extends Dao {
    void save(Activity activity);

    Activities get(ActivityFilter request);

    Activity get(ActivityFilter request, String id);
}
