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
public class DUL {

    public static final String URI = "http://www.loa-cnr.it/ontologies/DUL.owl#";
    public static final String PREFIX = "dul";
    private static final Model m = ModelFactory.createDefaultModel();
    public static final Resource UnitOfMeasure =
            m.createResource(URI + "UnitOfMeasure");
    public static final Resource TimeInterval =
            m.createResource(URI + "TimeInterval");
    public static final Resource Amount =
            m.createResource(URI + "Amount");
    
    public static final Property isClassifiedBy =
            m.createProperty(URI, "isClassifiedBy");
    public static final Property hasDataValue =
            m.createProperty(URI, "hasDataValue");

    private DUL() {
    }
}
