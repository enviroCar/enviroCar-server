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
