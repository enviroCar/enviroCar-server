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

import com.google.inject.Inject;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.util.PasswordEncoder;
import org.envirocar.server.rest.ForbiddenException;

import javax.ws.rs.core.HttpHeaders;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class AuthenticationFilter implements ContainerRequestFilter {
    private final UserService service;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public AuthenticationFilter(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        String username = request.getHeaderValue(AuthConstants.USERNAME_HEADER);
        String token = request.getHeaderValue(AuthConstants.TOKEN_HEADER);
        String auth = request.getHeaderValue(HttpHeaders.AUTHORIZATION);

        if (username == null) {
            if (auth != null) {
                if (!auth.startsWith("Basic ")) {
                    throw new BadRequestException("unsupported authorization scheme");
                }
                String decoded = new String(Base64.decode(auth.replaceFirst("Basic ", "")));
                int sep = decoded.indexOf(':');
                if (sep >= 0) {
                    username = decoded.substring(0, sep);
                    token = decoded.substring(sep + 1);
                } else {
                    throw new BadRequestException("invalid HTTP basic token");
                }
            }
        }
        if (username != null) {
            if (username.isEmpty() || token == null || token.isEmpty()) {
                throw new BadRequestException("invalid username or password");
            }
            try {
                User user = service.getUser(username, true);
                if (passwordEncoder.verify(token, user.getToken())) {
                    if (!user.isConfirmed()) {
                        throw new ForbiddenException("mail not confirmed");
                    } else {
                        request.setSecurityContext(new SecurityContextImpl(user, request.isSecure()));
                    }
                }
            } catch (UserNotFoundException ignored) {
            }
            if (request.getUserPrincipal() == null) {
                throw new ForbiddenException("invalid username or password");
            }

        }
        return request;
    }
}
