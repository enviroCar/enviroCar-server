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
package org.envirocar.server.rest.encoding.rdf.vocab;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author Jan Wirwahn
 */
public class SSN {
    public static final String URI = "http://purl.oclc.org/NET/ssnx/ssn#";
    public static final String PREFIX = "ssn";
    private static final Model m = ModelFactory.createDefaultModel();
    public static final Resource Sensor =
            m.createResource(URI + "Sensor");
    public static final Resource Sensing =
            m.createResource(URI + "Sensing");
    public static final Resource Property =
            m.createResource(URI + "Property");
    public static final Resource FeatureOfInterest =
            m.createResource(URI + "FeatureOfInterest");
    public static final Resource Observation =
            m.createResource(URI + "Observation");
    public static final Resource SensorOutput =
            m.createResource(URI + "SensorOutput");
    public static final Property hasInput =
            m.createProperty(URI, "hasInput");
    public static final Property hasOutput =
            m.createProperty(URI, "hasOutput");
    public static final Property isPropertyOf =
            m.createProperty(URI, "isPropertyOf");
    public static final Property isClassifiedBy =
            m.createProperty(URI, "isClassifiedBy");
    public static final Property hasProperty =
            m.createProperty(URI, "hasProperty");
    public static final Property observedProperty =
            m.createProperty(URI, "observedProperty");
    public static final Property observationResult =
            m.createProperty(URI, "observationResult");
    public static final Property isProducedBy =
            m.createProperty(URI, "isProducedBy");
    public static final Property hasValue =
            m.createProperty(URI, "hasValue");
    public static final Property observedBy =
            m.createProperty(URI, "observedBy");
    public static final Property featureOfInterest =
            m.createProperty(URI, "featureOfInterest");
    public static final Property observationSamplingTime =
            m.createProperty(URI, "observationSamplingTime");
    public static final Property observationResultTime =
            m.createProperty(URI, "observationResultTime");
    public static final Property implement =
            m.createProperty(URI, "implements");

    private SSN() {
    }
}
