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
import com.google.inject.Provider;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.*;
import org.envirocar.server.core.entities.PolicyType;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.rest.resources.PrivacyStatementsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.TermsOfUseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * ResourceFilterFactory for resources annotated with {@link HasAcceptedLatestLegalPolicies}.
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class HasAcceptedLatestLegalPoliciesResourceFilterFactory implements ResourceFilterFactory {
    private static final Logger LOG = LoggerFactory.getLogger(HasAcceptedLatestLegalPoliciesResourceFilterFactory.class);
    private final Supplier<Optional<TermsOfUseInstance>> termsOfUseSupplier;
    private final Supplier<Optional<PrivacyStatement>> privacyStatementSupplier;
    private final Provider<UriInfo> uriInfo;

    @Inject
    public HasAcceptedLatestLegalPoliciesResourceFilterFactory(
            Supplier<Optional<TermsOfUseInstance>> termsOfUseSupplier,
            Supplier<Optional<PrivacyStatement>> privacyStatementSupplier,
            Provider<UriInfo> uriInfo) {
        this.termsOfUseSupplier = Objects.requireNonNull(termsOfUseSupplier);
        this.privacyStatementSupplier = Objects.requireNonNull(privacyStatementSupplier);
        this.uriInfo = Objects.requireNonNull(uriInfo);
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        HasAcceptedLatestLegalPolicies annotation = am.getAnnotation(HasAcceptedLatestLegalPolicies.class);
        if (annotation != null) {
            PolicyType[] policyTypes = annotation.value();
            if (policyTypes.length > 0) {
                return Collections.singletonList(new LegalPolicyResourceFilter(termsOfUseSupplier, privacyStatementSupplier, policyTypes));
            } else {
                LOG.warn("No policies given for method {}", am);
            }
        }
        return Collections.emptyList();
    }

    private class LegalPolicyResourceFilter implements ResourceFilter, ContainerRequestFilter, ContainerResponseFilter {
        private static final String PRIVACY_STATEMENT_HEADER = "Privacy-Statement";
        private static final String TERMS_OF_USE_HEADER = "Terms-Of-Use";
        private static final String ACCEPT_TERMS_OF_USE_HEADER = "Accept-Terms-Of-Use";
        private static final String ACCEPT_PRIVACY_STATEMENT = "Accept-Privacy-Statement";
        private final Supplier<Optional<TermsOfUseInstance>> termsOfUseSupplier;
        private final Supplier<Optional<PrivacyStatement>> privacyStatementSupplier;
        private final PolicyType[] policyTypes;

        private LegalPolicyResourceFilter(Supplier<Optional<TermsOfUseInstance>> termsOfUseSupplier,
                                          Supplier<Optional<PrivacyStatement>> privacyStatementSupplier,
                                          PolicyType[] policyTypes) {
            this.termsOfUseSupplier = termsOfUseSupplier;
            this.privacyStatementSupplier = privacyStatementSupplier;
            this.policyTypes = policyTypes;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return this;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return this;
        }

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            for (PolicyType policyType : this.policyTypes) {
                switch (policyType) {
                    case PRIVACY_STATEMENT:
                        checkPrivacyStatementHeader(request);
                        break;
                    case TERMS_OF_USE:
                        checkTermsOfUseHeader(request);
                        break;
                    default:
                        throw new IllegalArgumentException("unsupported policy type: " + policyType);
                }
            }
            return request;
        }

        @Override
        public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
            for (PolicyType policyType : this.policyTypes) {
                switch (policyType) {
                    case PRIVACY_STATEMENT:
                        addPrivacyStatementHeader(response);
                        break;
                    case TERMS_OF_USE:
                        addTermsOfUseHeader(response);
                        break;
                    default:
                        throw new IllegalArgumentException("unsupported policy type: " + policyType);
                }
            }
            return response;
        }

        private void checkTermsOfUseHeader(ContainerRequest request) throws TermsOfUseException {
            String headerValue = request.getHeaderValue(ACCEPT_TERMS_OF_USE_HEADER);
            if (!Boolean.parseBoolean(headerValue)) {
                this.termsOfUseSupplier.get().ifPresent(terms -> {
                    throw createTermsOfUseException(terms);
                });
            }
        }

        private void checkPrivacyStatementHeader(ContainerRequest request) throws PrivacyStatementException {
            String headerValue = request.getHeaderValue(ACCEPT_PRIVACY_STATEMENT);
            if (!Boolean.parseBoolean(headerValue)) {
                this.privacyStatementSupplier.get().ifPresent(terms -> {
                    throw createPrivacyStatementException(terms);
                });
            }
        }

        private TermsOfUseException createTermsOfUseException(TermsOfUseInstance terms) {
            return new TermsOfUseException(String.format(
                    "not accepting terms of use in version %s, available under %s",
                    terms.getIssuedDate(), getTermsOfUseUri(terms)));
        }

        private PrivacyStatementException createPrivacyStatementException(PrivacyStatement terms) {
            return new PrivacyStatementException(String.format(
                    "not accepting privacy statement in version %s, available under %s",
                    terms.getIssuedDate(), getPrivacyStatementUri(terms)));
        }

        private void addTermsOfUseHeader(ContainerResponse response) {
            getTermsOfUseUri().ifPresent(uri -> response.getHttpHeaders().add(TERMS_OF_USE_HEADER, uri));
        }

        private void addPrivacyStatementHeader(ContainerResponse response) {
            getPrivacyStatementUri().ifPresent(uri -> response.getHttpHeaders().add(PRIVACY_STATEMENT_HEADER, uri));
        }

        private Optional<URI> getTermsOfUseUri() {
            return termsOfUseSupplier.get().map(this::getTermsOfUseUri);
        }

        private Optional<URI> getPrivacyStatementUri() {
            return privacyStatementSupplier.get().map(this::getPrivacyStatementUri);
        }

        private URI getTermsOfUseUri(TermsOfUseInstance terms) {
            return uriInfo.get().getBaseUriBuilder()
                    .path(RootResource.TERMS_OF_USE)
                    .path(TermsOfUseResource.TERMS_OF_USE_INSTANCE)
                    .build(terms.getIdentifier());
        }


        private URI getPrivacyStatementUri(PrivacyStatement terms) {
            return uriInfo.get().getBaseUriBuilder()
                    .path(RootResource.PRIVACY_STATEMENTS)
                    .path(PrivacyStatementsResource.PRIVACY_STATEMENT)
                    .build(terms.getIdentifier());
        }


    }


}
