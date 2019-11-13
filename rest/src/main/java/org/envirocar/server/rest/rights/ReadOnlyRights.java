/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.rest.rights;

import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.entities.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReadOnlyRights extends AccessRightsImpl {
    public ReadOnlyRights() {
    }

    public ReadOnlyRights(User user,
                          GroupService groupService,
                          FriendService friendService) {
        super(user, groupService, friendService);
    }

    @Override
    public boolean canDelete(Fueling f) {
        return false;
    }

    @Override
    public boolean canDelete(Group group) {
        return false;
    }

    @Override
    public boolean canDelete(Measurement measurement) {
        return false;
    }

    @Override
    public boolean canDelete(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canDelete(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canDelete(Track track) {
        return false;
    }

    @Override
    public boolean canDelete(User user) {
        return false;
    }

    @Override
    public boolean canModify(Group group) {
        return false;
    }

    @Override
    public boolean canModify(Measurement measurement) {
        return false;
    }

    @Override
    public boolean canModify(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canModify(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canModify(Track track) {
        return false;
    }

    @Override
    public boolean canModify(User user) {
        return false;
    }

    @Override
    public boolean canFriend(User user, User friend) {
        return false;
    }

    @Override
    public boolean canJoinGroup(Group group) {
        return false;
    }

    @Override
    public boolean canLeaveGroup(Group group) {
        return false;
    }

    @Override
    public boolean canUnfriend(User user, User friend) {
        return false;
    }

}
