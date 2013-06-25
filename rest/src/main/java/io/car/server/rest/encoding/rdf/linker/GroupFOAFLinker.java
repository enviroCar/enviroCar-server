package io.car.server.rest.encoding.rdf.linker;

import io.car.server.core.GroupService;
import io.car.server.core.UserService;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.GroupsResource;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UsersResource;
import io.car.server.rest.rights.AccessRights;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

/**
 * 
 * @author Arne de Wall
 * 
 */
public class GroupFOAFLinker implements RDFLinker<Group> {
    public static final String PREFIX = "foaf";

    private final GroupService groupService;
    private final UserService userService;

    @Inject
    public GroupFOAFLinker(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @Override
    public void link(Model m, Group t, AccessRights rights,
            Provider<UriBuilder> uriBuilder) {
        UriBuilder groupURIBuilder = uriBuilder.get().path(RootResource.class)
                .path(RootResource.GROUPS).path(GroupsResource.GROUP);
        UriBuilder userURIBuilder = uriBuilder.get().path(RootResource.class)
                .path(RootResource.USERS).path(UsersResource.USER);

        m.setNsPrefix(PREFIX, FOAF.NS);
        URI uri = groupURIBuilder.build(t.getName());
        Resource p = m.createResource(uri.toASCIIString(), FOAF.Group);
        p.addLiteral(FOAF.name, t.getName());
        p.addProperty(FOAF.maker, m.createResource(
                userURIBuilder.build(t.getOwner().getName()).toASCIIString(),
                FOAF.Person));
        for (User u : groupService.getGroupMembers(t, null)) {
            p.addProperty(FOAF.member, m.createResource(
                    userURIBuilder.build(u.getName()).toASCIIString(),
                    FOAF.Person));
        }
    }
}
