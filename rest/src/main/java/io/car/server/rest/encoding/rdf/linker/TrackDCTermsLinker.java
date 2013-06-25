
package io.car.server.rest.encoding.rdf.linker;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import io.car.server.core.entities.Track;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.TracksResource;
import io.car.server.rest.rights.AccessRights;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Jan Wirwahn
 */
public class TrackDCTermsLinker implements RDFLinker<Track> {
    
    public static final String ODBL_URL = "http://opendatacommons.org/licenses/odbl/";

    @Override
    public void link(Model m, Track t, AccessRights rights, Provider<UriBuilder> uriBuilder) {
        m.setNsPrefix(DCTerms.PREFIX, DCTerms.URI);
        String uri = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.TRACKS)
                .path(TracksResource.TRACK)
                .build(t.getIdentifier()).toASCIIString();

        m.createResource(uri).addProperty(DCTerms.rights, ODBL_URL);
    }
}
