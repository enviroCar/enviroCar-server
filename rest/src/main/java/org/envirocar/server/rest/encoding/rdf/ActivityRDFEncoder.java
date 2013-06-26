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
package org.envirocar.server.rest.encoding.rdf;

import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.rest.resources.ActivitiesResource;
import org.envirocar.server.rest.resources.FriendsActivitiesResource;
import org.envirocar.server.rest.resources.GroupResource;
import org.envirocar.server.rest.resources.GroupsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.UserResource;
import org.envirocar.server.rest.resources.UsersResource;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@javax.ws.rs.ext.Provider
public class ActivityRDFEncoder extends AbstractLinkerRDFEntityEncoder<Activity> {
    private final Provider<UriInfo> uriInfo;

    @Inject
    public ActivityRDFEncoder(Set<RDFLinker<Activity>> linkers,
                              Provider<UriInfo> uriInfo) {
        super(Activity.class, linkers);
        this.uriInfo = uriInfo;
    }

    @Override
    protected String getURI(Activity t, Provider<UriBuilder> uri) {
        return getURI(t, uriInfo.get().getMatchedResources().get(0), uri);
    }

    protected static String getURI(Activity t, Object resource,
                                   Provider<UriBuilder> uri) {
        if (resource instanceof FriendsActivitiesResource) {
            FriendsActivitiesResource ar = (FriendsActivitiesResource) resource;
            return uri.get()
                    .path(RootResource.class)
                    .path(RootResource.USERS)
                    .path(UsersResource.USER)
                    .path(UserResource.FRIEND_ACTIVITIES)
                    .path(FriendsActivitiesResource.ACTIVITY)
                    .build(ar.getUser().getName(), t.getIdentifier())
                    .toASCIIString();

        } else if (resource instanceof ActivitiesResource) {
            ActivitiesResource ar = (ActivitiesResource) resource;
            if (ar.getGroup() != null) {
                return uri.get()
                        .path(RootResource.class)
                        .path(RootResource.GROUPS)
                        .path(GroupsResource.GROUP)
                        .path(GroupResource.ACTIVITIES)
                        .path(ActivitiesResource.ACTIVITY)
                        .build(ar.getGroup().getName(), t.getIdentifier())
                        .toASCIIString();
            } else if (ar.getUser() != null) {
                return uri.get()
                        .path(RootResource.class)
                        .path(RootResource.USERS)
                        .path(UsersResource.USER)
                        .path(UserResource.ACTIVITIES)
                        .path(ActivitiesResource.ACTIVITY)
                        .build(ar.getUser().getName(), t.getIdentifier())
                        .toASCIIString();
            } else {
                throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }
}
