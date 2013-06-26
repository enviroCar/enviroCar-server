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
package org.envirocar.server.rest.encoding.rdf.linker;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.UsersResource;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 *
 * @author Arne de Wall
 *
 */
public class GroupFOAFLinker implements RDFLinker<Group> {
    public static final String PREFIX = "foaf";
    private final GroupService groupService;

    @Inject
    public GroupFOAFLinker(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void link(Model m, Group t, AccessRights rights,
                     Resource p, Provider<UriBuilder> uriBuilder) {
        UriBuilder b = uriBuilder.get().path(RootResource.class)
                .path(RootResource.USERS).path(UsersResource.USER);

        m.setNsPrefix(PREFIX, FOAF.NS);
        p.addProperty(RDF.type, FOAF.Group);
        if (rights.canSeeNameOf(t)) {
            p.addLiteral(FOAF.name, t.getName());
        }
        if (rights.canSeeOwnerOf(t)) {
            p.addProperty(FOAF.maker, m.createResource(
                    b.build(t.getOwner().getName()).toASCIIString(),
                    FOAF.Person));
        }
        if (rights.canSeeMembersOf(t)) {
            for (User u : groupService.getGroupMembers(t, null)) {
                p.addProperty(FOAF.member, m.createResource(
                        b.build(u.getName()).toASCIIString(),
                        FOAF.Person));
            }
        }
    }
}
