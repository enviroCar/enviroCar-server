/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.rest.encoding.rdf.linker;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import io.car.server.core.statistics.Statistic;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.rights.AccessRights;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author jan
 */
public class StatisticDCTermsLinker implements RDFLinker<Statistic> {
    
    public static final String ODBL_URL = "http://opendatacommons.org/licenses/odbl/";

    @Override
    public void link(Model m, Statistic t, AccessRights rights, Provider<UriBuilder> uriBuilder) {
       // FIXME add track/user/sensor etc. to the statistic entity to allow URI building
    }
}
