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

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import javax.ws.rs.core.UriBuilder;
import java.util.Set;

public abstract class AbstractCollectionRDFEntityEncoder<V, T extends Iterable<V>>
        extends AbstractRDFEntityEncoder<T> {
    private final Set<RDFLinker<V>> linkers;

    public AbstractCollectionRDFEntityEncoder(Class<T> classType, Set<RDFLinker<V>> linkers) {
        super(classType);
        this.linkers = linkers;
    }

    @Override
    public Model encodeRDF(T t, Provider<UriBuilder> uriBuilder) {
        Model m = ModelFactory.createDefaultModel();
        for (V v : t) {
            Resource r = m.createResource(getURI(v, uriBuilder));
            for (RDFLinker<V> linker : linkers) {
                linker.link(m, v, r, uriBuilder);
            }
        }
        return m;
    }

    protected abstract String getURI(V t, Provider<UriBuilder> uri);
}
