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
package org.envirocar.server.mongo.activities;

import org.envirocar.server.core.activities.ActivityType;
import org.envirocar.server.core.activities.TrackActivity;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoTrack;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoTrackActivity extends MongoActivity implements TrackActivity {
    public static final String TRACK = "track";
    @Property(TRACK)
    private Key<MongoTrack> track;
    @Transient
    private MongoTrack _track;

    @AssistedInject
    public MongoTrackActivity(MongoDB mongoDB,
                              @Assisted ActivityType type,
                              @Assisted User user,
                              @Assisted Track track) {
        super(mongoDB, user, type);
        this._track = (MongoTrack) track;
        this.track = mongoDB.key(this._track);
    }

    @Inject
    public MongoTrackActivity(MongoDB mongoDB) {
        this(mongoDB, null, null, null);
    }

    @Override
    public MongoTrack getTrack() {
        if (this._track == null) {
            this._track = getMongoDB().deref(MongoTrack.class, this.track);
        }
        return this._track;
    }

    @Override
    public void setTrack(Track track) {
        this._track = (MongoTrack) track;
        this.track = getMongoDB().key(this._track);
    }

    @Override
    public boolean hasTrack() {
        return getTrack() != null;
    }

    @Override
    public String toString() {
        return toStringHelper().add(TRACK, track).toString();
    }
}
