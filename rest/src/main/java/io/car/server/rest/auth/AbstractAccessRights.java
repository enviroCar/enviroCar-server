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

import javax.ws.rs.core.SecurityContext;

import com.google.inject.Inject;

import io.car.server.core.Service;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.rest.auth.PrincipalImpl;

public abstract class AbstractAccessRights implements AccessRights {
    private User user;
    private Service service;

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
        return user.equals(this.user);
    }

    protected boolean isFriend(User user) {
        return service.isFriend(this.user, user);
    }

    protected boolean isFriendOf(User user) {
        return service.isFriend(user, this.user);
    }

    protected boolean shareGroup(User user) {
        return service.shareGroup(this.user, user);
    }

    protected boolean isMember(Group group) {
        return service.isGroupMember(group, this.user);
    }
}
