/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package io.car.server.rest.auth;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class AuthenticationResourceFilterFactory implements ResourceFilterFactory {

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        Authenticated authenticated = am.getAnnotation(Authenticated.class);
        if (authenticated != null) {
            return Collections
                    .<ResourceFilter>singletonList(new AuthenticatedResourceFilter());
        }
        Anonymous anonymous = am.getAnnotation(Anonymous.class);
        if (anonymous != null) {
            return Collections.<ResourceFilter>singletonList(new AnonymousResourceFilter());
        }
        return null;
    }

    private class AuthenticatedResourceFilter implements ResourceFilter {

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return new AuthenticatedRequestFilter();
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }

    private class AnonymousResourceFilter implements ResourceFilter {
        @Override
        public ContainerRequestFilter getRequestFilter() {
            return new AnonymousRequestFilter();
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }

    private class AnonymousRequestFilter implements ContainerRequestFilter {
        @Override
        public ContainerRequest filter(ContainerRequest request) {
            if (request.getSecurityContext().getAuthenticationScheme() == null) {
                return request;
            }
            throw new WebApplicationException(Status.FORBIDDEN);
        }
    }

    private class AuthenticatedRequestFilter implements ContainerRequestFilter {

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            if (request.getSecurityContext().isUserInRole(AuthConstants.ADMIN_ROLE)) {
                return request;
            }
            if (!request.getSecurityContext().isUserInRole(AuthConstants.USER_ROLE)) {
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
            return request;
        }
    }
}
