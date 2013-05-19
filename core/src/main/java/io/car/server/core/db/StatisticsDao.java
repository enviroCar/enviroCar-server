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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.core.db;

import io.car.server.core.Statistic;
import io.car.server.core.Statistics;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

/**
 *
 * @author jan
 */
public interface StatisticsDao {
    long getNumberOfTracks();

    long getNumberOfMeasurements();

    long getNumberOfMeasurements(User user);

    long getNumberOfMeasurements(Track track);

    Statistics getStatistics(Track track);

    Statistics getStatistics(User user);

    Statistics getStatistics();

    Statistic getStatistics(Track track, Phenomenon phenomenon);

    Statistic getStatistics(User user, Phenomenon phenomenon);

    Statistic getStatistics(Phenomenon phenomenon);

    Statistics getStatistics(Track track, Phenomenons phenomenon);

    Statistics getStatistics(User user, Phenomenons phenomenon);

    Statistics getStatistics(Phenomenons phenomenon);
}
