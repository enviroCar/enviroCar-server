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

import java.net.URI;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.resources.MeasurementsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Jan Wirwahn
 * @author Christian Autermann <christian@autermann.org>
 */
public class TrackMeasurementsLinker extends DCTermsLinker<Track> {
    private final Set<RDFLinker<Measurement>> linker;
    private final DataService dataService;

    @Inject
    public TrackMeasurementsLinker(Set<RDFLinker<Measurement>> linker,
                                   DataService dataService) {
        this.linker = linker;
        this.dataService = dataService;
    }

    @Override
    public void linkRest(Model m, Track t, AccessRights rights,
                         Resource r, Provider<UriBuilder> uriBuilder) {
        UriBuilder mBuilder = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.MEASUREMENTS)
                .path(MeasurementsResource.MEASUREMENT);
        for (Measurement measurement : dataService
                .getMeasurements(new MeasurementFilter(t))) {
            URI uri = mBuilder.build(measurement.getIdentifier());
            Resource mr = m.createResource(uri.toASCIIString());
            for (RDFLinker<Measurement> l : linker) {
                l.link(m, measurement, rights, mr, uriBuilder);
            }
        }
    }
}
