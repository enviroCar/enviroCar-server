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

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Phenomenons;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.PhenomenonNotFoundException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.auth.Authenticated;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class PhenomenonsResource extends AbstractResource {

    public static final String PHENOMENON = "{phenomenon}";

    @GET
    @Schema(response = Schemas.PHENOMENONS)
    public Phenomenons get() throws BadRequestException {
        return getDataService().getPhenomenons(getPagination());
    }

    @POST
    @Authenticated
    @Schema(request = Schemas.PHENOMENON_CREATE)
    @Consumes({MediaTypes.JSON})
    public Response create(Phenomenon phenomenon) {
        return Response.created(getUriInfo().getAbsolutePathBuilder()
                                            .path(getDataService().createPhenomenon(phenomenon)
                                                                  .getName()).build()).build();
    }

    @Path(PHENOMENON)
    public PhenomenonResource phenomenon(@PathParam("phenomenon") String id) throws PhenomenonNotFoundException {
        Phenomenon p = getDataService().getPhenomenonByName(id);
        checkRights(getRights().canSee(p));
        return getResourceFactory().createPhenomenonResource(p);
    }
}
