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
package org.envirocar.server.rest.auth;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.envirocar.server.core.entities.User;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class SecurityContextImpl implements SecurityContext {
    private final PrincipalImpl principal;
    private final boolean secure;

    SecurityContextImpl(User user, boolean secure) {
        this.secure = secure;
        this.principal = new PrincipalImpl(user);
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        if (role.equals(AuthConstants.ADMIN_ROLE)) {
            return principal.getUser().isAdmin();
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
