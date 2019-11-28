/*
 * Copyright (C) 2013-2018 The enviroCar project
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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import org.envirocar.server.rest.PrefixedUriInfo;
import org.envirocar.server.rest.encoding.RDFEntityEncoder;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractRDFEntityEncoder<T>
        extends AbstractRDFMessageBodyWriter<T>
        implements RDFEntityEncoder<T> {
    private Provider<UriInfo> uriInfo;
    private Provider<HttpHeaders> headers;

    public AbstractRDFEntityEncoder(Class<T> classType) {
        super(classType);
    }

    @Inject
    public void setUriInfo(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Inject
    public void setHeaders(Provider<HttpHeaders> headers) {
        this.headers = headers;
    }

    @Override
    public Model encodeRDF(T t) {
        return encodeRDF(t, new RequestUriBuilderProvider());
    }

    private class RequestUriBuilderProvider implements Provider<UriBuilder> {
        @Override
        public UriBuilder get() {
            return new PrefixedUriInfo(uriInfo.get(), headers.get()).getBaseUriBuilder();
        }
    }
}
