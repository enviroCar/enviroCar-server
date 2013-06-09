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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.core.activities.Activities;
import io.car.server.core.activities.Activity;
import io.car.server.rest.JSONConstants;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ActivitiesEncoder extends AbstractEntityEncoder<Activities> {
    private final EntityEncoder<Activity> activityEncoder;

    @Inject
    public ActivitiesEncoder(EntityEncoder<Activity> activityEncoder) {
        this.activityEncoder = activityEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Activities t, MediaType mt) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode activities = root.putArray(JSONConstants.ACTIVITIES_KEY);
        for (Activity a : t) {
            activities.add(activityEncoder.encodeJSON(a, mt));
        }
        return root;
    }

    @Override
    public Model encodeRDF(Activities t, MediaType mt) {
        /* TODO implement io.car.server.rest.encoding.ActivitiesEncoder.encodeRDF() */
        throw new UnsupportedOperationException("io.car.server.rest.encoding.ActivitiesEncoder.encodeRDF() not yet implemented");
    }
}
