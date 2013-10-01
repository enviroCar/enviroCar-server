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
import org.envirocar.server.rest.encoding.rdf.vocab.DUL;
import org.envirocar.server.rest.encoding.rdf.vocab.SSN;
import org.envirocar.server.rest.resources.PhenomenonsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * TODO JavaDoc
 * 
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class PhenomenonSSNLinker extends AbstractSSNLinker<Phenomenon> {
    @Override
    protected void link1(Model m, Phenomenon t, AccessRights rights,
            Resource uri, Provider<UriBuilder> uriBuilder) {
        Resource phenomenon = m.createResource(uriBuilder.get()
                .path(RootResource.class).path(RootResource.PHENOMENONS)
                .path(PhenomenonsResource.PHENOMENON).build(t.getName())
                .toASCIIString());
        phenomenon.addProperty(RDF.type, SSN.Property);
        Resource unit = m.createResource(fragment(phenomenon,
                MeasurementSSNLinker.UNIT_FRAGMENT));
        unit.addProperty(RDF.type, DUL.UnitOfMeasure);
        unit.addLiteral(RDFS.comment, t.getUnit());
    }

}
