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
package io.car.server.core;

import com.google.inject.Inject;

import io.car.server.core.dao.StatisticsDao;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.filter.StatisticsFilter;
import io.car.server.core.statistics.Statistic;
import io.car.server.core.statistics.Statistics;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsDao dao;

    @Inject
    public StatisticsServiceImpl(StatisticsDao dao) {
        this.dao = dao;
    }

    @Override
    public Statistics getStatistics(StatisticsFilter request) {
        return this.dao.getStatistics(request);
    }

    @Override
    public Statistic getStatistic(StatisticsFilter request,
                                  Phenomenon phenomenon) {
        return this.dao.getStatistic(request, phenomenon);
    }
}
