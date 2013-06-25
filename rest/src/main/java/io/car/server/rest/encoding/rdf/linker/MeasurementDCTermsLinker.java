
package io.car.server.rest.encoding.rdf.linker;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import io.car.server.core.entities.Measurement;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.MeasurementsResource;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.rights.AccessRights;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Jan Wirwahn
 */
public class MeasurementDCTermsLinker implements RDFLinker<Measurement>{

    public static final String ODBL_URL = "http://opendatacommons.org/licenses/odbl/";

    @Override
    public void link(Model m, Measurement t, AccessRights rights, Provider<UriBuilder> uriBuilder) {
        String uri = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.MEASUREMENTS)
                .path(MeasurementsResource.MEASUREMENT)
                .build(t.getIdentifier()).toASCIIString();

        m.createResource(uri).addProperty(DCTerms.rights, ODBL_URL);

    }
}
