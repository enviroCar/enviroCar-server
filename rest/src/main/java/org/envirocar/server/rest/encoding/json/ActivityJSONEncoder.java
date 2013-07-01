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
package org.envirocar.server.rest.encoding.json;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.activities.GroupActivity;
import org.envirocar.server.core.activities.TrackActivity;
import org.envirocar.server.core.activities.UserActivity;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class ActivityJSONEncoder extends AbstractJSONEntityEncoder<Activity> {
    private final JSONEntityEncoder<User> userEncoder;
    private final JSONEntityEncoder<Track> trackEncoder;
    private final JSONEntityEncoder<Group> groupEncoder;

    @Inject
    public ActivityJSONEncoder(JSONEntityEncoder<User> userEncoder,
                               JSONEntityEncoder<Track> trackEncoder,
                               JSONEntityEncoder<Group> groupEncoder) {
        super(Activity.class);
        this.userEncoder = userEncoder;
        this.trackEncoder = trackEncoder;
        this.groupEncoder = groupEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Activity t, AccessRights rights, MediaType mt) {
        ObjectNode root = getJsonFactory().objectNode();
        if (t.hasTime()) {
            root.put(JSONConstants.TIME_KEY, getDateTimeFormat().print(t
                    .getTime()));
        }
        if (t.hasType()) {
            root.put(JSONConstants.TYPE_KEY, t.getType().toString());
        }
        if (t.hasUser()) {
            root.put(JSONConstants.USER_KEY,
                     userEncoder.encodeJSON(t.getUser(), rights, mt));
        }
        if (t instanceof GroupActivity) {
            GroupActivity groupActivity = (GroupActivity) t;
            if (groupActivity.hasGroup()) {
                root.put(JSONConstants.GROUP_KEY, groupEncoder
                        .encodeJSON(groupActivity.getGroup(), rights, mt));
            }
        } else if (t instanceof UserActivity) {
            UserActivity userActivity = (UserActivity) t;
            if (userActivity.hasOther()) {
                root.put(JSONConstants.OTHER_KEY, userEncoder
                        .encodeJSON(userActivity.getOther(), rights, mt));
            }
        } else if (t instanceof TrackActivity) {
            TrackActivity trackActivity = (TrackActivity) t;
            if (trackActivity.hasTrack()) {
                root.put(JSONConstants.TRACK_KEY, trackEncoder
                        .encodeJSON(trackActivity.getTrack(), rights, mt));
            }
        }
        return root;
    }
}
