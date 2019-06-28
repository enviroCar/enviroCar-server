package org.envirocar.server.core.validation;

public class PrivacyStatementException extends LegalPolicyException {

    public PrivacyStatementException(String currentVersion, String acceptedVersion) {
        super("privacy statement", currentVersion, acceptedVersion);
    }
}
