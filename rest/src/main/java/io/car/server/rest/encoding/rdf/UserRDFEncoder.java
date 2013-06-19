/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest.encoding.rdf;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.VCARD;

import io.car.server.core.entities.User;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UsersResource;
import io.car.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@javax.ws.rs.ext.Provider
public class UserRDFEncoder extends AbstractRDFEntityEncoder<User> {
    public UserRDFEncoder() {
        super(User.class);
    }

    @Override
    public Model encodeRDF(User t, AccessRights rights,
                           Provider<UriBuilder> uriBuilder) {
        Model model = ModelFactory.createDefaultModel();
        URI uri = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.USERS)
                .path(UsersResource.USER).build(t.getName());
        model.createResource(uri.toASCIIString())
                .addProperty(VCARD.EMAIL, t.getMail())
                .addProperty(VCARD.NICKNAME, t.getName());
        return model;
    }
}
