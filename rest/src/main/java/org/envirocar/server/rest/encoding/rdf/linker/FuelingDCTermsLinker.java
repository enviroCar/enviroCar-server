package org.envirocar.server.rest.encoding.rdf.linker;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.rest.encoding.rdf.vocab.DCTerms;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Linker for {@link Fueling}s in the {@link DCTerms} name space.
 *
 * @author Christian Autermann
 */
public class FuelingDCTermsLinker extends DCTermsLinker<Fueling> {
    @Override
    public void linkRest(Model m, Fueling t, AccessRights rights,
                         Resource r, Provider<UriBuilder> uriBuilder) {
    }
}
