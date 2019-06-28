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
package org.envirocar.server.rest.rights;

import com.google.inject.Inject;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.*;
import org.envirocar.server.core.entities.PolicyType;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.validation.LegalPolicyValidator;
import org.envirocar.server.rest.auth.PrincipalImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.SecurityContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ResourceFilterFactory for resources annotated with {@link HasAcceptedLatestLegalPolicies}.
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class HasAcceptedLatestLegalPoliciesResourceFilterFactory implements ResourceFilterFactory {
    private static final Logger LOG = LoggerFactory.getLogger(HasAcceptedLatestLegalPoliciesResourceFilterFactory.class);
    private final LegalPolicyValidator validator;

    @Inject
    public HasAcceptedLatestLegalPoliciesResourceFilterFactory(LegalPolicyValidator validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        HasAcceptedLatestLegalPolicies annotation = am.getAnnotation(HasAcceptedLatestLegalPolicies.class);
        if (annotation != null) {
            PolicyType[] policyTypes = annotation.value();
            if (policyTypes.length > 0) {
                LOG.warn("No policies given for method {}", am);
            } else {
                ResourceFilter filter = new LegalPolicyResourceFilter(this.validator, policyTypes);
                return Collections.singletonList(filter);
            }
        }
        return Collections.emptyList();
    }

    private static class LegalPolicyRequestFilter implements ContainerRequestFilter {
        private final LegalPolicyValidator validator;
        private final PolicyType[] policyTypes;

        private LegalPolicyRequestFilter(LegalPolicyValidator validator, PolicyType[] policyTypes) {
            this.validator = validator;
            this.policyTypes = policyTypes;
        }

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            getUser(request).ifPresent(this::validate);
            return request;
        }

        public void validate(User user) {
            this.validator.validate(this.policyTypes, user);
        }

        public Optional<User> getUser(ContainerRequest request) {
            return Optional.ofNullable(request)
                    .map(ContainerRequest::getSecurityContext)
                    .map(SecurityContext::getUserPrincipal)
                    .map(PrincipalImpl.class::cast)
                    .map(PrincipalImpl::getUser);
        }
    }

    private class LegalPolicyResourceFilter implements ResourceFilter {
        private final LegalPolicyValidator validator;
        private final PolicyType[] policyTypes;

        private LegalPolicyResourceFilter(LegalPolicyValidator validator, PolicyType[] policyTypes) {
            this.validator = validator;
            this.policyTypes = policyTypes;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return new LegalPolicyRequestFilter(this.validator, this.policyTypes);
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }
}
