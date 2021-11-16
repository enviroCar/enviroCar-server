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
package org.envirocar.server.mongo.util;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.mongo.entity.MongoTrackSummary;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.util.GeodesicGeometryOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public final class TrackStatisticImpl implements TrackStatistic {
    private static final Logger LOG = LoggerFactory.getLogger(TrackStatisticImpl.class);
    private static final GeodesicGeometryOperations ggo = new GeodesicGeometryOperations();
    private static final int MILLIS_PER_HOUR = 60 * 60 * 1000;
    private double distance = 0;
    private double duration = 0;
    private double distanceBelow60 = 0;
    private double durationBelow60 = 0;
    private double distanceAbove130 = 0;
    private double durationAbove130 = 0;
    private double distanceNaN = 0;
    private double durationNaN = 0;
    private Measurement first;
    private Measurement last;
    private final Track track;

    public TrackStatisticImpl(Track track, Iterable<Measurement> values) {
        this.track = track;
        if (values == null) {
            LOG.error("no measurements for track available...");
        } else {
            Iterator<Measurement> iterator = values.iterator();
            if (iterator.hasNext()) {
                Measurement prev = iterator.next();
                setFirst(prev);
                while (iterator.hasNext()) {
                    Measurement curr = iterator.next();
                    update(prev, curr).setLast(prev = curr);
                }
            }
        }
        finish();
    }

    @Override
    public boolean isValid() {
        return first != null && last != null && first != last;
    }

    @Override
    public TrackStatisticImpl negate() {
        distance *= -1;
        duration *= -1;
        distanceAbove130 *= -1;
        durationAbove130 *= -1;
        distanceBelow60 *= -1;
        durationBelow60 *= -1;
        distanceNaN *= -1;
        durationNaN *= -1;
        return this;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public double getDistanceAbove130() {
        return distanceAbove130;
    }

    @Override
    public double getDuration() {
        return duration;
    }

    @Override
    public double getDistanceBelow60() {
        return distanceBelow60;
    }

    @Override
    public double getDurationAbove130() {
        return durationAbove130;
    }

    @Override
    public double getDurationNaN() {
        return durationNaN;
    }

    @Override
    public double getDistanceNaN() {
        return distanceNaN;
    }

    @Override
    public double getDurationBelow60() {
        return durationBelow60;
    }

    @Override
    public TrackSummary getSummary() {
        TrackSummary ts = new MongoTrackSummary(track.getIdentifier());
        if (isValid()) {
            ts.setStartPosition(getFirst().getGeometry());
            ts.setEndPosition(getLast().getGeometry());
        }
        return ts;
    }

    private Measurement getFirst() {
        return first;
    }

    private Measurement getLast() {
        return last;
    }

    private void setLast(Measurement last) {
        this.last = last;
    }

    private void setFirst(Measurement first) {
        this.first = first;
    }

    @Override
    public void addTo(UserStatistic statistic) {
        statistic.setDistance(statistic.getDistance() + distance);
        statistic.setDistanceBelow60kmh(statistic.getDistanceBelow60kmh() + distanceBelow60);
        statistic.setDistanceAbove130kmh(statistic.getDistanceAbove130kmh() + distanceAbove130);
        statistic.setDistanceNaN(statistic.getDistanceNaN() + distanceNaN);
        statistic.setDuration(statistic.getDuration() + duration);
        statistic.setDurationBelow60kmh(statistic.getDurationBelow60kmh() + durationBelow60);
        statistic.setDurationAbove130kmh(statistic.getDurationAbove130kmh() + durationAbove130);
        statistic.setDurationNaN(statistic.getDurationNaN() + durationNaN);
    }

    private TrackStatisticImpl update(Measurement prev, Measurement curr) {
        double distance = getDistance(prev, curr);
        double duration = getDuration(prev, curr);
        double speed = getSpeed(prev);

        if (Double.isNaN(speed)) {
            distanceNaN += distance;
            durationNaN += duration;
        } else if (speed > 130) {
            distanceAbove130 += distance;
            durationAbove130 += duration;
        } else if (speed < 60) {
            distanceBelow60 += distance;
            durationBelow60 += duration;
        }
        this.distance += distance;
        this.duration += duration;
        return this;
    }

    private double getDistance(Measurement prev, Measurement curr) {
        return ggo.calculateDistance(prev, curr);
    }

    private long getDuration(Measurement prev, Measurement curr) {
        return !prev.hasTime() || !curr.hasTime()
               ? 0 : curr.getTime().getMillis() - prev.getTime().getMillis();
    }

    private double getSpeed(Measurement prev) {
        for (MeasurementValue value : prev.getValues()) {
            if (value.hasPhenomenon() &&
                value.getPhenomenon().hasName() &&
                value.getPhenomenon().getName().equals("Speed") &&
                value.hasValue()) {
                return (double) value.getValue();
            }
        }
        return Double.NaN;
    }

    private void finish() {
        duration /= MILLIS_PER_HOUR;
        durationBelow60 /= MILLIS_PER_HOUR;
        durationAbove130 /= MILLIS_PER_HOUR;
        durationNaN /= MILLIS_PER_HOUR;
    }

}
