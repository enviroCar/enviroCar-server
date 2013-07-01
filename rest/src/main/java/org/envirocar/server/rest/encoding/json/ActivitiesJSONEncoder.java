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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;

import org.envirocar.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class ActivitiesJSONEncoder extends AbstractJSONEntityEncoder<Activities> {
    private final JSONEntityEncoder<Activity> activityEncoder;

    @Inject
    public ActivitiesJSONEncoder(JSONEntityEncoder<Activity> activityEncoder) {
        super(Activities.class);
        this.activityEncoder = activityEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Activities t, AccessRights rights, MediaType mt) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode activities = root.putArray(JSONConstants.ACTIVITIES_KEY);
        for (Activity a : t) {
            activities.add(activityEncoder.encodeJSON(a, rights, mt));
        }
        return root;
    }
}
