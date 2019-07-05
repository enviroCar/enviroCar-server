/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import org.envirocar.server.core.*;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.rest.ForbiddenException;
import org.envirocar.server.rest.auth.PrincipalImpl;
import org.envirocar.server.rest.UnauthorizedException;
import org.envirocar.server.rest.pagination.PaginationProvider;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractResource {

    public static final String ALLOWED_MAIL_ADDRESSES = "allowedMailAddresses";
    public static final String NOT_ALLOWED_MAIL_ADDRESS
            = "enviroCar is currently in a closed beta phase. Please "
            + "contact envirocar@52north.org if you want to join the beta "
            + "testers or with any other inquiries.";
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
    private Provider<Optional<Set<String>>> allowedMailAddresses;
    private PaginationProvider pagination;

    protected AccessRights getRights() {
        return rights.get();
    }

    protected Pagination getPagination() throws BadRequestException {
        return pagination.get();
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

    protected UserStatisticService getUserStatisticService() {
        return userStatisticService.get();
    }

    protected ResourceFactory getResourceFactory() {
        return resourceFactory.get();
    }

    protected EntityFactory getEntityFactory() {
        return entityFactory.get();
    }

    protected void checkRights(boolean right) {
        if (!right) {
            throw getRights().isAuthenticated()
                    ? new ForbiddenException("forbidden")
                    : new UnauthorizedException("unauthorized");
        }
    }

    protected User getCurrentUser() {
        PrincipalImpl p = (PrincipalImpl) securityContext.get().getUserPrincipal();
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

    protected void checkMail(User user) {
        if (user.hasMail() && allowedMailAddresses.get().isPresent()
                && !allowedMailAddresses.get().get().contains(user.getMail())) {
            throw new ForbiddenException(NOT_ALLOWED_MAIL_ADDRESS);
        }
    }

    @Inject
    public void setAllowedMailAddresses(
            @Named(ALLOWED_MAIL_ADDRESSES) Provider<Optional<Set<String>>> allowedMailAddresses) {
        this.allowedMailAddresses = allowedMailAddresses;
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
