/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.rest.resources;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.activities.ActivityType;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.filter.ActivityFilter;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.schema.Schema;

import javax.annotation.Nullable;
import javax.ws.rs.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ActivitiesResource extends AbstractResource {
    public static final String ACTIVITY = "{id}";
    private User user;
    private Group group;

    @AssistedInject
    public ActivitiesResource(@Assisted @Nullable User user) {
        this.user = user;
    }

    @AssistedInject
    public ActivitiesResource(@Assisted @Nullable Group group) {
        this.group = group;
    }

    @AssistedInject
    public ActivitiesResource() {
    }


    @GET
    @Schema(response = Schemas.ACTIVITIES)
    @Produces({MediaTypes.JSON, MediaTypes.XML_RDF, MediaTypes.TURTLE, MediaTypes.TURTLE_ALT})
    public Activities activities(@QueryParam(RESTConstants.TYPE) ActivityType type) throws BadRequestException {
        return getUserService().getActivities(new ActivityFilter(group, user, type, getPagination()));
    }

    @GET
    @Path(ACTIVITY)
    @Schema(response = Schemas.ACTIVITY)
    @Produces({MediaTypes.JSON, MediaTypes.XML_RDF, MediaTypes.TURTLE, MediaTypes.TURTLE_ALT})
    public Activity activity(@PathParam("id") String id) {
        return getUserService().getActivity(new ActivityFilter(group, user, null, null), id);
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }
}
