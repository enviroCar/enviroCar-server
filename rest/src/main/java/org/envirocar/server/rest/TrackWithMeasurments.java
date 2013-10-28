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
package org.envirocar.server.rest;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;

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
    public void setName(String name) {
        track.setName(name);
    }

    @Override
    public String getDescription() {
        return track.getDescription();
    }

    @Override
    public void setDescription(String description) {
        track.setDescription(description);
    }

    @Override
    public String getIdentifier() {
        return track.getIdentifier();
    }

    @Override
    public void setIdentifier(String id) {
        track.setIdentifier(id);
    }

    @Override
    public User getUser() {
        return track.getUser();
    }

    @Override
    public void setUser(User user) {
        track.setUser(user);
    }

    @Override
    public Sensor getSensor() {
        return track.getSensor();
    }

    @Override
    public void setSensor(Sensor track) {
        this.track.setSensor(track);
    }

    @Override
    public Geometry getBoundingBox() {
        return track.getBoundingBox();
    }

    @Override
    public void setBoundingBox(Geometry bbox) {
        track.setBoundingBox(bbox);
    }

    @Override
    public DateTime getCreationTime() {
        return track.getCreationTime();
    }

    @Override
    public DateTime getModificationTime() {
        return track.getModificationTime();
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

    @Override
    public boolean hasUser() {
        return track.hasUser();
    }

    @Override
    public boolean hasSensor() {
        return track.hasSensor();
    }

    @Override
    public boolean hasName() {
        return track.hasName();
    }

    @Override
    public boolean hasDescription() {
        return track.hasDescription();
    }

    @Override
    public boolean hasIdentifier() {
        return track.hasIdentifier();
    }

    @Override
    public boolean hasBoundingBox() {
        return track.hasBoundingBox();
    }

    @Override
    public boolean hasCreationTime() {
        return track.hasCreationTime();
    }

    @Override
    public boolean hasModificationTime() {
        return track.hasModificationTime();
    }

    @Override
    public DateTime getBegin() {
        return track.getBegin();
    }

    @Override
    public void setBegin(DateTime begin) {
        setBegin(begin);
    }

    @Override
    public boolean hasBegin() {
        return track.hasBegin();
    }

    @Override
    public DateTime getEnd() {
        return track.getEnd();
    }

    @Override
    public void setEnd(DateTime end) {
        track.setEnd(end);
    }

    @Override
    public boolean hasEnd() {
        return track.hasEnd();
    }

	@Override
	public void setTouVersion(String touVersion) {
		track.setTouVersion(touVersion);
	}

	@Override
	public String getTouVersion() {
		return track.getTouVersion();
	}

	@Override
	public void setObdDevice(String obdDevice) {
		track.setObdDevice(obdDevice);
	}

	@Override
	public String getObdDevice() {
		return track.getObdDevice();
	}

	@Override
	public void setAppVersion(String appVersion) {
		track.setAppVersion(appVersion);
	}

	@Override
	public String getAppVersion() {
		return track.getAppVersion();
	}

	@Override
	public boolean hasAppVersion() {
		return track.hasAppVersion();
	}

	@Override
	public boolean hasObdDevice() {
		return track.hasObdDevice();
	}

	@Override
	public boolean hasTouVersion() {
		return track.hasTouVersion();
	}
}
