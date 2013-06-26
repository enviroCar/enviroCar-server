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

/**
 *
 * @author Arne de Wall
 *
 */
public class W3CGeo {
    public static final String PREFIX = "geo";
    public static final String URI = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    private static final Model m = ModelFactory.createDefaultModel();
    public static final Property lat = m.createProperty(URI, "lat");
    public static final Property lon = m.createProperty(URI, "lon");

    private W3CGeo() {
    }
}
