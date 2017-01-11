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
package org.envirocar.server.mongo.util;

import java.util.List;

import org.envirocar.server.core.entities.Measurement;
import java.util.ArrayList;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.MeasurementValues;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.util.GeodesicGeometryOperations;
import org.envirocar.server.mongo.entity.MongoUserStatistic;

import org.joda.time.DateTime;

public class UserStatisticUtils implements UserStatisticOperations {

    @Override
    public MongoUserStatistic addTrackStatistic(UserStatistic previous, Track track) {
        GeodesicGeometryOperations ggo = new GeodesicGeometryOperations();

        // calculate statistics of Track and add to resulting UserStatistics:
        MongoUserStatistic updatedUserStatistics = new MongoUserStatistic();
        TrackSummary ts;
        
        if (track instanceof Iterable) {
            Iterable<Measurement> measurementIterator = (Iterable<Measurement>) track;
            List<Measurement> list = new ArrayList();
            for (Measurement m : measurementIterator) {
                list.add(m);
            }

            // calculate userstatistic for this track:
            double total_dist = 0;
            double total_dura = 0;
            double total_dist60 = 0;
            double total_dura60 = 0;
            double total_dist130 = 0;
            double total_dura130 = 0;
            double total_distNaN = 0;
            double total_duraNaN = 0;
            for (int i = 0; i < list.size()-1; i++) {
                Measurement m_this = list.get(i);
                Measurement m_next = list.get(i + 1);

                // calculate dist:
                double dist = ggo.calculateDistance(
                        m_this,
                        m_next);

                // calculate dura:
                DateTime t_start = m_this.getTime();
                DateTime t_end = m_next.getTime();
                double dura_millis
                        = t_end.getMillis()
                        - t_start.getMillis();

                // b. fill dist and dura according to intervals <60,>130,NaN:
                Double speed = null;
                MeasurementValues values = m_this.getValues();
                for (MeasurementValue value : values) {
                    if (value.getPhenomenon().getName().equals("Speed")) {
                        speed = (double) ((Integer) value.getValue());
                        break;
                    }
                }
                if (speed == null) {
                    total_distNaN += dist;
                    total_duraNaN += dura_millis;
                } else if (speed > 130) {
                    total_dist130 += dist;
                    total_dura130 += dura_millis;
                } else if (speed < 60) {
                    total_dist60 += dist;
                    total_dura60 += dura_millis;
                }
                total_dist += dist;
                total_dura += dura_millis;
            }
            // format milliseconds to hours:
            total_dura /= (60 * 60 * 1000);
            total_dura60 /= (60 * 60 * 1000);
            total_dura130 /= (60 * 60 * 1000);
            total_duraNaN /= (60 * 60 * 1000);

            // create TrackSummary object:
            // get first and last measurements geometries
            Measurement valuesStart = list.get(0);
            Measurement valuesEnd = list.get(list.size()-1);

            ts = new TrackSummary();
            ts.setStartPosition(valuesStart.getGeometry());
            ts.setEndPosition(valuesEnd.getGeometry());
            ts.setIdentifier(track.getIdentifier());
            TrackSummaries trackSummaries = previous.getTrackSummaries();
            trackSummaries.addTrackSummary(ts);

            updatedUserStatistics.setDistance(previous.getDistance() + total_dist);
            updatedUserStatistics.setDistanceBelow60kmh(previous.getDistanceBelow60kmh() + total_dist60);
            updatedUserStatistics.setDistanceAbove130kmh(previous.getDistanceAbove130kmh() + total_dist130);
            updatedUserStatistics.setDistanceNaN(previous.getDistanceNaN() + total_distNaN);
            updatedUserStatistics.setDuration(previous.getDuration() + total_dura);
            updatedUserStatistics.setDurationBelow60kmh(previous.getDurationBelow60kmh() + total_dura60);
            updatedUserStatistics.setDurationAbove130kmh(previous.getDurationAbove130kmh() + total_dura130);
            updatedUserStatistics.setDurationNaN(previous.getDurationNaN() + total_duraNaN);

            // update TrackSummaries:
            updatedUserStatistics.setTrackSummaries(trackSummaries);
        } else {
            // track has no measurements. put empty zeros:
            TrackSummaries trackSummaries = previous.getTrackSummaries();

            updatedUserStatistics.setDistance(0.5);
            updatedUserStatistics.setDistanceBelow60kmh(0);
            updatedUserStatistics.setDistanceAbove130kmh(0);
            updatedUserStatistics.setDistanceNaN(0);
            updatedUserStatistics.setDuration(0);
            updatedUserStatistics.setDurationBelow60kmh(0);
            updatedUserStatistics.setDurationAbove130kmh(0);
            updatedUserStatistics.setDurationNaN(0);

            updatedUserStatistics.setTrackSummaries(trackSummaries);
        }

        return updatedUserStatistics;
    }

    @Override
    public MongoUserStatistic removeTrackStatistic(UserStatistic previous, Track track) {
        GeodesicGeometryOperations ggo = new GeodesicGeometryOperations();

        // calculate statistics of Track and add to resulting UserStatistics:
        MongoUserStatistic updatedUserStatistics = new MongoUserStatistic();
        TrackSummary trackSummary;

        if (track instanceof Iterable) {
            Iterable<Measurement> measurementIterator = (Iterable<Measurement>) track;
            List<Measurement> list = new ArrayList();
            for (Measurement m : measurementIterator) {
                list.add(m);
            }

            // calculate userstatistic for this track:
            double total_dist = 0;
            double total_dura = 0;
            double total_dist60 = 0;
            double total_dura60 = 0;
            double total_dist130 = 0;
            double total_dura130 = 0;
            double total_distNaN = 0;
            double total_duraNaN = 0;
            for (int i = 0; i < list.size()-1; i++) {
                Measurement m_this = list.get(i);
                Measurement m_next = list.get(i + 1);

                // calculate dist:
                double dist = ggo.calculateDistance(
                        m_this,
                        m_next);

                // calculate dura:
                DateTime t_start = m_this.getTime();
                DateTime t_end = m_next.getTime();
                double dura_millis
                        = t_end.getMillis()
                        - t_start.getMillis();

                // b. fill dist and dura according to intervals <60,>130,NaN:
                Double speed = null;
                MeasurementValues values = m_this.getValues();
                for (MeasurementValue value : values) {
                    if (value.getPhenomenon().getName().equals("Speed")) {
                        speed = (double) ((Integer) value.getValue());
                        break;
                    }
                }
                if (speed == null) {
                    total_distNaN += dist;
                    total_duraNaN += dura_millis;
                } else if (speed > 130) {
                    total_dist130 += dist;
                    total_dura130 += dura_millis;
                } else if (speed < 60) {
                    total_dist60 += dist;
                    total_dura60 += dura_millis;
                }
                total_dist += dist;
                total_dura += dura_millis;
            }
            // format milliseconds to hours:
            total_dura /= (60 * 60 * 1000);
            total_dura60 /= (60 * 60 * 1000);
            total_dura130 /= (60 * 60 * 1000);
            total_duraNaN /= (60 * 60 * 1000);

            // create TrackSummary object:
            // get first and last measurements geometries
            Measurement valuesStart = list.get(0);
            Measurement valuesEnd = list.get(list.size()-1);

            updatedUserStatistics.setDistance(
                    previous.getDistance() - total_dist
            );
            updatedUserStatistics.setDistanceAbove130kmh(
                    previous.getDistanceAbove130kmh() - total_dist130
            );
            updatedUserStatistics.setDistanceBelow60kmh(
                    previous.getDistanceBelow60kmh() - total_dist60
            );
            updatedUserStatistics.setDistanceNaN(
                    previous.getDistanceNaN() - total_distNaN
            );
            updatedUserStatistics.setDuration(
                    previous.getDuration() - total_dura
            );
            updatedUserStatistics.setDurationAbove130kmh(
                    previous.getDurationAbove130kmh() - total_dura130
            );
            updatedUserStatistics.setDurationBelow60kmh(
                    previous.getDurationBelow60kmh() - total_dura60
            );
            updatedUserStatistics.setDurationNaN(
                    previous.getDurationNaN() - total_duraNaN
            );

            trackSummary = new TrackSummary();
            trackSummary.setStartPosition(valuesStart.getGeometry());
            trackSummary.setEndPosition(valuesEnd.getGeometry());
            trackSummary.setIdentifier(track.getIdentifier());
            
            TrackSummaries trackSummaries = previous.getTrackSummaries();
            TrackSummaries resultSummaries = new TrackSummaries();
            for (TrackSummary currTS : trackSummaries.getTrackSummaryList()) {
                // if currTS !== DeletedTrack
                if (!currTS.getIdentifier()
                        .equals(trackSummary.getIdentifier())) // add from result:
                {
                    resultSummaries.addTrackSummary(currTS);
                }
            }
            // update TrackSummaries:
            updatedUserStatistics.setTrackSummaries(resultSummaries);

        } else {
            // track is not instance of iterable
            updatedUserStatistics.setDistance(0);
            updatedUserStatistics.setDistanceBelow60kmh(0);
            updatedUserStatistics.setDistanceAbove130kmh(0);
            updatedUserStatistics.setDistanceNaN(0);
            updatedUserStatistics.setDuration(0);
            updatedUserStatistics.setDurationBelow60kmh(0);
            updatedUserStatistics.setDurationAbove130kmh(0);
            updatedUserStatistics.setDurationNaN(0);

            updatedUserStatistics.setTrackSummaries(previous.getTrackSummaries());
        }

        return updatedUserStatistics;
    }

}
