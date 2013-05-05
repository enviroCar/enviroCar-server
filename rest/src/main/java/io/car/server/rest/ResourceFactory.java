/**
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
package io.car.server.rest;

import io.car.server.core.Group;
import io.car.server.core.Track;
import io.car.server.core.User;
import io.car.server.rest.resources.FriendsResource;
import io.car.server.rest.resources.GroupMemberResource;
import io.car.server.rest.resources.GroupMembersResource;
import io.car.server.rest.resources.GroupResource;
import io.car.server.rest.resources.GroupsResource;
import io.car.server.rest.resources.TrackResource;
import io.car.server.rest.resources.TracksResource;
import io.car.server.rest.resources.UserResource;
import io.car.server.rest.resources.UsersResource;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Arne de Wall <a.dewall@52north.org>
 */
public interface ResourceFactory {
    UserResource createUserResource(User user);
    UsersResource createUsersResource();

    FriendsResource createFriendsResource(User user);

    GroupResource createGroupResource(Group group);
    GroupsResource createGroupsResource();
    GroupsResource createGroupsResource(User user);
    
    GroupMemberResource createGroupMemberResource(Group group, User member);
    GroupMembersResource createGroupMembersResource(Group group);
    
    TrackResource createTrackResource(Track track);
    TracksResource createTracksResource();
    TracksResource createTracksResource(User user);
}
