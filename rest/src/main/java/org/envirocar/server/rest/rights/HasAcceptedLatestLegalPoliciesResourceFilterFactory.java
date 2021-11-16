/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.rest.rights;

import com.google.inject.Inject;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import org.envirocar.server.core.entities.PolicyType;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.validation.LegalPolicyValidator;
import org.envirocar.server.rest.auth.PrincipalImpl;

import javax.ws.rs.core.SecurityContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ResourceFilterFactory for resources.
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class HasAcceptedLatestLegalPoliciesResourceFilterFactory
        implements ResourceFilterFactory, ResourceFilter, ContainerRequestFilter {
    private final LegalPolicyValidator validator;

    @Inject
    public HasAcceptedLatestLegalPoliciesResourceFilterFactory(LegalPolicyValidator validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        return am instanceof AbstractResourceMethod && !am.isAnnotationPresent(AllowOutdatedTerms.class)
               ? Collections.singletonList(this) : Collections.emptyList();
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        getUser(request).ifPresent(this::validate);
        return request;
    }

    public void validate(User user) {
        this.validator.validate(PolicyType.TERMS_OF_USE, user);
    }

    public Optional<User> getUser(ContainerRequest request) {
        return Optional.ofNullable(request)
                       .map(ContainerRequest::getSecurityContext)
                       .map(SecurityContext::getUserPrincipal)
                       .map(PrincipalImpl.class::cast)
                       .map(PrincipalImpl::getUser);
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }
}
