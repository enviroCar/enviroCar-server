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
package io.car.server.rest.auth;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.google.inject.Inject;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import io.car.server.core.util.PasswordEncoder;
import io.car.server.core.Service;
import io.car.server.core.entities.User;
import io.car.server.core.exception.UserNotFoundException;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class AuthenticationFilter implements ContainerRequestFilter {
    
    private final Service service;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public AuthenticationFilter(Service service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        String username = request.getRequestHeaders().getFirst(AuthConstants.USERNAME_HEADER);
        String token = request.getRequestHeaders().getFirst(AuthConstants.TOKEN_HEADER);

        if (username != null) {
            if (username.isEmpty() || token == null || token.isEmpty()) {
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
            try {
                User user = service.getUser(username);
                if (passwordEncoder.verify(token, user.getToken())) {
                    request.setSecurityContext(new CustomSecurityContext(username, request.isSecure(), user.isAdmin()));
                } else {
                    throw new WebApplicationException(Status.FORBIDDEN);
                }
            } catch (UserNotFoundException ex) {
                throw new WebApplicationException(ex, Status.FORBIDDEN);
            }
        }
        return request;
    }

    private class CustomSecurityContext implements SecurityContext {
        private final Principal principal;
        private final boolean secure;
        private final boolean isAdmin;

        CustomSecurityContext(final String name, boolean secure, boolean isAdmin) {
            this.secure = secure;
            this.isAdmin = isAdmin;
            this.principal = new CustomPrincipal(name);
        }

        @Override
        public Principal getUserPrincipal() {
            return this.principal;
        }

        @Override
        public boolean isUserInRole(String role) {
            if (role.equals(AuthConstants.ADMIN_ROLE)) {
                return isAdmin;
            } else {
                return role.equals(AuthConstants.USER_ROLE);
            }
        }

        @Override
        public boolean isSecure() {
            return secure;
        }

        @Override
        public String getAuthenticationScheme() {
            return AuthConstants.AUTH_SCHEME;
        }
    }

    private class CustomPrincipal implements Principal {
        private final String name;

        CustomPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
