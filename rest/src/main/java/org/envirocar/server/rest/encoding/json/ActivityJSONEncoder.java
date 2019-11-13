/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
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

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Singleton
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
    public ObjectNode encodeJSON(Activity entity, AccessRights rights, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        if (entity.hasTime()) {
            root.put(JSONConstants.TIME_KEY, getDateTimeFormat().print(entity.getTime()));
        }
        if (entity.hasType()) {
            root.put(JSONConstants.TYPE_KEY, entity.getType().toString());
        }
        if (entity.hasUser()) {
            root.set(JSONConstants.USER_KEY, userEncoder.encodeJSON(entity.getUser(), rights, mediaType));
        }
        if (entity instanceof GroupActivity) {
            GroupActivity activity = (GroupActivity) entity;
            if (activity.hasGroup()) {
                root.set(JSONConstants.GROUP_KEY, groupEncoder.encodeJSON(activity.getGroup(), rights, mediaType));
            }
        } else if (entity instanceof UserActivity) {
            UserActivity activity = (UserActivity) entity;
            if (activity.hasOther()) {
                root.set(JSONConstants.OTHER_KEY, userEncoder.encodeJSON(activity.getOther(), rights, mediaType));
            }
        } else if (entity instanceof TrackActivity) {
            TrackActivity activity = (TrackActivity) entity;
            if (activity.hasTrack()) {
                root.set(JSONConstants.TRACK_KEY, trackEncoder.encodeJSON(activity.getTrack(), rights, mediaType));
            }
        }
        return root;
    }
}
