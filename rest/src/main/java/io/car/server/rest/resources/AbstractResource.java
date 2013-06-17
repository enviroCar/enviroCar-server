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
package io.car.server.rest.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.Provider;

import io.car.server.core.DataService;
import io.car.server.core.FriendService;
import io.car.server.core.GroupService;
import io.car.server.core.UserService;
import io.car.server.core.entities.EntityFactory;
import io.car.server.core.entities.User;
import io.car.server.rest.auth.PrincipalImpl;
import io.car.server.rest.rights.AccessRights;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractResource {
    private Provider<SecurityContext> securityContext;
    private Provider<AccessRights> rights;
    private Provider<DataService> dataService;
    private Provider<FriendService> friendService;
    private Provider<GroupService> groupService;
    private Provider<UserService> userService;
    private Provider<UriInfo> uriInfo;
    private Provider<ResourceFactory> resourceFactory;
    private Provider<EntityFactory> entityFactory;

    protected AccessRights getRights() {
        return rights.get();
    }

    protected UriInfo getUriInfo() {
        return uriInfo.get();
    }

    protected DataService getDataService() {
        return dataService.get();
    }

    protected FriendService getFriendService() {
        return friendService.get();
    }

    protected GroupService getGroupService() {
        return groupService.get();
    }

    protected UserService getUserService() {
        return userService.get();
    }

    protected ResourceFactory getResourceFactory() {
        return resourceFactory.get();
    }

    protected EntityFactory getEntityFactory() {
        return entityFactory.get();
    }

    protected void checkRights(boolean right) {
        if (!right) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
    }

    protected User getCurrentUser() {
        PrincipalImpl p = (PrincipalImpl) securityContext.get()
                .getUserPrincipal();
        return p == null ? null : p.getUser();
    }

    @Inject
    public void setRights(Provider<AccessRights> accessRights) {
        this.rights = accessRights;
    }

    @Inject
    public void setSecurityContext(Provider<SecurityContext> securityContext) {
        this.securityContext = securityContext;
    }

    @Inject
    public void setUriInfo(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Inject
    public void setResourceFactory(Provider<ResourceFactory> resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    @Inject
    public void setEntityFactory(Provider<EntityFactory> entityFactory) {
        this.entityFactory = entityFactory;
    }

    @Inject
    public void setDataService(Provider<DataService> dataService) {
        this.dataService = dataService;
    }

    @Inject
    public void setFriendService(Provider<FriendService> friendService) {
        this.friendService = friendService;
    }

    @Inject
    public void setGroupService(Provider<GroupService> groupService) {
        this.groupService = groupService;
    }

    @Inject
    public void setUserService(Provider<UserService> userService) {
        this.userService = userService;
    }
}
