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
package org.envirocar.server.rest.encoding.rdf.linker;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.resources.PhenomenonsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 *
 * @author Arne de Wall
 *
 */
public class EEAPhenomenonLinker implements RDFLinker<Phenomenon> {
    private static final String CO2 = "co2";
    private static final String URI_CO2 =
            "http://dd.eionet.europa.eu/vocabulary/aq/pollutant/71";

    @Override
    public void link(Model m, Phenomenon t, AccessRights rights,
                     String uri, Provider<UriBuilder> uriBuilder) {
        UriBuilder phenomenonBuilder = uriBuilder.get()
                .path(RootResource.class).path(RootResource.PHENOMENONS)
                .path(PhenomenonsResource.PHENOMENON);

        if (t.getName().equals(CO2)) {
            m.createResource(uri)
                    .addProperty(OWL.sameAs, m.createResource(URI_CO2));
        }
    }
}
