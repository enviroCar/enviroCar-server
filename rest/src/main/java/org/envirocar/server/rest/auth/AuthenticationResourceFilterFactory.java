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
package org.envirocar.server.rest.auth;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import org.envirocar.server.rest.ForbiddenException;
import org.envirocar.server.rest.UnauthorizedException;

import java.util.Collections;
import java.util.List;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class AuthenticationResourceFilterFactory implements ResourceFilterFactory {
    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        if (am.getAnnotation(Authenticated.class) != null) {
            return Collections.singletonList(new AuthenticatedResourceFilter());
        }
        if (am.getAnnotation(Anonymous.class) != null) {
            return Collections.singletonList(new AnonymousResourceFilter());
        }
        return null;
    }

    private static class AuthenticatedResourceFilter implements ResourceFilter {
        @Override
        public ContainerRequestFilter getRequestFilter() {
            return new AuthenticatedRequestFilter();
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }

    private static class AnonymousResourceFilter implements ResourceFilter {
        @Override
        public ContainerRequestFilter getRequestFilter() {
            return new AnonymousRequestFilter();
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }

    private static class AnonymousRequestFilter implements ContainerRequestFilter {
        @Override
        public ContainerRequest filter(ContainerRequest request) {
            if (request.getSecurityContext().getAuthenticationScheme() == null) {
                return request;
            }
            throw new ForbiddenException();
        }
    }

    private static class AuthenticatedRequestFilter implements ContainerRequestFilter {
        @Override
        public ContainerRequest filter(ContainerRequest request) {
            if (request.getSecurityContext()
                       .isUserInRole(AuthConstants.ADMIN_ROLE)) {
                return request;
            }
            if (!request.getSecurityContext().isUserInRole(AuthConstants.USER_ROLE)) {
                throw new UnauthorizedException();
            }
            return request;
        }
    }
}
