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
public class VSO {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String URI = "http://purl.org/vso/ns#";
    public static final Resource Automobile = m.createResource(URI + "Automobile");
    public static final Property fuelType = m.createProperty(URI, "fuelType");
    public static final Property modelDate = m.createProperty ( URI, "modelDate");
}
