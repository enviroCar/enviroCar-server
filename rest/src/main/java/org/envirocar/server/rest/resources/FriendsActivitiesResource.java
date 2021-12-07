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
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.filter.ActivityFilter;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class FriendsActivitiesResource extends AbstractResource {
    public static final String ACTIVITY = "{id}";
    private final User user;

    @AssistedInject
    public FriendsActivitiesResource(@Assisted User user) {
        this.user = user;
    }

    @GET
    @Schema(response = Schemas.ACTIVITIES)
    public Activities activities(@QueryParam(RESTConstants.TYPE) ActivityType type) throws BadRequestException {
        return getUserService().getActivities(new ActivityFilter(this.user, getPagination()));
    }

    @GET
    @Path(ACTIVITY)
    @Schema(response = Schemas.ACTIVITY)
    public Activity activity(@PathParam("id") String id) {
        return getUserService().getActivity(new ActivityFilter(this.user, null), id);
    }

    public User getUser() {
        return this.user;
    }
}
