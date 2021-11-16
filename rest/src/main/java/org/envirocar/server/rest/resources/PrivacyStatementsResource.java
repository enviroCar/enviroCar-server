/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.PrivacyStatements;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.rights.AllowOutdatedTerms;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public class PrivacyStatementsResource extends AbstractResource {

    public static final String PRIVACY_STATEMENT = "{id}";

    @GET
    @AllowOutdatedTerms
    @Schema(response = Schemas.PRIVACY_STATEMENTS)
    @Produces({MediaTypes.JSON})
    public PrivacyStatements get() throws BadRequestException {
        return getDataService().getPrivacyStatements(getPagination());
    }

    @Path(PRIVACY_STATEMENT)
    public PrivacyStatementResource privacyStatement(@PathParam("id") String id) throws ResourceNotFoundException {
        PrivacyStatement privacyStatement = getDataService().getPrivacyStatement(id);
        return getResourceFactory().createPrivacyStatementResource(privacyStatement);
    }
}
