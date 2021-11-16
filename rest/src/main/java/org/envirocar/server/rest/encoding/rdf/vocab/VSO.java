/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author Jan Wirwahn
 */
public class VSO {
    public static final String URI = "http://purl.org/vso/ns#";
    public static final String PREFIX = "vso";
    private static final Model m = ModelFactory.createDefaultModel();
    public static final Resource Automobile =
            m.createResource(URI + "Automobile");
    public static final Property fuelType =
            m.createProperty(URI, "fuelType");
    public static final Property modelDate =
            m.createProperty(URI, "modelDate");

    private VSO() {
    }
}
