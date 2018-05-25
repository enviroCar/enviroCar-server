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
package org.envirocar.server.mongo.statistics;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.envirocar.server.core.dao.StatisticsDao;
import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.event.CreatedTrackEvent;
import org.envirocar.server.core.event.DeletedTrackEvent;

@Singleton
public class NewTrackListener {
    private final StatisticsDao dao;
    private final UserStatisticDao userStatisticDao;

    @Inject
    public NewTrackListener(StatisticsDao dao, UserStatisticDao userStatisticDao) {
        this.dao = dao;
        this.userStatisticDao = userStatisticDao;
    }

    @Subscribe
    public void onCreatedTrackEvent(CreatedTrackEvent e) {
        this.dao.updateStatisticsOnNewTrack(e.getTrack());
        this.userStatisticDao.updateStatisticsOnNewTrack(e.getTrack());
    }

    @Subscribe
    public void onDeletedTrackEvent(DeletedTrackEvent e) {
        this.userStatisticDao.updateStatisticsOnTrackDeletion(e.getTrack(), e.getMeasurements());
    }
}
