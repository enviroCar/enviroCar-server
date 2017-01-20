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
package org.envirocar.server.core.dao;

import java.util.List;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.filter.UserStatisticFilter;

/**
 * Dao for {@link UserStatistic}s
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public interface UserStatisticDao {
  
    /**
     * Get the {@code UserStatistic}s matching the specified filter.
     *
     * @param request the request
     *
     * @return the {@code UserStatistics}
     */
    UserStatistic get(UserStatisticFilter request);
    
    public void updateStatisticsOnTrackDeletion(Track e, Measurements m);
    
    public void updateStatisticsOnNewTrack(Track e);
    
}
