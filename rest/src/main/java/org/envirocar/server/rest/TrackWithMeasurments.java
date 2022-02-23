/*
 * Copyright (C) 2013-2022 The enviroCar project
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
package org.envirocar.server.rest;

import com.google.common.collect.Lists;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.entities.User;
import org.joda.time.DateTime;
import org.locationtech.jts.geom.Geometry;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class TrackWithMeasurments implements Track, Iterable<Measurement> {
    public static final String SENSOR = "sensor";
    public static final String USER = "user";
    public static final String BBOX = "bbox";
    public static final String NAME = "name";
    public static final String DESCIPTION = "description";
    public static final String LENGTH = "length";
    private final Track track;
    private final List<Measurement> measurements = Lists.newLinkedList();

    public TrackWithMeasurments(Track track) {
        this.track = track;
    }

    @Override
    public String getName() {
        return this.track.getName();
    }

    @Override
    public void setName(String name) {
        this.track.setName(name);
    }

    @Override
    public String getDescription() {
        return this.track.getDescription();
    }

    @Override
    public void setDescription(String description) {
        this.track.setDescription(description);
    }

    @Override
    public String getIdentifier() {
        return this.track.getIdentifier();
    }

    @Override
    public void setIdentifier(String id) {
        this.track.setIdentifier(id);
    }

    @Override
    public User getUser() {
        return this.track.getUser();
    }

    @Override
    public void setUser(User user) {
        this.track.setUser(user);
    }

    @Override
    public Sensor getSensor() {
        return this.track.getSensor();
    }

    @Override
    public void setSensor(Sensor track) {
        this.track.setSensor(track);
    }

    @Override
    public Geometry getBoundingBox() {
        return this.track.getBoundingBox();
    }

    @Override
    public void setBoundingBox(Geometry bbox) {
        this.track.setBoundingBox(bbox);
    }

    @Override
    public DateTime getCreationTime() {
        return this.track.getCreationTime();
    }

    @Override
    public DateTime getModificationTime() {
        return this.track.getModificationTime();
    }

    @Override
    public Iterator<Measurement> iterator() {
        return this.measurements.iterator();
    }

    public boolean addMeasurement(Measurement e) {
        return this.measurements.add(e);
    }

    public boolean removeMeasurement(Measurement o) {
        return this.measurements.remove(o);
    }

    public List<Measurement> getMeasurements() {
        return Collections.unmodifiableList(this.measurements);
    }

    public Track getTrack() {
        return this.track;
    }

    @Override
    public boolean hasUser() {
        return this.track.hasUser();
    }

    @Override
    public boolean hasSensor() {
        return this.track.hasSensor();
    }

    @Override
    public boolean hasName() {
        return this.track.hasName();
    }

    @Override
    public boolean hasDescription() {
        return this.track.hasDescription();
    }

    @Override
    public boolean hasIdentifier() {
        return this.track.hasIdentifier();
    }

    @Override
    public boolean hasBoundingBox() {
        return this.track.hasBoundingBox();
    }

    @Override
    public boolean hasCreationTime() {
        return this.track.hasCreationTime();
    }

    @Override
    public boolean hasModificationTime() {
        return this.track.hasModificationTime();
    }

    @Override
    public DateTime getBegin() {
        return this.track.getBegin();
    }

    @Override
    public void setBegin(DateTime begin) {
        this.track.setBegin(begin);
    }

    @Override
    public boolean hasBegin() {
        return this.track.hasBegin();
    }

    @Override
    public DateTime getEnd() {
        return this.track.getEnd();
    }

    @Override
    public void setEnd(DateTime end) {
        this.track.setEnd(end);
    }

    @Override
    public boolean hasEnd() {
        return this.track.hasEnd();
    }

    @Override
    public void setTouVersion(String touVersion) {
        this.track.setTouVersion(touVersion);
    }

    @Override
    public String getTouVersion() {
        return this.track.getTouVersion();
    }

    @Override
    public void setObdDevice(String obdDevice) {
        this.track.setObdDevice(obdDevice);
    }

    @Override
    public String getObdDevice() {
        return this.track.getObdDevice();
    }

    @Override
    public void setAppVersion(String appVersion) {
        this.track.setAppVersion(appVersion);
    }

    @Override
    public String getAppVersion() {
        return this.track.getAppVersion();
    }

    @Override
    public boolean hasAppVersion() {
        return this.track.hasAppVersion();
    }

    @Override
    public boolean hasObdDevice() {
        return this.track.hasObdDevice();
    }

    @Override
    public boolean hasTouVersion() {
        return this.track.hasTouVersion();
    }

    @Override
    public double getLength() {
        return this.track.getLength();
    }

    @Override
    public void setLength(double length) {
        this.track.setLength(length);
    }

    @Override
    public boolean hasLength() {
        return this.track.hasLength();
    }

    @Override
    public TrackStatus getStatus() {
        return this.track.getStatus();
    }

    @Override
    public void setStatus(TrackStatus status) {
        this.track.setStatus(status);
    }
}
