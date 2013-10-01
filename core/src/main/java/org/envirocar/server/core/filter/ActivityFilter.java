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
package org.envirocar.server.core.filter;

import org.envirocar.server.core.activities.ActivityType;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ActivityFilter {
    private final Group group;
    private final User user;
    private final ActivityType type;
    private final boolean friendActivities;
    private final Pagination pagination;

    private ActivityFilter(Group group, User user, ActivityType type,
                           boolean friendActivities, Pagination pagination) {
        this.group = group;
        this.user = user;
        this.type = type;
        this.friendActivities = friendActivities;
        this.pagination = pagination;
    }

    public ActivityFilter(Group group, User user, ActivityType type,
                          Pagination pagination) {
        this(group, user, type, false, pagination);
    }

    public ActivityFilter(User user, Pagination pagination) {
        this(null, user, null, true, pagination);
    }

    public Group getGroup() {
        return group;
    }

    public boolean hasGroup() {
        return group != null;
    }

    public User getUser() {
        return user;
    }

    public boolean hasUser() {
        return user != null;
    }

    public ActivityType getType() {
        return type;
    }

    public boolean hasType() {
        return type != null;
    }

    public boolean isFriendActivities() {
        return friendActivities;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public boolean hasPagination() {
        return pagination != null;
    }
}
