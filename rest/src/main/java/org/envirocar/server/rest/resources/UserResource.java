/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.*;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.rights.AllowOutdatedTerms;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.*;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Iterator;
import java.util.List;

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
    public static final String USERSTATISTIC = "userStatistic";
    public static final String ACTIVITIES = "activities";
    public static final String FRIEND_ACTIVITIES = "friendActivities";
    public static final String AVATAR = "avatar";
    public static final String FUELINGS = "fuelings";
    public static final String SENSORS = "sensors";
    public static final String DELETE_CONTENT = "deleteContent";
    private final User user;

    @Inject
    public UserResource(@Assisted User user) {
        this.user = Preconditions.checkNotNull(user);
    }

    protected User getUser() {
        return this.user;
    }

    @PUT
    @Authenticated
    @Schema(request = Schemas.USER_MODIFY)
    @Consumes(MediaTypes.JSON)
    @AllowOutdatedTerms
    public Response modify(User changes)
            throws UserNotFoundException, IllegalModificationException,
            ValidationException, ResourceAlreadyExistException {
        checkRights(getRights().canModify(this.user));
//        checkMail(user);
        User modified = getUserService().modifyUser(this.user, changes);
        if (modified.getName().equals(this.user.getName())) {
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
    public User get() {
        return this.user;
    }

    @DELETE
    @Authenticated
    @AllowOutdatedTerms
    public void delete(@QueryParam(DELETE_CONTENT) @DefaultValue("false") boolean deleteContent)
            throws ResourceNotFoundException {
        checkRights(getRights().canDelete(this.user));
        getUserService().deleteUser(this.user, deleteContent);
    }

    @Path(FRIENDS)
    public FriendsResource friends() {
        return getResourceFactory().createFriendsResource(this.user);
    }

    @Path(GROUPS)
    public GroupsResource groups() {
        checkRights(getRights().canSeeGroupsOf(this.user));
        return getResourceFactory().createGroupsResource(this.user);
    }

    @Path(TRACKS)
    public TracksResource tracks() {
        checkRights(getRights().canSeeTracksOf(this.user));
        return getResourceFactory().createTracksResource(this.user);
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        checkRights(getRights().canSeeMeasurementsOf(this.user));
        return getResourceFactory().createMeasurementsResource(this.user, null);
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        checkRights(getRights().canSeeStatisticsOf(this.user));
        return getResourceFactory().createStatisticsResource(this.user);
    }

    @Path(USERSTATISTIC)
    public UserStatisticResource userStatistic() {
        checkRights(getRights().canSeeUserStatisticsOf(this.user));
        return getResourceFactory().createUserStatisticsResource(this.user);
    }

    @Path(ACTIVITIES)
    public ActivitiesResource activities() {
        checkRights(getRights().canSeeActivitiesOf(this.user));
        return getResourceFactory().createActivitiesResource(this.user);
    }

    @Path(FRIEND_ACTIVITIES)
    public FriendsActivitiesResource friendActivities() {
        checkRights(getRights().canSeeFriendActivitiesOf(this.user));
        return getResourceFactory().createFriendActivitiesResource(this.user);
    }

    @Path(AVATAR)
    public AvatarResource avatar() {
        checkRights(getRights().canSeeAvatarOf(this.user));
        return getResourceFactory().createAvatarResource(this.user);
    }

    @Path(FUELINGS)
    public FuelingsResource fuelings() {
        checkRights(getRights().canSeeFuelingsOf(this.user));
        return getResourceFactory().createFuelingsResource(this.user);
    }

    @Path(SENSORS)
    public SensorsResource sensors() {
        checkRights(getRights().canSee(this.user));
        return getResourceFactory().createSensorsResource(this.user);
    }
}
