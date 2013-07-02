/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.server.rest.encoding.rdf.linker;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import static org.envirocar.server.rest.encoding.rdf.linker.SSN.URI;

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
