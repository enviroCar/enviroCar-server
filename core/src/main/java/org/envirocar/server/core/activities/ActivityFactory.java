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
package org.envirocar.server.core.activities;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;

import com.google.inject.assistedinject.Assisted;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface ActivityFactory {
    Activity createActivity(ActivityType type,
                            User user);

    GroupActivity createGroupActivity(ActivityType type,
                                      User user,
                                      Group group);

    TrackActivity createTrackActivity(ActivityType type,
                                      User user,
                                      Track track);

    UserActivity createUserActivity(ActivityType type,
                                    @Assisted("user") User user,
                                    @Assisted("other") User other);
}
