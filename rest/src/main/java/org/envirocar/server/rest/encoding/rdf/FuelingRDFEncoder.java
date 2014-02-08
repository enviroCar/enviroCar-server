package org.envirocar.server.rest.encoding.rdf;

import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.rest.resources.FuelingsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.UserResource;
import org.envirocar.server.rest.resources.UsersResource;

import com.google.inject.Inject;

/**
 * RDF Encoder for {@link Fueling}s.
 *
 * @author Christian Autermann
 */
public class FuelingRDFEncoder extends AbstractLinkerRDFEntityEncoder<Fueling> {
    /**
     * Creates a new {@code FuelingRDFEncoder} using the supplied
     * {@link RDFLinker}.
     *
     * @param linkers the {@link RDFLinker}
     */
    @Inject
    public FuelingRDFEncoder(Set<RDFLinker<Fueling>> linkers) {
        super(Fueling.class, linkers);
    }

    @Override
    protected String getURI(Fueling v,
                            com.google.inject.Provider<UriBuilder> uri) {
        return uri.get().path(RootResource.class)
                .path(RootResource.USERS)
                .path(UsersResource.USER)
                .path(UserResource.FUELINGS)
                .path(FuelingsResource.FUELING)
                .build(v.getUser().getName(),
                       v.getIdentifier()).toASCIIString();
    }
}
