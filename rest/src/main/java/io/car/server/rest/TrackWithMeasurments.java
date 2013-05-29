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
package io.car.server.rest;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

/**
 * TODO JavaDoc
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class TrackWithMeasurments implements Track, Iterable<Measurement> {

    private Track track;
    private List<Measurement> measurements = Lists.newLinkedList();

    public TrackWithMeasurments(Track track) {
        this.track = track;
    }

    @Override
    public String getName() {
        return track.getName();
    }

    @Override
    public Track setName(String name) {
        track.setName(name);
        return this;
    }

    @Override
    public String getDescription() {
        return track.getDescription();
    }

    @Override
    public Track setDescription(String description) {
        track.setDescription(description);
        return this;
    }

    @Override
    public String getIdentifier() {
        return track.getIdentifier();
    }

    @Override
    public Track setIdentifier(String id) {
        track.setIdentifier(id);
        return this;
    }

    @Override
    public User getUser() {
        return track.getUser();
    }

    @Override
    public Track setUser(User user) {
        track.setUser(user);
        return this;
    }

    @Override
    public Sensor getSensor() {
        return track.getSensor();
    }

    @Override
    public Track setSensor(Sensor track) {
        this.track.setSensor(track);
        return this;
    }

    @Override
    public Geometry getBbox() {
        return track.getBbox();
    }

    @Override
    public Track setBbox(Geometry bbox) {
        track.setBbox(bbox);
        return this;
    }

    @Override
    public Track setBbox(double minx, double miny, double maxx, double maxy) {
        track.setBbox(minx, miny, maxx, maxy);
        return this;
    }

    @Override
    public DateTime getCreationDate() {
        return track.getCreationDate();
    }

    @Override
    public DateTime getLastModificationDate() {
        return track.getLastModificationDate();
    }

    @Override
    public Iterator<Measurement> iterator() {
        return measurements.iterator();
    }

    public boolean addMeasurement(Measurement e) {
        return measurements.add(e);
    }

    public boolean removeMeasurement(Measurement o) {
        return measurements.remove(o);
    }

    public List<Measurement> getMeasurements() {
        return Collections.unmodifiableList(measurements);
    }

    public Track getTrack() {
        return track;
    }
}
