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

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.activities.ActivityType;
import io.car.server.core.activities.TrackActivity;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.TrackBase;
import io.car.server.core.entities.User;
import io.car.server.mongo.entity.MongoTrack;
import io.car.server.mongo.entity.MongoTrackBase;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoTrackActivity extends MongoActivity implements TrackActivity {
    @Embedded(TRACK)
    private MongoTrackBase track;

    @Inject
    public MongoTrackActivity(@Assisted ActivityType type,
                              @Assisted User user,
                              @Assisted Track track) {
        super(user, type);
        this.track = (MongoTrack) track;
    }

    public MongoTrackActivity() {
        this(null, null, null);
    }

    @Override
    public MongoTrackBase getTrack() {
        return this.track;
    }

    @Override
    public void setTrack(TrackBase track) {
        this.track = (MongoTrackBase) track;
    }
}
