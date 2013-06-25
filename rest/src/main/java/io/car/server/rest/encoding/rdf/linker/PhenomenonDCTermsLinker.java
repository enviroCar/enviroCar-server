package io.car.server.rest.encoding.rdf.linker;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import io.car.server.core.entities.Phenomenon;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.PhenomenonsResource;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.rights.AccessRights;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Jan Wirwahn
 */
public class PhenomenonDCTermsLinker implements RDFLinker<Phenomenon> {

    public static final String ODBL_URL = "http://opendatacommons.org/licenses/odbl/";

    @Override
    public void link(Model m, Phenomenon t, AccessRights rights, Provider<UriBuilder> uriBuilder) {
        String uri = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.PHENOMENONS)
                .path(PhenomenonsResource.PHENOMENON)
                .build(t.getName()).toASCIIString();

        m.createResource(uri).addProperty(DCTerms.rights, ODBL_URL);

    }
}
