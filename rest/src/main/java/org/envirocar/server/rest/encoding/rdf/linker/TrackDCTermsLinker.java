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

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;

import org.envirocar.server.core.entities.Track;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;

import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.TracksResource;
import org.envirocar.server.rest.rights.AccessRights;

/**
 * @author Jan Wirwahn
 */
public class TrackDCTermsLinker implements RDFLinker<Track> {
    public static final String ODBL_URL =
            "http://opendatacommons.org/licenses/odbl/";

    @Override
    public void link(Model m, Track t, AccessRights rights,
                     Provider<UriBuilder> uriBuilder) {
        m.setNsPrefix(DCTerms.PREFIX, DCTerms.URI);
        String uri = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.TRACKS)
                .path(TracksResource.TRACK)
                .build(t.getIdentifier()).toASCIIString();

        m.createResource(uri).addProperty(DCTerms.rights, ODBL_URL);
    }
}
