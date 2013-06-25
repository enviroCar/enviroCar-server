package io.car.server.rest.encoding.rdf.linker;

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
