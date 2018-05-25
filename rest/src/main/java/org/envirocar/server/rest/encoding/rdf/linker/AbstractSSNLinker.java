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

import org.envirocar.server.rest.encoding.rdf.vocab.SSN;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractSSNLinker<T> extends AbstractDULLinker<T> {

    @Override
    protected void addNamespaces(Model m) {
        super.addNamespaces(m);
        addSSNNamespace(m);
    }

    protected void addSSNNamespace(Model m) {
        m.setNsPrefix(SSN.PREFIX, SSN.URI);
    }
}
