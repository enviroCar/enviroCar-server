package org.envirocar.server.core.validation;

public class TermsOfUseException extends LegalPolicyException {

    public TermsOfUseException(String currentVersion, String acceptedVersion) {
        super("terms of use", currentVersion, acceptedVersion);
    }
}
