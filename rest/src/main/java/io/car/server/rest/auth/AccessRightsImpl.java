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
package io.car.server.rest.auth;

import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;

public class AccessRightsImpl extends AbstractAccessRights {
   
    @Override
    public boolean canSeeTracksOf(User user) {
        return isSelf(user) ||
               isFriend(user) ||
               shareGroup(user);
    }

    @Override
    public boolean canSeeMeasurementsOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeFriendsOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeGroupsOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeProfileOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeMailAddressOf(User user) {
        return isSelf(user);
    }

    @Override
    public boolean canModify(User user) {
        return isSelf(user);
    }

    @Override
    public boolean canModify(Track track) {
        return isSelf(track.getUser());
    }

    @Override
    public boolean canModify(Measurement measurement) {
        return isSelf(measurement.getUser());
    }

    @Override
    public boolean canModify(Group group) {
        return isSelf(group.getOwner());
    }

    @Override
    public boolean canModify(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canModify(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canDelete(User user) {
        return isSelf(user);
    }

    @Override
    public boolean canDelete(Track track) {
        return isSelf(track.getUser());
    }

    @Override
    public boolean canDelete(Measurement measurement) {
        return isSelf(measurement.getUser());
    }

    @Override
    public boolean canDelete(Group group) {
        return isSelf(group.getOwner());
    }

    @Override
    public boolean canDelete(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canDelete(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canSeeUserOf(Track track) {
        return isSelf(track.getUser()) ||
               isFriendOf(track.getUser()) ||
               shareGroup(track.getUser());
    }

    @Override
    public boolean canSeeUserOf(Measurement measurement) {
        return isSelf(measurement.getUser()) ||
               isFriendOf(measurement.getUser()) ||
               shareGroup(measurement.getUser());
    }

    @Override
    public boolean canSeeActivitiesOf(User user) {
        return isSelf(user) ||
               isFriendOf(user) ||
               shareGroup(user);
    }

    @Override
    public boolean canSeeActivitiesOf(Group group) {
        return isMember(group);
    }

    @Override
    public boolean canJoinGroup(Group group) {
        return true;
    }

    @Override
    public boolean canLeaveGroup(Group group) {
        return true;
    }
}
