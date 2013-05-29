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
package io.car.server.rest.encoding;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.core.activities.Activity;
import io.car.server.core.activities.GroupActivity;
import io.car.server.core.activities.MeasurementActivity;
import io.car.server.core.activities.TrackActivity;
import io.car.server.core.activities.UserActivity;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.rest.JSONConstants;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ActivityEncoder extends AbstractEntityEncoder<Activity> {

    private final EntityEncoder<User> userEncoder;
    private final EntityEncoder<Track> trackEncoder;
    private final EntityEncoder<Group> groupEncoder;
    private final EntityEncoder<Measurement> measurementEncoder;

    @Inject
    public ActivityEncoder(EntityEncoder<User> userEncoder,
                           EntityEncoder<Track> trackEncoder,
                           EntityEncoder<Group> groupEncoder,
                           EntityEncoder<Measurement> measurementEncoder) {
        this.userEncoder = userEncoder;
        this.trackEncoder = trackEncoder;
        this.groupEncoder = groupEncoder;
        this.measurementEncoder = measurementEncoder;
    }

    @Override
    public ObjectNode encode(Activity t, MediaType mt) {
        ObjectNode root = getJsonFactory().objectNode();
        root.put(JSONConstants.TIME_KEY, getDateTimeFormat().print(t.getTime()));
        root.put(JSONConstants.TYPE_KEY, t.getType().toString());
        root.put(JSONConstants.USER_KEY, userEncoder.encode(t.getUser(), mt));
        if (t instanceof GroupActivity) {
            GroupActivity groupActivity = (GroupActivity) t;
            root.put(JSONConstants.GROUP_KEY, groupEncoder
                    .encode(groupActivity.getGroup(), mt));
        } else if (t instanceof UserActivity) {
            UserActivity userActivity = (UserActivity) t;
            root.put(JSONConstants.OTHER_KEY, userEncoder
                    .encode(userActivity.getOther(), mt));

        } else if (t instanceof TrackActivity) {
            TrackActivity trackActivity = (TrackActivity) t;
            root.put(JSONConstants.TRACK_KEY, trackEncoder
                    .encode(trackActivity.getTrack(), mt));

        } else if (t instanceof MeasurementActivity) {
            MeasurementActivity measurementActivity = (MeasurementActivity) t;
            root.put(JSONConstants.MEASUREMENT_KEY, measurementEncoder
                    .encode(measurementActivity.getMeasurement(), mt));
        }
        return root;
    }
}
