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
package org.envirocar.server.rest.encoding.rdf.linker;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.rest.encoding.rdf.vocab.DUL;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.TracksResource;

import javax.ws.rs.core.UriBuilder;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class MeasurementDULLinker extends AbstractDULLinker<Measurement> {

    @Override
    protected void linkInternal(Model model, Measurement entity,
                                Resource resource,
                                Provider<UriBuilder> uriBuilder) {
        if (entity.hasTrack()) {
            String uri = getTrackURI(uriBuilder, entity.getTrack());
            resource.addProperty(DUL.isMemberOf, model.createResource(uri));
        }
    }

    private String getTrackURI(Provider<UriBuilder> uriBuilder, Track entity) {
        return uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.TRACKS)
                .path(TracksResource.TRACK)
                .build(entity.getIdentifier())
                .toASCIIString();
    }

}
