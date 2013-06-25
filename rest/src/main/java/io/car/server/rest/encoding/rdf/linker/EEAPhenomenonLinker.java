package io.car.server.rest.encoding.rdf.linker;

import io.car.server.core.entities.Phenomenon;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.PhenomenonsResource;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.rights.AccessRights;

import javax.ws.rs.core.UriBuilder;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 * 
 * @author Arne de Wall
 * 
 */
public class EEAPhenomenonLinker implements RDFLinker<Phenomenon> {
    private static final String CO2 = "co2";
    private static final String URI_CO2 = "http://dd.eionet.europa.eu/vocabulary/aq/pollutant/71";

    @Override
    public void link(Model m, Phenomenon t, AccessRights rights,
            Provider<UriBuilder> uriBuilder) {
        UriBuilder phenomenonBuilder = uriBuilder.get()
                .path(RootResource.class).path(RootResource.PHENOMENONS)
                .path(PhenomenonsResource.PHENOMENON);

        if (t.getName().equals(CO2)) {
            m.createResource(
                    phenomenonBuilder.build(t.getName()).toASCIIString())
                    .addProperty(OWL.sameAs, m.createResource(URI_CO2));
        }
    }
}
