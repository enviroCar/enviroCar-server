/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import org.envirocar.server.core.dao.ActivityDao;
import org.envirocar.server.core.dao.AnnouncementsDao;
import org.envirocar.server.core.dao.BadgesDao;
import org.envirocar.server.core.dao.FuelingDao;
import org.envirocar.server.core.dao.GroupDao;
import org.envirocar.server.core.dao.MeasurementDao;
import org.envirocar.server.core.dao.PhenomenonDao;
import org.envirocar.server.core.dao.PrivacyStatementDao;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.dao.StatisticsDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.dao.TrackDao;
import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.dao.UserStatisticDao;

import javax.inject.Inject;

public class CountServiceImpl implements CountService, Counts {
    private final ActivityDao activityDao;
    private final AnnouncementsDao announcementsDao;
    private final BadgesDao badgesDao;
    private final FuelingDao fuelingDao;
    private final GroupDao groupDao;
    private final MeasurementDao measurementDao;
    private final PhenomenonDao phenomenonDao;
    private final PrivacyStatementDao privacyStatementDao;
    private final SensorDao sensorDao;
    private final StatisticsDao statisticsDao;
    private final TermsOfUseDao termsOfUseDao;
    private final TrackDao trackDao;
    private final UserDao userDao;
    private final UserStatisticDao userStatisticDao;

    @Inject
    public CountServiceImpl(ActivityDao activityDao, AnnouncementsDao announcementsDao,
                            BadgesDao badgesDao, FuelingDao fuelingDao, GroupDao groupDao,
                            MeasurementDao measurementDao, PhenomenonDao phenomenonDao,
                            PrivacyStatementDao privacyStatementDao, SensorDao sensorDao,
                            StatisticsDao statisticsDao, TermsOfUseDao termsOfUseDao,
                            TrackDao trackDao, UserDao userDao,
                            UserStatisticDao userStatisticDao) {
        this.activityDao = activityDao;
        this.announcementsDao = announcementsDao;
        this.badgesDao = badgesDao;
        this.fuelingDao = fuelingDao;
        this.groupDao = groupDao;
        this.measurementDao = measurementDao;
        this.phenomenonDao = phenomenonDao;
        this.privacyStatementDao = privacyStatementDao;
        this.sensorDao = sensorDao;
        this.statisticsDao = statisticsDao;
        this.termsOfUseDao = termsOfUseDao;
        this.trackDao = trackDao;
        this.userDao = userDao;
        this.userStatisticDao = userStatisticDao;
    }

    @Override
    public Counts getCounts() {
        return this;
    }

    @Override
    public long getActivities() {
        return activityDao.getCount();
    }

    @Override
    public long getAnnouncements() {
        return announcementsDao.getCount();
    }

    @Override
    public long getBadges() {
        return badgesDao.getCount();
    }

    @Override
    public long getFuelings() {
        return fuelingDao.getCount();
    }

    @Override
    public long getGroups() {
        return groupDao.getCount();
    }

    @Override
    public long getMeasurements() {
        return measurementDao.getCount();
    }

    @Override
    public long getPhenomenons() {
        return phenomenonDao.getCount();
    }

    @Override
    public long getPrivacyStatements() {
        return privacyStatementDao.getCount();
    }

    @Override
    public long getSensors() {
        return sensorDao.getCount();
    }

    @Override
    public long getStatistics() {
        return statisticsDao.getCount();
    }

    @Override
    public long getTermsOfUses() {
        return termsOfUseDao.getCount();
    }

    @Override
    public long getTracks() {
        return trackDao.getCount();
    }

    @Override
    public long getUsers() {
        return userDao.getCount();
    }

    @Override
    public long getUserStatistics() {
        return userStatisticDao.getCount();
    }
}
