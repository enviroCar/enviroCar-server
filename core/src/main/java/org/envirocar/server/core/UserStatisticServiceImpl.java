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
package org.envirocar.server.core;

import org.envirocar.server.core.filter.UserStatisticFilter;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.List;
import org.envirocar.server.core.dao.UserStatisticDao;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.UserStatisticImpl;
import org.envirocar.server.core.entities.TrackSummary;
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

    /**
     * @Override public UserStatistic getUserStatistic(UserStatisticFilter
     * request) { UserStatistic dummyUserStatistics = new UserStatisticImpl();
     * dummyUserStatistics.setUser(request.getUser());
     * dummyUserStatistics.setDistance(3344.22);
     * dummyUserStatistics.setDuration(50.2);
     * dummyUserStatistics.setDistanceBelow60kmh(1610.2);
     * dummyUserStatistics.setDurationBelow60kmh(32.1);
     * dummyUserStatistics.setDistanceAbove130kmh(502.34);
     * dummyUserStatistics.setDurationAbove130kmh(7.1);
     *
     *
     * TrackSummary ts1 = new TrackSummaryImpl();
     * ts1.setIdentifier("5743fd9ee4b09078f971f0d7"); GeometryFactory geomFac =
     * new GeometryFactory(); Coordinate startPt = new Coordinate(
     * 6.935220557150537, 51.38094428409963); Coordinate endPt = new Coordinate(
     * 7.022849717547225, 51.43455885936926); Geometry startPos =
     * geomFac.createPoint(startPt); Geometry endPos =
     * geomFac.createPoint(endPt); ts1.setStartPosition(startPos);
     * ts1.setEndPosition(endPos);
     *
     * TrackSummary ts2 = new TrackSummaryImpl();
     * ts2.setIdentifier("4743fd9ee4b09078f971f0d8"); geomFac = new
     * GeometryFactory(); startPt = new Coordinate( 13.154406, 53.437805); endPt
     * = new Coordinate( 13.32280, 53.4959299); startPos =
     * geomFac.createPoint(startPt); endPos = geomFac.createPoint(endPt);
     * ts2.setStartPosition(startPos); ts2.setEndPosition(endPos);
     *
     * TrackSummary ts3 = new TrackSummaryImpl();
     * ts3.setIdentifier("3743fd9ee4b09078f971f0d8"); geomFac = new
     * GeometryFactory(); startPt = new Coordinate( 23.154406, 43.437805); endPt
     * = new Coordinate( 23.32280, 43.4959299); startPos =
     * geomFac.createPoint(startPt); endPos = geomFac.createPoint(endPt);
     * ts3.setStartPosition(startPos); ts3.setEndPosition(endPos);
     *
     * TrackSummaries ts = new TrackSummariesImpl(); ts.addTrackSummary(ts1);
     * ts.addTrackSummary(ts2); ts.addTrackSummary(ts3);
     *
     * dummyUserStatistics.setTrackSummaries(ts);
     *
     * return dummyUserStatistics; //return this.dao.getUserStatistics(request);
     * }
    *
     */
}