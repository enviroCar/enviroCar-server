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
package org.envirocar.server.rest.resources;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.validation.Schema;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class UserResource extends AbstractResource {
    public static final String GROUPS = "groups";
    public static final String FRIENDS = "friends";
    public static final String TRACKS = "tracks";
    public static final String MEASUREMENTS = "measurements";
    public static final String STATISTICS = "statistics";
    public static final String ACTIVITIES = "activities";
    public static final String FRIEND_ACTIVITIES = "friendActivities";
    public static final String AVATAR = "avatar";
    private final User user;

    @Inject
    public UserResource(@Assisted User user) {
        this.user = Preconditions.checkNotNull(user);
    }

    protected User getUser() {
        return this.user;
    }

    @PUT
    @Schema(request = Schemas.USER_MODIFY)
    @Consumes({ MediaTypes.USER_MODIFY })
    @Authenticated
    public Response modify(User changes) throws
            UserNotFoundException, IllegalModificationException,
            ValidationException, ResourceAlreadyExistException {
        checkRights(getRights().canModify(user));
//        checkMail(user);
        User modified = getUserService().modifyUser(user, changes);
        if (modified.getName().equals(user.getName())) {
            return Response.noContent().build();
        } else {
            UriBuilder b = getUriInfo().getBaseUriBuilder();
            List<PathSegment> pathSegments = getUriInfo().getPathSegments();
            Iterator<PathSegment> ps = pathSegments.iterator();
            for (int i = 0; i < pathSegments.size() - 1; ++i) {
                b.path(ps.next().getPath());
            }
            return Response.seeOther(b.path(modified.getName()).build()).build();
        }
    }

    @GET
    @Schema(response = Schemas.USER)
    @Produces({ MediaTypes.USER,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public User get() throws UserNotFoundException {
        return user;
    }

    @DELETE
    @Authenticated
    public void delete() throws ResourceNotFoundException {
        checkRights(getRights().canDelete(user));
        getUserService().deleteUser(this.user);
    }

    @Path(FRIENDS)
    public FriendsResource friends() {
        return getResourceFactory().createFriendsResource(this.user);
    }

    @Path(GROUPS)
    public GroupsResource groups() {
        checkRights(getRights().canSeeGroupsOf(user));
        return getResourceFactory().createGroupsResource(this.user);
    }

    @Path(TRACKS)
    public TracksResource tracks() {
        checkRights(getRights().canSeeTracksOf(user));
        return getResourceFactory().createTracksResource(this.user);
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        checkRights(getRights().canSeeMeasurementsOf(user));
        return getResourceFactory().createMeasurementsResource(this.user, null);
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        checkRights(getRights().canSeeStatisticsOf(user));
        return getResourceFactory().createStatisticsResource(this.user);
    }

    @Path(ACTIVITIES)
    public ActivitiesResource activities() {
        checkRights(getRights().canSeeActivitiesOf(user));
        return getResourceFactory().createActivitiesResource(this.user);
    }

    @Path(FRIEND_ACTIVITIES)
    public FriendsActivitiesResource friendActivities() {
        checkRights(getRights().canSeeFriendActivitiesOf(user));
        return getResourceFactory().createFriendActivitiesResource(this.user);
    }

    @Path(AVATAR)
    public AvatarResource avatar() {
        checkRights(getRights().canSeeAvatarOf(user));
        return getResourceFactory().createAvatarResource(this.user);
    }
}
