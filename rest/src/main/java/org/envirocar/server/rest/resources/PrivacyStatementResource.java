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
package org.envirocar.server.rest.resources;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.resources.AbstractResource;
import org.envirocar.server.rest.validation.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

public class PrivacyStatementResource extends AbstractResource {
    private final PrivacyStatement privacyStatement;

    @Inject
    public PrivacyStatementResource(@Assisted PrivacyStatement privacyStatement) {
        this.privacyStatement = privacyStatement;
    }

    @GET
    @Schema(response = Schemas.PRIVACY_STATEMENT)
    @Produces({MediaTypes.PRIVACY_STATEMENT})
    public PrivacyStatement get() {
        return privacyStatement;
    }

}
