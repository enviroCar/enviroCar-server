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
package org.envirocar.server.rest.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.envirocar.server.core.DataService;
import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.StatisticsService;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.auth.PrincipalImpl;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractResource {
    private Provider<SecurityContext> securityContext;
    private Provider<AccessRights> rights;
    private Provider<DataService> dataService;
    private Provider<FriendService> friendService;
    private Provider<GroupService> groupService;
    private Provider<UserService> userService;
    private Provider<StatisticsService> statisticsService;
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

    protected StatisticsService getStatisticsService() {
        return statisticsService.get();
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

    @Inject
    public void setStatisticsService(
            Provider<StatisticsService> statisticsService) {
        this.statisticsService = statisticsService;
    }
}
