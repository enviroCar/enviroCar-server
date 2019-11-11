/*
 * Copyright (C) 2013-2019 The enviroCar project
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
