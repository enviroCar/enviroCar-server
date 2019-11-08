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
package org.envirocar.server.core.validation;

import org.envirocar.server.core.entities.PolicyType;
import org.envirocar.server.core.entities.User;

import java.util.Arrays;
import java.util.Objects;

public interface LegalPolicyValidator {
    void validatePrivacyStatement(User user) throws LegalPolicyException;

    void validateTermsOfUse(User user) throws LegalPolicyException;


    default void validateAll(User user) throws LegalPolicyException {
        Objects.requireNonNull(user);
        validateTermsOfUse(user);
        validatePrivacyStatement(user);
    }

    default void validate(PolicyType[] types, User user) throws LegalPolicyException {
        Arrays.stream(types).forEach(type -> validate(type, user));
    }

    default void validate(PolicyType type, User user) throws LegalPolicyException {
        Objects.requireNonNull(type);
        Objects.requireNonNull(user);
        switch (type) {
            case TERMS_OF_USE:
                validateTermsOfUse(user);
                break;
            case PRIVACY_STATEMENT:
                validatePrivacyStatement(user);
                break;
            default:
                throw new IllegalArgumentException("Unsupported policy type: " + type);
        }
    }
}
