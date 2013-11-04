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
package org.envirocar.server.mongo.entity;

import static org.envirocar.server.mongo.entity.MongoMeasurement.IDENTIFIER;

import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("tracks")
public class MongoTrack extends MongoEntityBase implements Track {
    public static final String ID = Mapper.ID_KEY;
    public static final String USER = "user";
    public static final String SENSOR = "sensor";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String BBOX = "bbox";
    public static final String BEGIN = "begin";
    public static final String END = "end";
	public static final String APP_VERSION = "appVersion";
	public static final String OBD_DEVICE = "obdDevice";
	public static final String TERMS_OF_USE_VERSION = "touVersion";
    @Id
    private ObjectId id = new ObjectId();
    @Property(USER)
    private Key<MongoUser> user;
    @Transient
    private MongoUser _user;
    @Embedded(SENSOR)
    private MongoSensor sensor;
    @Property(NAME)
    private String name;
    @Property(DESCRIPTION)
    private String description;
    @Property(BBOX)
    private Geometry bbox;
    @Property(BEGIN)
    private DateTime begin;
    @Property(END)
    private DateTime end;
    @Property(APP_VERSION)
    private String appVersion;
    @Property(OBD_DEVICE)
    private String obdDevice;
    @Property(TERMS_OF_USE_VERSION)
    private String touVersion;

    @Override
    public MongoUser getUser() {
        if (this._user == null) {
            this._user = getMongoDB().deref(MongoUser.class, this.user);
        }
        return this._user;
    }

    @Override
    public void setUser(User user) {
        this._user = (MongoUser) user;
        this.user = getMongoDB().key(this._user);
    }

    @Override
    public MongoSensor getSensor() {
        return this.sensor;
    }

    @Override
    public void setSensor(Sensor sensor) {
        this.sensor = (MongoSensor) sensor;
    }

    @Override
    public boolean hasUser() {
        return getUser() != null;
    }

    @Override
    public boolean hasSensor() {
        return getSensor() != null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean hasDescription() {
        return getDescription() != null && !getDescription().isEmpty();
    }

    @Override
    public String getIdentifier() {
        return this.id == null ? null : this.id.toString();
    }

    @Override
    public void setIdentifier(String id) {
        this.id = id == null ? null : new ObjectId(id);
    }

    @Override
    public boolean hasIdentifier() {
        return this.id != null;
    }

    @Override
    public Geometry getBoundingBox() {
        return this.bbox;
    }

    @Override
    public void setBoundingBox(Geometry bbox) {
        this.bbox = bbox;
    }

    @Override
    public boolean hasBoundingBox() {
        return getBoundingBox() != null && !getBoundingBox().isEmpty();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return toStringHelper()
                .add(IDENTIFIER, id)
                .add(NAME, name)
                .add(DESCRIPTION, description)
                .add(USER, user)
                .add(SENSOR, sensor)
                .add(BBOX, bbox)
                .add(BEGIN, begin)
                .add(END, end)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoTrack other = (MongoTrack) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public DateTime getBegin() {
        return begin;
    }

    @Override
    public void setBegin(DateTime begin) {
        this.begin = begin;
    }

    @Override
    public boolean hasBegin() {
        return getBegin() != null;
    }

    @Override
    public DateTime getEnd() {
        return end;
    }

    @Override
    public void setEnd(DateTime end) {
        this.end = end;
    }

    @Override
    public boolean hasEnd() {
        return getEnd() != null;
    }

    @Override
	public String getAppVersion() {
		return appVersion;
	}

    @Override
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

    @Override
	public String getObdDevice() {
		return obdDevice;
	}

    @Override
	public void setObdDevice(String obdDevice) {
		this.obdDevice = obdDevice;
	}

    @Override
	public String getTouVersion() {
		return touVersion;
	}

    @Override
	public void setTouVersion(String touVersion) {
		this.touVersion = touVersion;
	}

	@Override
	public boolean hasAppVersion() {
		return appVersion != null && !appVersion.isEmpty();
	}

	@Override
	public boolean hasObdDevice() {
		return obdDevice != null && !obdDevice.isEmpty();
	}

	@Override
	public boolean hasTouVersion() {
		return touVersion != null && !touVersion.isEmpty();
	}
    
}
