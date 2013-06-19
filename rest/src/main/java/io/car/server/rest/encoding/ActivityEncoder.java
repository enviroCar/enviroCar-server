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

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.core.activities.Activity;
import io.car.server.core.activities.GroupActivity;
import io.car.server.core.activities.TrackActivity;
import io.car.server.core.activities.UserActivity;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ActivityEncoder extends AbstractEntityEncoder<Activity> {
    private final EntityEncoder<User> userEncoder;
    private final EntityEncoder<Track> trackEncoder;
    private final EntityEncoder<Group> groupEncoder;

    @Inject
    public ActivityEncoder(EntityEncoder<User> userEncoder,
                           EntityEncoder<Track> trackEncoder,
                           EntityEncoder<Group> groupEncoder) {
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

    @Override
    public Model encodeRDF(Activity t, AccessRights rights, MediaType mt) {
        /* TODO implement io.car.server.rest.encoding.ActivityEncoder.encodeRDF() */
        throw new UnsupportedOperationException("io.car.server.rest.encoding.ActivityEncoder.encodeRDF() not yet implemented");
    }
}
