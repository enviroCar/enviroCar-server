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
package org.envirocar.server.rest.mapper;

import org.envirocar.server.core.validation.LegalPolicyException;
import org.envirocar.server.rest.util.CustomStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class LegalPolicyExceptionMapper extends AbstractExceptionMapper<LegalPolicyException> {

    private static final Response.StatusType UNAVAILABLE_FOR_LEGAL_REASONS
            = new CustomStatus(451, "Unavailable For Legal Reasons");

    @Override
    protected Response.StatusType getStatus(LegalPolicyException exception) {
        return UNAVAILABLE_FOR_LEGAL_REASONS;
    }
}
