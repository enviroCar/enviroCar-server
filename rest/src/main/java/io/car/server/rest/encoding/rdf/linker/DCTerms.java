
package io.car.server.rest.encoding.rdf.linker;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 *
 * @author jan
 */
public class DCTerms {
    public static final String URI ="http://purl.org/dc/elements/1.1/";
    public static final String PREFIX ="dcterms";
    private static final Model m = ModelFactory.createDefaultModel();
    public static final Property rights = m.createProperty(URI, "rights");      
}
