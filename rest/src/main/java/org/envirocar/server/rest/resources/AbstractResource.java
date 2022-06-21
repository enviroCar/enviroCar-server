/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.StatisticsService;
import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.TemporalFilterOperator;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.UserStatisticService;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.rest.ForbiddenException;
import org.envirocar.server.rest.UnauthorizedException;
import org.envirocar.server.rest.auth.PrincipalImpl;
import org.envirocar.server.rest.pagination.PaginationProvider;
import org.envirocar.server.rest.rights.AccessRights;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

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
    private Provider<UserStatisticService> userStatisticService;
    private Provider<UriInfo> uriInfo;
    private Provider<ResourceFactory> resourceFactory;
    private Provider<EntityFactory> entityFactory;
    private PaginationProvider pagination;

    protected AccessRights getRights() {
        return this.rights.get();
    }

    protected Pagination getPagination() throws BadRequestException {
        return this.pagination.get();
    }

    protected UriInfo getUriInfo() {
        return this.uriInfo.get();
    }

    protected DataService getDataService() {
        return this.dataService.get();
    }

    protected FriendService getFriendService() {
        return this.friendService.get();
    }

    protected GroupService getGroupService() {
        return this.groupService.get();
    }

    protected UserService getUserService() {
        return this.userService.get();
    }

    protected StatisticsService getStatisticsService() {
        return this.statisticsService.get();
    }

    protected UserStatisticService getUserStatisticService() {
        return this.userStatisticService.get();
    }

    protected ResourceFactory getResourceFactory() {
        return this.resourceFactory.get();
    }

    protected EntityFactory getEntityFactory() {
        return this.entityFactory.get();
    }

    protected void checkRights(boolean right) {
        if (!right) {
            throw getRights().isAuthenticated()
                  ? new ForbiddenException("forbidden")
                  : new UnauthorizedException("unauthorized");
        }
    }

    protected User getCurrentUser() {
        PrincipalImpl p = (PrincipalImpl) this.securityContext.get().getUserPrincipal();
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
    public void setStatisticsService(Provider<StatisticsService> statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Inject
    public void setUserStatisticService(Provider<UserStatisticService> userStatisticsService) {
        this.userStatisticService = userStatisticsService;
    }

    @Inject
    public void setPagination(PaginationProvider pagination) {
        this.pagination = pagination;
    }

    protected TemporalFilter parseTemporalFilterForInstant() {
        for (TemporalFilterOperator op : TemporalFilterOperator.values()) {
            String param = getUriInfo().getQueryParameters().getFirst(op.name());
            if (param != null) {
                try {
                    return op.parseFilterForInstant(param);
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException(e);
                }
            }
        }
        return null;
    }

    protected TemporalFilter parseTemporalFilterForInterval() {
        for (TemporalFilterOperator op : TemporalFilterOperator.values()) {
            String param = getUriInfo().getQueryParameters().getFirst(op.name());
            if (param != null) {
                try {
                    return op.parseFilterForInterval(param);
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException(e);
                }
            }
        }
        return null;
    }
}
