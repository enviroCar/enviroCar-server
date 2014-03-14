/*
 * Copyright (C) 2013 The enviroCar project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.envirocar.server.rest.encoding.rdf;

import java.util.Set;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Fuelings;
import org.envirocar.server.rest.resources.FuelingsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.UserResource;
import org.envirocar.server.rest.resources.UsersResource;

import com.google.inject.Inject;

/**
 * RDF encoder for {@link Fuelings}.
 *
 * @author Christian Autermann
 */
@Provider
public class FuelingsRDFEncoder extends AbstractCollectionRDFEntityEncoder<Fueling, Fuelings> {
    /**
     * Creates a new {@code FuelingsRDFEncoder} using the supplied
     * {@link RDFLinker}.
     *
     * @param linkers the {@link RDFLinker}
     */
    @Inject
    public FuelingsRDFEncoder(Set<RDFLinker<Fueling>> linkers) {
        super(Fuelings.class, linkers);
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
