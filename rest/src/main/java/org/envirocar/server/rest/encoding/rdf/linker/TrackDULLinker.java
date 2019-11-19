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
package org.envirocar.server.rest.encoding.rdf.linker;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.encoding.rdf.vocab.DUL;
import org.envirocar.server.rest.resources.MeasurementsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class TrackDULLinker extends AbstractDULLinker<Track> {
    private final DataService dataService;

    @Inject
    public TrackDULLinker(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    protected void linkInternal(Model model, Track track, AccessRights rights,
                                Resource trackResource,
                                Provider<UriBuilder> uriBuilder) {
        UriBuilder measurementURIBuilder = getMeasurementURIBuilder(uriBuilder);
        trackResource.addProperty(RDF.type, DUL.Collection);
        if (rights.canSeeMeasurementsOf(track)) {
            for (Measurement measurement : getMeasurements(track)) {
                URI uri = measurementURIBuilder.build(measurement.getIdentifier());
                Resource measurementResource = model.createResource(uri.toASCIIString());
                trackResource.addProperty(DUL.hasMember, measurementResource);
            }
        }
    }

    //FIXME this really should not be needed
    private Measurements getMeasurements(Track track) {
        return dataService.getMeasurements(new MeasurementFilter(track));
    }

    private UriBuilder getMeasurementURIBuilder(Provider<UriBuilder> uriBuilder) {
        return uriBuilder.get().path(RootResource.class)
                .path(RootResource.MEASUREMENTS)
                .path(MeasurementsResource.MEASUREMENT);
    }

}
