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
package org.envirocar.server.rest.encoding.rdf.linker;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import org.envirocar.server.rest.encoding.rdf.vocab.DUL;

import javax.ws.rs.core.UriBuilder;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractDULLinker<T> extends AbstractLinker<T> {

    protected void addDULNamespace(Model m) {
        m.setNsPrefix(DUL.PREFIX, DUL.URI);
    }

    @Override
    protected void addNamespaces(Model m) {
        addDULNamespace(m);
    }

    protected String fragment(Resource resource, String fragment) {
        return UriBuilder.fromUri(resource.getURI()).fragment(fragment).build().toASCIIString();
    }
}
