/**
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
package io.car.server.rest;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;

import io.car.server.core.User;
import io.car.server.core.UserService;
import io.car.server.rest.auth.AuthConstants;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public abstract class AbstractResource {
    private SecurityContext securityContext;
    private UserService service;
    private UriInfo uriInfo;
    private ResourceFactory resourceFactory;

    protected boolean canModifyUser(User user) {
        return getSecurityContext().isUserInRole(AuthConstants.ADMIN_ROLE) ||
               (getSecurityContext().getUserPrincipal() != null &&
                getSecurityContext().getUserPrincipal().getName().equals(user.getName()));
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    public UserService getUserService() {
        return service;
    }

    @Inject
    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Inject
    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Inject
    public void setUserService(UserService service) {
        this.service = service;
    }

    @Inject
    public void setResourceFactory(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public ResourceFactory getResourceFactory() {
        return resourceFactory;
    }
}
