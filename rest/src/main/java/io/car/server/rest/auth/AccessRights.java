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

/**
 * TODO JavaDoc
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface AccessRights {
    boolean isSelf(User user);

    boolean canSeeTracksOf(User user);

    boolean canSeeMeasurementsOf(User user);

    boolean canSeeFriendsOf(User user);

    boolean canSeeGroupsOf(User user);

    boolean canSeeProfileOf(User user);

    boolean canSeeMailAddressOf(User user);

    boolean canSeeUserOf(Track track);

    boolean canSeeUserOf(Measurement measurement);

    boolean canSeeActivitiesOf(User user);

    boolean canSeeActivitiesOf(Group group);

    boolean canModify(User user);

    boolean canModify(Track track);

    boolean canModify(Measurement measurement);

    boolean canModify(Group group);

    boolean canModify(Sensor sensor);

    boolean canModify(Phenomenon phenomenon);

    boolean canJoinGroup(Group group);

    boolean canLeaveGroup(Group group);

    boolean canDelete(User user);

    boolean canDelete(Track track);

    boolean canDelete(Measurement measurement);

    boolean canDelete(Group group);

    boolean canDelete(Sensor sensor);

    boolean canDelete(Phenomenon phenomenon);
}
