/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.rest.encoding.rdf.linker;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author jan
 */
public class GR {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String URI = "http://purl.org/goodrelations/v1#";
    public static final Property hasManufacturer = m.createProperty(URI, "hasManufacturer");
    public static final Property hasMakeAndModel = m.createProperty(URI, "hasMakeAndModel");
}
