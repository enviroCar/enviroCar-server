
package io.car.server.rest.encoding.rdf.linker;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import io.car.server.core.entities.Sensor;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.SensorsResource;
import io.car.server.rest.rights.AccessRights;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author jan
 */
public class SensorDCTermsLinker implements RDFLinker<Sensor>{
    
    public static final String ODBL_URL = "http://opendatacommons.org/licenses/odbl/";

    @Override
    public void link(Model m, Sensor t, AccessRights rights, Provider<UriBuilder> uriBuilder) {
        m.setNsPrefix(DCTerms.PREFIX, DCTerms.URI);
        String uri = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.SENSORS)
                .path(SensorsResource.SENSOR)
                .build(t.getIdentifier()).toASCIIString();

        m.createResource(uri).addProperty(DCTerms.rights, ODBL_URL);
    }
}
