/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server.core.validation;

import org.envirocar.server.core.TermsRepository;
import org.envirocar.server.core.TermsRepositoryImpl;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.User;

import javax.inject.Inject;

/**
 * {@link EntityValidator} that validates that a {@link User} has accepted the latest {@link TermsOfUseInstance} and
 * {@link PrivacyStatement}.
 */
public class LegalPolicyValidatorImpl implements LegalPolicyValidator {
    private final TermsRepository termsRepository;

    /**
     * Creates a new {@link LegalPolicyValidatorImpl}.
     *
     * @param termsRepository The {@link TermsRepositoryImpl} to retrieve the latest {@link PrivacyStatement} and {@link
     *                        TermsOfUseInstance}.
     */
    @Inject
    public LegalPolicyValidatorImpl(TermsRepository termsRepository) {
        this.termsRepository = termsRepository;
    }

    @Override
    public void validateTermsOfUse(User user) throws TermsOfUseException {
        this.termsRepository.getLatestTermsOfUse().ifPresent(termsOfUse -> {
            String acceptedVersion = user.getTermsOfUseVersion();
            String currentVersion = termsOfUse.getIssuedDate();
            if (!currentVersion.equals(acceptedVersion)) {
                throw new TermsOfUseException(currentVersion, acceptedVersion);
            }
        });
    }

    @Override
    public void validatePrivacyStatement(User user) throws PrivacyStatementException {
        this.termsRepository.getLatestPrivacyStatement().ifPresent(privacyStatement -> {
            String acceptedVersion = user.getPrivacyStatementVersion();
            String currentVersion = privacyStatement.getIssuedDate();
            if (!currentVersion.equals(acceptedVersion)) {
                throw new PrivacyStatementException(currentVersion, acceptedVersion);
            }
        });
    }
}
