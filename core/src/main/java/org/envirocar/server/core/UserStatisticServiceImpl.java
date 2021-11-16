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
package org.envirocar.server.core;

import org.envirocar.server.core.filter.UserStatisticFilter;

import com.google.inject.Inject;
import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.entities.UserStatistic;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class UserStatisticServiceImpl implements UserStatisticService {

    private final UserStatisticDao dao;

    @Inject
    public UserStatisticServiceImpl(UserStatisticDao dao) {
        this.dao = dao;
    }

    @Override
    public UserStatistic getUserStatistic(UserStatisticFilter request) {
         return this.dao.get(request);
    }
    
}