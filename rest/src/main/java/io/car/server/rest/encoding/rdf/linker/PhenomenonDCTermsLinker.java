/*
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
package io.car.server.rest.encoding.rdf.linker;

import javax.ws.rs.core.UriBuilder;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.core.entities.Phenomenon;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.PhenomenonsResource;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.rights.AccessRights;

/**
 *
 * @author Jan Wirwahn
 */
public class PhenomenonDCTermsLinker implements RDFLinker<Phenomenon> {
    public static final String ODBL_URL =
            "http://opendatacommons.org/licenses/odbl/";

    @Override
    public void link(Model m, Phenomenon t, AccessRights rights,
                     Provider<UriBuilder> uriBuilder) {
        String uri = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.PHENOMENONS)
                .path(PhenomenonsResource.PHENOMENON)
                .build(t.getName()).toASCIIString();

        m.createResource(uri).addProperty(DCTerms.rights, ODBL_URL);

    }
}
