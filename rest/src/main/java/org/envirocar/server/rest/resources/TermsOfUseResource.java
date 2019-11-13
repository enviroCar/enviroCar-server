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
package org.envirocar.server.rest.resources;

import org.envirocar.server.core.entities.TermsOfUse;
import org.envirocar.server.core.entities.TermsOfUseInstance;
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

public class TermsOfUseResource extends AbstractResource {

    public static final String TERMS_OF_USE_INSTANCE = "{id}";

    @GET
    @AllowOutdatedTerms
    @Schema(response = Schemas.TERMS_OF_USE)
    @Produces({MediaTypes.JSON})
    public TermsOfUse get() throws BadRequestException {
        return getDataService().getTermsOfUse(getPagination());
    }

    @Path(TERMS_OF_USE_INSTANCE)
    public TermsOfUseInstanceResource termsOfUseInstance(@PathParam("id") String id) throws ResourceNotFoundException {
        TermsOfUseInstance t = getDataService().getTermsOfUseInstance(id);
        return getResourceFactory().createTermsOfUseInstanceResource(t);
    }

}
