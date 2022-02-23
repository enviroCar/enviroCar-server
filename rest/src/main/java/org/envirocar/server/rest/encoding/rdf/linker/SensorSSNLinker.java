/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.rest.encoding.rdf.vocab.SSN;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Provider;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Jan Wirwahn
 */
public class SensorSSNLinker extends AbstractSSNLinker<Sensor> {

    private static final String SENSOR_FRAGMENT = "sensor";

    @Override
    protected void linkInternal(Model m, Sensor t, AccessRights rights, Resource uri,
            Provider<UriBuilder> uriBuilder) {
        Resource sensor = m.createResource(fragment(uri, SENSOR_FRAGMENT));
        sensor.addProperty(RDF.type, SSN.Sensor);
    }
}
