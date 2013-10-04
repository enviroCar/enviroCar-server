/*
 * Copyright (C) 2013 The enviroCar project
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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.envirocar.server.core.entities.TermsOfUse;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.validation.Schema;

public class TermsOfUseResource extends AbstractResource {

    public static final String TERMS_OF_USE_INSTANCE = "{termsOfUse}";

	@GET
    @Schema(response = Schemas.TERMS_OF_USE)
    @Produces({ MediaTypes.TERMS_OF_USE })
    public TermsOfUse get(
            @QueryParam(RESTConstants.LIMIT) @DefaultValue("0") int limit,
            @QueryParam(RESTConstants.PAGE) @DefaultValue("0") int page) {
        return getDataService().getTermsOfUse(new Pagination(limit, page));
    }
    
    @Path(TERMS_OF_USE_INSTANCE)
    public TermsOfUseInstanceResource track(@PathParam("termsOfUse") String id)
            throws ResourceNotFoundException {
        TermsOfUseInstance t = getDataService().getTermsOfUseInstance(id);
        checkRights(getRights().canSee(t));
        return getResourceFactory().createTermsOfUseInstanceResource(t);
    }
	
}
