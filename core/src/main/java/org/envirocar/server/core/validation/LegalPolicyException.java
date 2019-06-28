package org.envirocar.server.core.validation;

import org.envirocar.server.core.exception.ValidationException;

public abstract class LegalPolicyException extends ValidationException {
    private final String currentVersion;
    private final String acceptedVersion;
    private final String legalPolicyType;

    public LegalPolicyException(String legalPolicyType, String currentVersion, String acceptedVersion) {
        super(String.format("User has accepted the %s only in version %s, the current one is %s",
                legalPolicyType, acceptedVersion, currentVersion));
        this.legalPolicyType = legalPolicyType;
        this.currentVersion = currentVersion;
        this.acceptedVersion = acceptedVersion;
    }

    public String getCurrentVersion() {
        return this.currentVersion;
    }

    public String getAcceptedVersion() {
        return this.acceptedVersion;
    }

    public String getLegalPolicyType() {
        return this.legalPolicyType;
    }

}
