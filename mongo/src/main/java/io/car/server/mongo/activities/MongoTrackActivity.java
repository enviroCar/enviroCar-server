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
package io.car.server.mongo.activities;

import com.github.jmkgreen.morphia.annotations.Property;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.activities.ActivityType;
import io.car.server.core.activities.TrackActivity;
import io.car.server.core.entities.Track;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoUser;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoTrackActivity extends MongoActivity implements TrackActivity {
    @Property(TRACK)
    private MongoTrack track;

    @Inject
    public MongoTrackActivity(@Assisted ActivityType type,
                              @Assisted MongoUser user,
                              @Assisted MongoTrack track) {
        super(user, type);
        this.track = track;
    }

    public MongoTrackActivity() {
        this(null, null, null);
    }

    @Override
    public MongoTrack getTrack() {
        return this.track;
    }

    @Override
    public MongoTrackActivity setTrack(Track track) {
        this.track = (MongoTrack) track;
        return this;
    }
}
