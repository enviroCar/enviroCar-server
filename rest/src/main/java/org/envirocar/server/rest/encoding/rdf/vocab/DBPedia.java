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
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class DBPedia {
    public static final String URI = "http://dbpedia.org/resource/";
    public static final String PREFIX = "dbpedia";
    private static final Model m = ModelFactory.createDefaultModel();
    public static final Resource DBPEDIA_GASOLINE =
            m.createResource(URI + "Gasoline");
    public static final Resource DBPEDIA_DIESEL =
            m.createResource(URI + "Diesel");
    public static final Resource DBPEDIA_BIODIESEL =
            m.createResource(URI + "Biodiesel");
    public static final Resource DBPEDIA_KEROSENE =
            m.createResource(URI + "Kerosene");

    private DBPedia() {
    }
}
