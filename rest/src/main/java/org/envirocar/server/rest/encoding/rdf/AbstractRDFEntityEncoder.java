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

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;

import org.envirocar.server.rest.encoding.RDFEntityEncoder;

import org.envirocar.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractRDFEntityEncoder<T>
        extends AbstractRDFMessageBodyWriter<T>
        implements RDFEntityEncoder<T> {
    private Provider<AccessRights> rights;
    private Provider<UriInfo> uriInfo;

    public AbstractRDFEntityEncoder(Class<T> classType) {
        super(classType);
    }

    @Inject
    public void setRights(Provider<AccessRights> rights) {
        this.rights = rights;
    }

    @Inject
    public void setUriInfo(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public Model encodeRDF(T t) {
        return encodeRDF(t, rights.get());
    }

    @Override
    public Model encodeRDF(T t, AccessRights rights) {
        return encodeRDF(t, rights, new RequestUriBuilderProvider());
    }

    private class RequestUriBuilderProvider implements Provider<UriBuilder> {
        @Override
        public UriBuilder get() {
            return uriInfo.get().getBaseUriBuilder();
        }
    }
}
