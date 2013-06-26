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

import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Groups;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.resources.GroupsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.UserResource;
import org.envirocar.server.rest.resources.UsersResource;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserFOAFLinker implements RDFLinker<User> {
    public static final String PREFIX = "foaf";
    private final GroupService groupService;
    private final FriendService friendService;

    @Inject
    public UserFOAFLinker(GroupService groupService, FriendService friendService) {
        this.groupService = groupService;
        this.friendService = friendService;
    }

    @Override
    public void link(Model m, User t, AccessRights rights,
                     Resource p, Provider<UriBuilder> uriBuilder) {
        m.setNsPrefix(PREFIX, FOAF.NS);
        p.addProperty(RDF.type, FOAF.Person);
        p.addLiteral(FOAF.nick, t.getName());
        if (t.hasFirstName() && rights.canSeeFirstNameOf(t)) {
            p.addLiteral(FOAF.firstName, t.getFirstName());
            p.addLiteral(FOAF.givenname, t.getFirstName());
        }
        if (t.hasLastName() && rights.canSeeLastNameOf(t)) {
            p.addLiteral(FOAF.surname, t.getLastName());
            p.addLiteral(FOAF.family_name, t.getLastName());
        }
        if (t.hasDayOfBirth() && rights.canSeeDayOfBirthOf(t)) {
            p.addLiteral(FOAF.birthday, t.getDayOfBirth());
        }
        if (t.hasUrl() && rights.canSeeUrlOf(t)) {
            p.addProperty(FOAF.homepage, m.createResource(t.getUrl()
                    .toString()));
        }
        if (t.hasGender() && rights.canSeeGenderOf(t)) {
            p.addLiteral(FOAF.gender, t.getGender().toString().toLowerCase());
        }
        if (rights.canSeeAvatarOf(t)) {
            p.addProperty(FOAF.img, m.createResource(UriBuilder.fromUri(p
                    .getURI()).path(UserResource.AVATAR).build().toASCIIString()));
        }
        if (t.hasMail() && rights.canSeeMailOf(t)) {
            p.addLiteral(FOAF.mbox, "mailto:" + t.getMail());
        }
        if (rights.canSeeFriendsOf(t)) {
            Users friends = friendService.getFriends(t);
            UriBuilder userURIBuilder = uriBuilder.get()
                    .path(RootResource.class)
                    .path(RootResource.USERS)
                    .path(UsersResource.USER);
            for (User f : friends) {
                String friendURI = userURIBuilder
                        .build(f.getName()).toASCIIString();
                p.addProperty(FOAF.knows, m
                        .createResource(friendURI, FOAF.Person));
            }
        }
        if (rights.canSeeGroupsOf(t)) {
            Groups groups = groupService.getGroups(t, null);
            UriBuilder groupUriBuilder = uriBuilder.get()
                    .path(RootResource.class)
                    .path(RootResource.GROUPS)
                    .path(GroupsResource.GROUP);
            for (Group group : groups) {
                m.createResource(groupUriBuilder.build(group.getName())
                        .toASCIIString(), FOAF.Group)
                        .addProperty(FOAF.member, p);
            }
        }
    }
}
