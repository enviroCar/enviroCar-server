/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.car.server.core.entities.Phenomenons;
import io.car.server.core.exception.PhenomenonNotFoundException;
import io.car.server.rest.AbstractResource;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class PhenomenonsResource extends AbstractResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Phenomenons get() {
        return getService().getAllPhenomenons();
    }

    @Path("{id}")
    public PhenomenonResource phenomenon(@PathParam("id") String id) throws PhenomenonNotFoundException {
        return getResourceFactory().createPhenomenonResource(getService().getPhenomenonByName(id));
    }
}
