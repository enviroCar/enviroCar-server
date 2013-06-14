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
package io.car.server.rest.rights;

import java.util.Map;

import javax.ws.rs.core.SecurityContext;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import io.car.server.core.Service;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.rest.auth.PrincipalImpl;

public abstract class AbstractAccessRights implements AccessRights {
    private User user;
    private Service service;
    private final Map<User, Boolean> isFriend = Maps.newHashMap();
    private final Map<User, Boolean> isFriendOf = Maps.newHashMap();
    private final Map<Group, Boolean> isMember = Maps.newHashMap();
    private final Map<User, Boolean> shareGroup = Maps.newHashMap();

    @Inject
    public void setService(Service service) {
        this.service = service;
    }

    @Inject
    public void setSecurityContext(SecurityContext ctx) {
        PrincipalImpl p = (PrincipalImpl) ctx.getUserPrincipal();
        this.user = p == null ? null : p.getUser();
    }

    @Override
    public boolean isSelf(User user) {
        if (this.user == null || user == null) {
            return false;
        }
        return user.equals(this.user);
    }

    protected boolean isFriend(User user) {
        if (this.user == null || user == null) {
            return false;
        }
        if (!isFriend.containsKey(user)) {
            isFriend.put(user, service.isFriend(this.user, user));
        }
        return isFriend.get(user).booleanValue();
    }

    protected boolean isFriendOf(User user) {
        if (this.user == null || user == null) {
            return false;
        }
        if (!isFriendOf.containsKey(user)) {
            isFriendOf.put(user, service.isFriend(user, this.user));
        }
        return isFriendOf.get(user).booleanValue();
    }

    protected boolean shareGroup(User user) {
        if (this.user == null || user == null) {
            return false;
        }
        if (!shareGroup.containsKey(user)) {
            shareGroup.put(user, service.shareGroup(this.user, user));
        }
        return shareGroup.get(user).booleanValue();
    }

    protected boolean isMember(Group group) {
        if (this.user == null || group == null) {
            return false;
        }
        if (!isMember.containsKey(group)) {
            isMember.put(group, service.isGroupMember(group, this.user));
        }
        return isMember.get(group).booleanValue();
    }

    protected boolean isSelfFriendOfOrShareGroup(User user) {
        return isSelf(user) || isFriendOf(user) || shareGroup(user);
    }
}
